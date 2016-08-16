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


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author it.student
 */
public class Spiceworks_Archive_Data
{

    private static final String TICKET_QUERY = "select id,summary,description,first_name,last_name,attachment_name from ticket_view;";
    private static final String SOFTWARE_QUERY = "select id, category,serial_number,library_id,note,status,checked_out_by,checked_out_at,last_checked_out_by,last_checked_out_at,created_on,library_category from software_categories;";
    private static final String SOFTWARE_CATEGORY_QUERY = "select distinct library_category from categories;";
    private static final String LUCENE_INDEX_QUERY = "select id,summary,description,first_name,last_name,attachment_name from ticket_view;";
    private static final String TECHNICIAN_QUERY = "select first_name,last_name from admin_view;";
    
    private static final String CREATE_ADMIN_VIEW = "CREATE VIEW admin_view AS Select id,first_name,last_name from users where role='admin';";
    private static final String CREATE_TICKET_VIEW = "CREATE VIEW \"ticket_view\" AS select tickets.id,tickets.summary,tickets.description, comments.attachment_name,admin_view.first_name, admin_view.last_name from tickets left join comments on tickets.id=comments.ticket_id left join admin_view on tickets.assigned_to=admin_view.id;";
    private static final String CREATE_KNOWLEDGEBASE_VIEW = "CREATE VIEW 'knowledgebase_view' AS select title, start_content, end_content, steps, 'references', created_at from knowledge_base_articles;";
    
    private static final String VIEW_EXISTS_TICKET_VIEW = "SELECT name FROM sqlite_master where type='view' AND name='ticket_view'";
    private static final String VIEW_EXISTS_KNOWLEDGEBASE_VIEW = "SELECT name FROM sqlite_master where type='view' AND name='ticket_view'";
    private static final String VIEW_EXISTS_ADMIN_VIEW = "SELECT name FROM sqlite_master where type='view' AND name='admin_view'";
    
    
    private static final String SPICEWORKS_DATABASE_URL = "jdbc:SQLite:C:\\Spiceworks_Archive\\DATABASE\\spiceworks_prod.db";
    private static final String SOFTWARE_DATABASE_URL = "jdbc:SQLite:C:\\Spiceworks_Archive\\DATABASE\\Software_Library.db";
    private static Connection spiceworks_Database_Connection;
    private static Connection software_Database_Connection;
    private static Properties spiceworks_Connection_Properties;
    private static Properties software_Connection_Properties;
    public Spiceworks_Archive_Data()
    {
        CreateDatabaseConnection();
    }
    
    

    private void CreateDatabaseConnection()
    {
        try
        {
            //spiceworks_prod.db does not require any permissions so connection_Properties is empty.
            spiceworks_Connection_Properties = new Properties();
            //create a connection to the sqlite database.
            spiceworks_Database_Connection = DriverManager.getConnection(SPICEWORKS_DATABASE_URL, spiceworks_Connection_Properties);

        }
        catch (SQLException ex)
        {
            System.err.println("COULD NOT CONNECT TO SPICEWORKS DATABASE");
        }
        
        try
        {
            //spiceworks_prod.db does not require any permissions so connection_Properties is empty.
            software_Connection_Properties = new Properties();
            //create a connection to the sqlite database.
            software_Database_Connection = DriverManager.getConnection(SOFTWARE_DATABASE_URL, software_Connection_Properties);

        }
        catch (SQLException ex)
        {
            System.err.println("COULD NOT CONNECT TO SOFTWARE DATABASE");
        }
    }
    
    public void CheckViewsExist()
    {
        if(CheckView(VIEW_EXISTS_ADMIN_VIEW) == false)
        {
            CreateViewQuery(CREATE_ADMIN_VIEW);
        }
        if(CheckView(VIEW_EXISTS_TICKET_VIEW) == false)
        {
            CreateViewQuery(CREATE_TICKET_VIEW);
        }
        
        if(CheckView(VIEW_EXISTS_KNOWLEDGEBASE_VIEW) == false)
        {
            CreateViewQuery(CREATE_KNOWLEDGEBASE_VIEW);
        }
    }
    private boolean CheckView(String query)
    {
        boolean ticket_View_Exists = false;
        int result_Counter = 0;
        
        try
        {
            ResultSet results = QuerySpiceworksDatabase(query);  
            while(results.next())
            {
                
                result_Counter++;
            }
            
            if(result_Counter == 0)
            {
                ticket_View_Exists = false;
            }
            else
            {
                ticket_View_Exists = true;
            }
        }
        catch (SQLException ex)
        {
            
        }
        return ticket_View_Exists;
    }
    
    private void CreateViewQuery(String create_Query)
    {
        try
        {
            Statement database_Query;
            database_Query = spiceworks_Database_Connection.createStatement();
            
            int result = database_Query.executeUpdate(create_Query);
        }
        catch (SQLException ex)
        {
            System.err.println(ex.getMessage());
        }
    }
    
    private ResultSet QuerySpiceworksDatabase(String select_Query)
    {
        System.out.println(select_Query);
        ResultSet statement_Results = null;
        try
        {

            Statement database_Query;

            database_Query = spiceworks_Database_Connection.createStatement();

            statement_Results = database_Query.executeQuery(select_Query);

        }
        catch (SQLException ex)
        {
            System.err.println(ex.getMessage());
        }
        return statement_Results;
    }
    
    private ResultSet QuerySoftwareDatabase(String select_Query)
    {
        System.out.println(select_Query);
        ResultSet statement_Results = null;
        try
        {

            Statement database_Query;

            database_Query = software_Database_Connection.createStatement();

            statement_Results = database_Query.executeQuery(select_Query);

        }
        catch (SQLException ex)
        {
            System.err.println(ex.getMessage());
        }
        return statement_Results;
    }

    public ResultSet getSoftwareCategories() throws Spiceworks_Archive_Exception
    {
        ResultSet database_Response = QuerySoftwareDatabase(SOFTWARE_CATEGORY_QUERY);
        
        if (database_Response == null)
        {
            throw new Spiceworks_Archive_Exception("ERROR CATEGORIES COULD NOT BE RETURNED!");
        }
        
        return database_Response;
    }
    
    /**
     *
     * @return ResultSet Technicians
     * @throws Spiceworks_Archive_Exception
     */
    public ResultSet getTechnicians() throws Spiceworks_Archive_Exception
    {
        ResultSet database_Response = QuerySpiceworksDatabase(TECHNICIAN_QUERY);

        if (database_Response == null)
        {
            throw new Spiceworks_Archive_Exception("ERROR TECHNICIANS COULD NOT BE RETURNED!");
        }

        return database_Response;
    }

    public ResultSet getTickets() throws Spiceworks_Archive_Exception
    {
        ResultSet database_Response = QuerySpiceworksDatabase(TICKET_QUERY);
        if (database_Response == null)
        {
            throw new Spiceworks_Archive_Exception("ERROR TICKETS COULD NOT BE RETURNED!");
        }
        return database_Response;
    }
    public ResultSet getSoftware() throws Spiceworks_Archive_Exception
    {
        ResultSet database_Response = QuerySoftwareDatabase(SOFTWARE_QUERY);
        if (database_Response == null)
        {
            throw new Spiceworks_Archive_Exception("ERROR SOFTWARE COULD NOT BE RETURNED!");
        }
        return database_Response;
    }

    /*
    public ResultSet getTickets(String[] terms, String first_name, String last_name) throws Spiceworks_Archive_Exception
    {
        String where = "WHERE ID=" + terms[0];
        for (int i = 1; i < terms.length; i++)
        {
            where += " OR ID=" + terms[i];
        }
        System.out.println(where);
        ResultSet database_Response = QueryDatabase("select id,summary,description,first_name,last_name,attachment_name from ticket_plus_attachments where first_name=\"" + first_name + "\" AND last_name=\"" + last_name + "\"");
        if (database_Response == null)
        {
            throw new Spiceworks_Archive_Exception("ERROR TICKETS COULD NOT BE RETURNED BASED ON TECHNICIAN NAME!");
        }
        return database_Response;
    }*/

   
    
    public ResultSet getLuceneIndexResults() throws Spiceworks_Archive_Exception
    {
        
        ResultSet database_Response = QuerySpiceworksDatabase(LUCENE_INDEX_QUERY);

        if (database_Response == null)
        {
            throw new Spiceworks_Archive_Exception("INDEX REQUEST COULD NOT BE RETURNED!");
        }
        return database_Response;
    }
    
    

}
