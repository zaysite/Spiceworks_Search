/*
 * The MIT License
 *
 * Copyright 2016 Jordan Kahtava.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package spiceworks_archive_search;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Collator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
//import javafx.scene.control.TextField;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PositiveScoresOnlyCollector;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import spiceworks_archive_javafx.Software;
import spiceworks_archive_javafx.Ticket;

/**
 *
 * @author it.student
 */
public class Spiceworks_Archive_Logic
{

    private static Spiceworks_Archive_Data data_Layer;

    private static ObservableList<Ticket> list_Tickets;
    private static ObservableList<Ticket> list_Tickets_With_Attachments;
    private static ObservableList<String> list_Ticket_IDs;

    private static ObservableList<Software> list_Software;
    private static ObservableList<String> list_Software_IDs;
    
    public static final String TICKET_INDEX_PATH = "C:\\Spiceworks_Archive\\INDEX\\TICKET";
    public static final String SOFTWARE_INDEX_PATH = "C:\\Spiceworks_Archive\\INDEX\\SOFTWARE";
    public static final File TICKET_INDEX_DIRECTORY = new File(TICKET_INDEX_PATH);
    public static final File SOFTWARE_INDEX_DIRECTORY = new File(SOFTWARE_INDEX_PATH);
    private static final int MAX_HITS = 10000;
    private static boolean has_Attachment = false;

    private javafx.scene.control.TextField message_Box;

    public Spiceworks_Archive_Logic()
    {

        data_Layer = new Spiceworks_Archive_Data();
    }

    public void checkTables()
    {
        data_Layer.CheckViewsExist();
    }

    public void setMessageBox(javafx.scene.control.TextField message_Box)
    {

        this.message_Box = message_Box;

    }

    public void toggleAttachmentQuery()
    {
        if (has_Attachment)
        {
            has_Attachment = false;
        }
        else
        {
            has_Attachment = true;
        }
    }

    public ObservableList<Software> searchSoftware(String terms, String categories,javafx.scene.control.TextField message_Box)
    {
        
        String query = "(note:(" + terms + ") category:(" + terms + ") serial_number:(" + terms + "))";

        if(terms.trim().length() != 0 && categories.trim().length() != 0)
        {
            query = "(note:(" + terms + ") category:(" + terms + ") serial_number:(" + terms + ")) AND (library_category:\"" + categories + "\")";
        }
        else if (terms.trim().length() == 0 && categories.trim().length() != 0)
        {
            query = "(library_category:\"" + categories + "\")";
        }
        else if (terms.trim().length() == 0 && categories.trim().length() == 0)
        {
            message_Box.setText("");
            return list_Software;
        }

        message_Box.setText("Search: " + query);
        
        return searchSoftwareIndex(query,message_Box);
    }
    
    public ObservableList<Ticket> searchTicket(String terms, String technician)
    {
        String[] first_Last = technician.split("[ ]");
        String query = "description:( " + terms + ") summary:(" + terms + ") attachment_name:(" + terms + ")";
        if (first_Last.length == 2)
        {
            query = "(description:(" + terms + ") summary:(" + terms + ") attachment_name:(" + terms + ")) AND (first_name:\"" + first_Last[0] + "\" AND last_name:\"" + first_Last[1] + "\")";

        }

        if (first_Last.length == 2 && terms.trim().length() == 0)
        {
            query = "(first_name:\"" + first_Last[0] + "\" AND last_name:\"" + first_Last[1] + "\")";
        }
        else if (terms.trim().length() == 0 && has_Attachment == false)
        {
            message_Box.setText("");
            return list_Tickets;
        }
        else if (terms.trim().length() == 0 && has_Attachment == true)
        {
            message_Box.setText("");
            return list_Tickets_With_Attachments;
        }

        message_Box.setText("Search: " + query);
        return searchTicketIndex(query);
    }

    public ObservableList<String> getTechnicians()
    {
        ObservableList<String> technician_Observable_List = null;
        try
        {
            technician_Observable_List = FXCollections.observableArrayList();
            ResultSet technicians = data_Layer.getTechnicians();

            //The first value in the list should be blank incase we dont want to search based on a name.
            technician_Observable_List.add("");
            while (technicians.next())
            {
                String first_Name = technicians.getString("first_name");
                String last_Name = technicians.getString("last_name");
                technician_Observable_List.add(first_Name + " " + last_Name);
            }

        }
        catch (Spiceworks_Archive_Exception ex)
        {
            System.err.println(ex.getMessage());
        }
        catch (SQLException ex)
        {
            System.err.println("TECHNICIAN FIELD COULD NOT BE ACCESSED: " + ex.getMessage());
        }

        return technician_Observable_List;
    }
    
    public ObservableList<String> getCategories()
    {
        ObservableList<String> category_Observable_List = FXCollections.observableArrayList();
        try
        {
            
            ResultSet categories = data_Layer.getSoftwareCategories();

            //The first value in the list should be blank incase we dont want to search based on a name.
            
            while (categories.next())
            {
                String category_Name = categories.getString("library_category");
                category_Observable_List.add(category_Name);
            }

        }
        catch (Spiceworks_Archive_Exception ex)
        {
            System.err.println(ex.getMessage());
        }
        catch (SQLException ex)
        {
            System.err.println("TECHNICIAN FIELD COULD NOT BE ACCESSED: " + ex.getMessage());
        }
 
        category_Observable_List = new SortedList<>(category_Observable_List, Collator.getInstance());
        ObservableList<String> sorted_List = FXCollections.observableArrayList();
        sorted_List.addAll(category_Observable_List);

        return sorted_List;
    }

    public ObservableList<Ticket> getTickets()
    {
        list_Tickets = FXCollections.observableArrayList();
        list_Ticket_IDs = FXCollections.observableArrayList();
        list_Tickets_With_Attachments = FXCollections.observableArrayList();
        ResultSet database_Results;
        try
        {

            database_Results = data_Layer.getTickets();
            if (database_Results != null)
            {
                while (database_Results.next())
                {

                    String id = database_Results.getString("id");
                    String summary = database_Results.getString("summary");
                    String description = database_Results.getString("description");
                    String first_Name = database_Results.getString("first_name");
                    String last_Name = database_Results.getString("last_name");
                    String attachment_Name = database_Results.getString("attachment_name");

                    int duplicate_Ticket = list_Ticket_IDs.indexOf(id);
                    if (duplicate_Ticket == -1)
                    {
                        Ticket current_Ticket = new Ticket(id, summary, description, first_Name, last_Name, attachment_Name);
                        list_Tickets.add(current_Ticket);
                        if (attachment_Name != null)
                        {
                            list_Tickets_With_Attachments.add(current_Ticket);
                        }
                        list_Ticket_IDs.add(id);
                    }
                    else
                    {
                        list_Tickets.get(duplicate_Ticket).addAttachment_Name(attachment_Name);

                    }

                }

            }

        }
        catch (Spiceworks_Archive_Exception | SQLException ex)
        {
            Logger.getLogger(Spiceworks_Archive_Logic.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list_Tickets;
    }

    public ObservableList<Software> getSoftware()
    {
        list_Software = FXCollections.observableArrayList();
        list_Software_IDs = FXCollections.observableArrayList();
        
        ResultSet database_Results;
        try
        {

            database_Results = data_Layer.getSoftware();
            if (database_Results != null)
            {
                while (database_Results.next())
                {
                    String id = database_Results.getString("id");
                    String category = database_Results.getString("category");
                    String serial_Number = database_Results.getString("serial_Number");
                    String library_ID = database_Results.getString("library_ID");
                    String created_on = database_Results.getString("created_on");
                    String note = database_Results.getString("note");
                    String status = database_Results.getString("status");
                    String checked_out_by = database_Results.getString("checked_out_by");
                    String checked_out_at = database_Results.getString("checked_out_at");
                    String last_checked_out_by = database_Results.getString("last_checked_out_by");
                    String last_checked_out_at = database_Results.getString("last_checked_out_at");

                    Software current_Software = new Software(id,category, serial_Number, library_ID, created_on, note, status,checked_out_by,checked_out_at,last_checked_out_by,last_checked_out_at);
                    list_Software_IDs.add(id);
                    list_Software.add(current_Software);

                }

            }

        }
        catch (Spiceworks_Archive_Exception | SQLException ex)
        {
            Logger.getLogger(Spiceworks_Archive_Logic.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list_Software;
    }

    public boolean createLuceneIndexTicket()
    {
        if(Files.exists(TICKET_INDEX_DIRECTORY.toPath()) == false)
        {
            try
            {
                Files.createDirectory(TICKET_INDEX_DIRECTORY.toPath());
            }
            catch (IOException ex)
            {
                Logger.getLogger(Spiceworks_Archive_Logic.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (TICKET_INDEX_DIRECTORY.listFiles().length <= 0)
        {
            try
            {
                SimpleAnalyzer analyzer = new SimpleAnalyzer();

                IndexWriterConfig index_Writer_Configuration = new IndexWriterConfig(analyzer);
                int index_Document_Count = 0;

                try (IndexWriter index_Writer = new IndexWriter(FSDirectory.open(TICKET_INDEX_DIRECTORY.toPath()), index_Writer_Configuration))
                {
                    System.out.println("Indexing DB to Directory: " + TICKET_INDEX_DIRECTORY + " ...");
                    index_Document_Count = indexTicketDocuments(index_Writer);
                }
                catch (Spiceworks_Archive_Exception | SQLException ex)
                {

                }
                System.out.println("Total Records Indexed: " + index_Document_Count);

            }
            catch (IOException ex)
            {

            }
            return true;
        }
        
        return false;
    }
    public boolean createLuceneIndexSoftware()
    {
        if(Files.exists(SOFTWARE_INDEX_DIRECTORY.toPath()) == false)
        {
            try
            {
                Files.createDirectory(SOFTWARE_INDEX_DIRECTORY.toPath());
            }
            catch (IOException ex)
            {
                Logger.getLogger(Spiceworks_Archive_Logic.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (SOFTWARE_INDEX_DIRECTORY.listFiles().length <= 0)
        {
            try
            {
                SimpleAnalyzer analyzer = new SimpleAnalyzer();

                IndexWriterConfig index_Writer_Configuration = new IndexWriterConfig(analyzer);
                int index_Document_Count = 0;

                try (IndexWriter index_Writer = new IndexWriter(FSDirectory.open(SOFTWARE_INDEX_DIRECTORY.toPath()), index_Writer_Configuration))
                {
                    System.out.println("Indexing DB to Directory: " + SOFTWARE_INDEX_DIRECTORY + " ...");
                    index_Document_Count = indexSoftwareDocuments(index_Writer);
                }
                catch (Spiceworks_Archive_Exception | SQLException ex)
                {

                }
                System.out.println("Total Records Indexed: " + index_Document_Count);

            }
            catch (IOException ex)
            {

            }
            return true;
        }
        
        return false;
    }

    private int indexTicketDocuments(IndexWriter index_Writer) throws Spiceworks_Archive_Exception, SQLException, IOException
    {
        ResultSet database_Results = data_Layer.getLuceneIndexTicketResults();
        int document_Count = 0;
        while (database_Results.next())
        {
            Document index_Document = new Document();
            //"select id,summary,description,first_name,last_name,attachment_name from ticket_plus_attachments"

            int id = database_Results.getInt("id");
            String summary = database_Results.getString("summary");
            String first_name = database_Results.getString("first_name");
            String last_name = database_Results.getString("last_name");
            String description = database_Results.getString("description");
            String attachment_Name = database_Results.getString("attachment_name");

            if (summary == null)
            {
                summary = "";
            }
            if (first_name == null)
            {
                first_name = "";
            }
            if (last_name == null)
            {
                last_name = "";
            }
            if (description == null)
            {
                description = "";
            }
            if (attachment_Name == null)
            {
                attachment_Name = "";
            }

            index_Document.add(new StoredField("id", id));
            index_Document.add(new TextField("summary", summary, Field.Store.YES));
            index_Document.add(new TextField("description", description, Field.Store.YES));
            index_Document.add(new TextField("first_name", first_name, Field.Store.YES));
            index_Document.add(new TextField("last_name", last_name, Field.Store.YES));
            index_Document.add(new TextField("attachment_name", attachment_Name, Field.Store.YES));

            index_Writer.addDocument(index_Document);
            document_Count++;

        }

        return document_Count;
    }
    private int indexSoftwareDocuments(IndexWriter index_Writer) throws Spiceworks_Archive_Exception, SQLException, IOException
    {
        ResultSet database_Results = data_Layer.getLuceneIndexSoftwareResults();
        int document_Count = 0;
        while (database_Results.next())
        {
            Document index_Document = new Document();
            //"select id,summary,description,first_name,last_name,attachment_name from ticket_plus_attachments"

            int id = database_Results.getInt("id");
            String category = database_Results.getString("category");
            String serial_number = database_Results.getString("serial_number");
            String note = database_Results.getString("note");
            String library_category = database_Results.getString("library_category");
            

            if (category == null)
            {
                category = "";
            }
            if (serial_number == null)
            {
                serial_number = "";
            }
            if (note == null)
            {
                note = "";
            }
            if (library_category == null)
            {
                library_category = "";
            }
            

            index_Document.add(new StoredField("id", id));
            index_Document.add(new TextField("category", category, Field.Store.YES));
            index_Document.add(new TextField("serial_number", serial_number, Field.Store.YES));
            index_Document.add(new TextField("note", note, Field.Store.YES));
            index_Document.add(new TextField("library_category", library_category, Field.Store.YES));
            

            index_Writer.addDocument(index_Document);
            document_Count++;

        }

        return document_Count;
    }

    private ObservableList<Ticket> searchTicketIndex(String query_String)
    {
        ObservableList<Ticket> ticket_List = FXCollections.observableArrayList();

        try
        {
            Directory directory = FSDirectory.open(TICKET_INDEX_DIRECTORY.toPath());
            MultiFieldQueryParser query_Parser = new MultiFieldQueryParser(new String[]
            {
                "summary", "description", "first_name", "last_name", "attachment_name"
            }, new StandardAnalyzer());
            DirectoryReader reader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(reader);

            query_Parser.setPhraseSlop(0);

            query_Parser.setLowercaseExpandedTerms(true);

            Query query = query_Parser.parse(query_String);

            //TopDocs topDocs = searcher.search(query, MAX_HITS);
            TopScoreDocCollector collector = TopScoreDocCollector.create(MAX_HITS);
            searcher.search(query, new PositiveScoresOnlyCollector(collector));
            TopDocs topDocs = collector.topDocs();

            ScoreDoc[] hits = topDocs.scoreDocs;

            int ticket_Count = 0;
            for (int i = 0; i < hits.length; i++)
            {

                int docId = hits[i].doc;

                Document d = searcher.doc(docId);
                int index = list_Ticket_IDs.indexOf(d.get("id"));
                Ticket current = list_Tickets.get(index);
                if (ticket_List.indexOf(current) == -1)
                {
                    if (has_Attachment)
                    {

                        if (current.hasAttachment())
                        {
                            ticket_Count++;
                            ticket_List.add(current);
                        }

                    }
                    else
                    {
                        ticket_Count++;
                        ticket_List.add(current);
                    }
                }
            }
            message_Box.setText(message_Box.getText() + ", Record(s) Found: " + ticket_Count);
            if (hits.length == 0)
            {

                System.out.println("No Data Founds");

            }

        }
        catch (IOException | ParseException ex)
        {

        }
        return ticket_List;
    }
    private ObservableList<Software> searchSoftwareIndex(String query_String, javafx.scene.control.TextField message_Box)
    {
        ObservableList<Software> software_List = FXCollections.observableArrayList();

        try
        {
            Directory directory = FSDirectory.open(SOFTWARE_INDEX_DIRECTORY.toPath());
            MultiFieldQueryParser query_Parser = new MultiFieldQueryParser(new String[]
            {
                "category", "serial_number", "note", "library_category"
            }, new StandardAnalyzer());
            DirectoryReader reader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(reader);

            query_Parser.setPhraseSlop(0);

            query_Parser.setLowercaseExpandedTerms(true);

            Query query = query_Parser.parse(query_String);

            //TopDocs topDocs = searcher.search(query, MAX_HITS);
            TopScoreDocCollector collector = TopScoreDocCollector.create(MAX_HITS);
            searcher.search(query, new PositiveScoresOnlyCollector(collector));
            TopDocs topDocs = collector.topDocs();

            ScoreDoc[] hits = topDocs.scoreDocs;

            int software_Count = 0;
            for (int i = 0; i < hits.length; i++)
            {

                int docId = hits[i].doc;

                Document document = searcher.doc(docId);
                int index = list_Software_IDs.indexOf(document.get("id"));
                Software current = list_Software.get(index);
                if (software_List.indexOf(current) == -1) //MAKES SURE THERE ARE NO DUPLICATE ROWS PROCESSED
                {
                        software_Count++;
                        software_List.add(current);
                }
            }
            message_Box.setText(message_Box.getText() + ", Record(s) Found: " + software_Count);
            if (hits.length == 0)
            {

                System.out.println("No Data Founds");

            }

        }
        catch (IOException | ParseException ex)
        {

        }
        return software_List;
    }
    
    public Software insertSoftwareRecord(String library_id, String description, String serial_number,String note,String category_Name,String created_on)
    {
        if(library_id == null )
        {
            library_id = "";
        }
        if(description == null)
        {
            description = "";
        }
        if(serial_number == null)
        {
            serial_number = "";
        }
        if(note == null)
        {
            note = "";
        }
        if(category_Name == null)
        {
            category_Name = "";
        }
        int id = data_Layer.insertSoftwareRecord(library_id, description, serial_number, note, category_Name, created_on);
        System.out.println(id);
        Software new_Record  = new Software(Integer.toString(id), description, serial_number, library_id, created_on, note, "IN", "NO ONE", "", "NO ONE", "");
        list_Software_IDs.add(Integer.toString(id));
        return new_Record;
    }
    public void insertSoftwareCategory(String category)
    {
        
       data_Layer.insertSoftwareCategory(category);
    }
    public void updateCheckInFields(String status,String checked_out_by, String checked_out_at,String last_checked_out_by,String last_checked_out_at, String id)
    {
        
       data_Layer.updateCheckInFields(status, checked_out_by, checked_out_at, last_checked_out_by, last_checked_out_at, id);
    }
    
    public void updateSoftwareRecord(String id, String library_id, String description, String serial_number,String note,String category_Name)
    {
        if(library_id == null)
        {
            library_id = "";
        }
        if(description == null)
        {
            description = "";
        }
        if(serial_number == null)
        {
            serial_number = "";
        }
        if(note == null)
        {
            note = "";
        }
        if(category_Name == null)
        {
            category_Name = "";
        }
        int software_Index = list_Software_IDs.indexOf(id);
        Software selection = list_Software.get(software_Index);
        selection.setLibraryID(library_id);
        selection.setCategory(category_Name + " - " + description);
        selection.setSerialNumber(serial_number);
        selection.setNote(note);
        data_Layer.updateSoftwareRecord(id, library_id, description, serial_number, note, category_Name);
    }
    public void updateSoftwareRecordNote(String id,String note)
    {
        
        
        if(note == null)
        {
            note = "";
        }
        int software_Index = list_Software_IDs.indexOf(id);
        Software selection = list_Software.get(software_Index);
        selection.setNote(note);
        data_Layer.updateSoftwareRecordNote(id, note);
    }

}
