/*
 * The MIT License
 *
 * Copyright 2016 Jordan Kahtava (it.student).
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
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
import spiceworks_archive_javafx.Ticket;


/**
 *
 * @author it.student
 */
public class Spiceworks_Archive_Logic
{

    private static Spiceworks_Archive_Data data_Layer;

    private static ObservableList<Ticket> ticket_Observable_List;
    private static ObservableList<String> ticket_IDs_Observable_List;
    public static final String INDEX_PATH = "H:\\Spiceworks_Archive_Database\\INDEX";
    public static final File INDEX_DIRECTORY = new File(INDEX_PATH);
    private static final int MAX_HITS = 100;

    public Spiceworks_Archive_Logic()
    {
        data_Layer = new Spiceworks_Archive_Data();
    }

    public ObservableList<Ticket> search(String terms)
    {

        ObservableList<Ticket> ticket_List = FXCollections.observableArrayList();

        return ticket_List;
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
        ticket_Observable_List = FXCollections.observableArrayList();
        ResultSet tickets = null;
        try
        {

            tickets = data_Layer.getTickets();
            if (tickets != null)
            {
                while (tickets.next())
                {

                    String id = tickets.getString("id");
                    String summary = tickets.getString("summary");
                    String description = tickets.getString("description");
                    String first_Name = tickets.getString("first_name");
                    String last_Name = tickets.getString("last_name");
                    String attachment_Name = tickets.getString("attachment_name");
                    ticket_Observable_List.add(new Ticket(id, summary, description, first_Name, last_Name, attachment_Name));
                    ticket_IDs_Observable_List.add(tickets.getString("id"));
                }

            }

        }
        catch (Spiceworks_Archive_Exception | SQLException ex)
        {
            Logger.getLogger(Spiceworks_Archive_Logic.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ticket_Observable_List;
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

    public int indexDocuments(IndexWriter index_Writer) throws Spiceworks_Archive_Exception, SQLException, IOException
    {
        ResultSet database_Results = data_Layer.getLuceneIndexResults();
        int document_Count = 0;
        while (database_Results.next())
        {
            Document index_Document = new Document();
            //"select id,summary,description,first_name,last_name,attachment_name from ticket_plus_attachments"
            FieldType type = new FieldType();
            type.setStored(true);
            type.setIndexOptions(IndexOptions.NONE);

            index_Document.add(new StoredField("id", database_Results.getInt("id")));
            String summary = database_Results.getString("summary");
            if (summary != null)
            {
                index_Document.add(new TextField("summary", summary, Field.Store.YES));
            }
            index_Document.add(new TextField("description", database_Results.getString("description"), Field.Store.YES));
            index_Document.add(new StringField("first_name", database_Results.getString("first_name"), Field.Store.YES));
            index_Document.add(new StringField("last_name", database_Results.getString("last_name"), Field.Store.YES));
            String attachment_Name = database_Results.getString("attachment_name");

            if (attachment_Name != null)
            {
                index_Document.add(new StringField("attachment_name", attachment_Name, Field.Store.YES));
            }

            index_Writer.addDocument(index_Document);
            document_Count++;

        }

        return document_Count;
    }

    public ObservableList<Ticket> searchIndex(String query_String)
    {
        ObservableList<Ticket> ticket_List = FXCollections.observableArrayList();

        try
        {
            Directory directory = FSDirectory.open(INDEX_DIRECTORY.toPath());
            MultiFieldQueryParser query_Parser = new MultiFieldQueryParser(new String[]
            {
                "summary", "description", "first_name", "last_name"
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
            System.out.println(hits.length + " Record(s) Found");

            for (int i = 0; i < hits.length; i++)
            {

                int docId = hits[i].doc;

                Document d = searcher.doc(docId);
                int index = ticket_IDs_Observable_List.indexOf(d.get("id"));
                ticket_List.add(ticket_Observable_List.get(index));
                System.out.println("\"ID:\" " + d.get("id") + ", \"Summary:\" " + d.get("summary") + ", \"Description:\" " + d.get("description"));

            }

            if (hits.length == 0)
            {

                System.out.println("No Data Founds ");

            }

        }
        catch (IOException ex)
        {
            
        }
        catch (ParseException ex)
        {
            
        }
        return ticket_List;
    }

}
