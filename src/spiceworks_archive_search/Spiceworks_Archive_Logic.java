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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    private static ObservableList<Ticket> tickets;
    private static ObservableList<Ticket> tickets_With_Attachments;
    private static ObservableList<String> ticket_IDs;

    private static ObservableList<Software> software;

    public static final String INDEX_PATH = "C:\\Spiceworks_Archive\\INDEX";
    public static final File INDEX_DIRECTORY = new File(INDEX_PATH);
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

    public ObservableList<Ticket> search(String terms, String technician)
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
            return tickets;
        }
        else if (terms.trim().length() == 0 && has_Attachment == true)
        {
            message_Box.setText("");
            return tickets_With_Attachments;
        }

        message_Box.setText("Search: " + query);
        return searchIndex(query);
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

    public ObservableList<Ticket> getTickets()
    {
        tickets = FXCollections.observableArrayList();
        ticket_IDs = FXCollections.observableArrayList();
        tickets_With_Attachments = FXCollections.observableArrayList();
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

                    int duplicate_Ticket = ticket_IDs.indexOf(id);
                    if (duplicate_Ticket == -1)
                    {
                        Ticket current_Ticket = new Ticket(id, summary, description, first_Name, last_Name, attachment_Name);
                        tickets.add(current_Ticket);
                        if (attachment_Name != null)
                        {
                            tickets_With_Attachments.add(current_Ticket);
                        }
                        ticket_IDs.add(id);
                    }
                    else
                    {
                        tickets.get(duplicate_Ticket).addAttachment_Name(attachment_Name);

                    }

                }

            }

        }
        catch (Spiceworks_Archive_Exception | SQLException ex)
        {
            Logger.getLogger(Spiceworks_Archive_Logic.class.getName()).log(Level.SEVERE, null, ex);
        }

        return tickets;
    }

    public ObservableList<Software> getSoftware()
    {
        software = FXCollections.observableArrayList();
        ResultSet database_Results;
        try
        {

            database_Results = data_Layer.getSoftware();
            if (database_Results != null)
            {
                while (database_Results.next())
                {
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

                    Software current_Software = new Software(category, serial_Number, library_ID, created_on, note, status,checked_out_by,checked_out_at,last_checked_out_by,last_checked_out_at);
                    software.add(current_Software);

                }

            }

        }
        catch (Spiceworks_Archive_Exception | SQLException ex)
        {
            Logger.getLogger(Spiceworks_Archive_Logic.class.getName()).log(Level.SEVERE, null, ex);
        }
        return software;
    }

    public boolean createLuceneIndexes()
    {
        if (INDEX_DIRECTORY.listFiles().length <= 0)
        {
            try
            {
                SimpleAnalyzer analyzer = new SimpleAnalyzer();

                IndexWriterConfig index_Writer_Configuration = new IndexWriterConfig(analyzer);
                int index_Document_Count = 0;

                try (IndexWriter index_Writer = new IndexWriter(FSDirectory.open(INDEX_DIRECTORY.toPath()), index_Writer_Configuration))
                {
                    System.out.println("Indexing DB to Directory: " + INDEX_DIRECTORY + " ...");
                    index_Document_Count = indexDocuments(index_Writer);
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

    private int indexDocuments(IndexWriter index_Writer) throws Spiceworks_Archive_Exception, SQLException, IOException
    {
        ResultSet database_Results = data_Layer.getLuceneIndexResults();
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

    private ObservableList<Ticket> searchIndex(String query_String)
    {
        ObservableList<Ticket> ticket_List = FXCollections.observableArrayList();

        try
        {
            Directory directory = FSDirectory.open(INDEX_DIRECTORY.toPath());
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
                int index = ticket_IDs.indexOf(d.get("id"));
                Ticket current = tickets.get(index);
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

}
