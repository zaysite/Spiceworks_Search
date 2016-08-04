/*
 * The MIT License
 *
 * Copyright 2016 it.student.
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
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
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

/**
 * https://www.toptal.com/database/full-text-search-of-dialogues-with-apache-lucene
 * https://dzone.com/articles/lucene-database-oracle-five
 *
 * @author it.student
 */
public class Spiceworks_Archive_Lucene
{

    public static final String INDEX_PATH = "H:\\Spiceworks_Archive_Database\\INDEX";
    public static final File INDEX_DIRECTORY = new File(INDEX_PATH);
    private static final int MAX_HITS = 100;

    public Spiceworks_Archive_Lucene(Spiceworks_Archive_Logic logic_Layer)
    {
        try
        {
            SimpleAnalyzer analyzer = new SimpleAnalyzer();

            IndexWriterConfig index_Writer_Configuration = new IndexWriterConfig(analyzer);
            int index_Document_Count = 0;
            if (INDEX_DIRECTORY.listFiles().length <= 0)
            {
                try (IndexWriter index_Writer = new IndexWriter(FSDirectory.open(INDEX_DIRECTORY.toPath()), index_Writer_Configuration))
                {
                    System.out.println("Indexing DB to Directory: " + INDEX_DIRECTORY + " ...");
                    index_Document_Count = logic_Layer.indexDocuments(index_Writer);
                }
                catch (Spiceworks_Archive_Exception | SQLException ex)
                {
                    Logger.getLogger(Spiceworks_Archive_Lucene.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("Total Records Indexed: " + index_Document_Count);
            }

        }
        catch (IOException ex)
        {
            Logger.getLogger(Spiceworks_Archive_Lucene.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void searchIndex(String query_String)
    {

        try
        {
            Directory directory = FSDirectory.open(INDEX_DIRECTORY.toPath());
            MultiFieldQueryParser query_Parser = new MultiFieldQueryParser(new String[]
            {
                "summary", "description"
            }, new StandardAnalyzer());
            DirectoryReader reader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(reader);

            query_Parser.setPhraseSlop(0);

            query_Parser.setLowercaseExpandedTerms(true);

            Query query = query_Parser.parse(query_String);

            //TopDocs topDocs = searcher.search(query, MAX_HITS);
            TopScoreDocCollector collector = TopScoreDocCollector.create(10);
            searcher.search(query, new PositiveScoresOnlyCollector(collector));
            TopDocs topDocs = collector.topDocs();

            ScoreDoc[] hits = topDocs.scoreDocs;
            System.out.println(hits.length + " Record(s) Found");

            for (int i = 0; i < hits.length; i++)
            {

                int docId = hits[i].doc;

                Document d = searcher.doc(docId);

                System.out.println("\"ID:\" " + d.get("id") + ", \"Summary:\" " + d.get("summary") + ", \"Description:\" " + d.get("description"));

            }

            if (hits.length == 0)
            {

                System.out.println("No Data Founds ");

            }

        }
        catch (IOException ex)
        {
            Logger.getLogger(Spiceworks_Archive_Lucene.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (ParseException ex)
        {
            Logger.getLogger(Spiceworks_Archive_Lucene.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
