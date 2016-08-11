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

import com.sun.javafx.application.LauncherImpl;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Preloader.ProgressNotification;
import javafx.application.Preloader.StateChangeNotification;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import spiceworks_archive_javafx.LabelNotification;
import spiceworks_archive_javafx.Spiceworks_Archive_Load;


/**
 *
 * @author it.student
 */
public class Spiceworks_Archive_Presentation extends Application
{

    private static Spiceworks_Archive_Logic logic_Layer;
    Spiceworks_Archive_Presentation_Tickets ticket_Layout;
    Spiceworks_Archive_Presentation_SoftwareLibrary library_Layout;

    @Override
    public void init()
    {
        try
        {
            //NOTIFIES THE PRELOADER APPLICATION THAT WORK IS BEING COMPLETED BEFORE THE APPLICATION LAUNCHES
            notifyPreloader(new ProgressNotification(0.0));
            notifyPreloader(new LabelNotification("...Loading..."));
            Thread.sleep(300);
            logic_Layer = new Spiceworks_Archive_Logic();
            logic_Layer.checkTables();
            
            notifyPreloader(new ProgressNotification(0.25));
            notifyPreloader(new LabelNotification("...Retrieving Tickets..."));
            Thread.sleep(300);
            ticket_Layout = new Spiceworks_Archive_Presentation_Tickets(logic_Layer);
            notifyPreloader(new ProgressNotification(0.5));
            
            notifyPreloader(new LabelNotification("...Retrieving Software Library..."));
            library_Layout = new Spiceworks_Archive_Presentation_SoftwareLibrary(logic_Layer);
            notifyPreloader(new ProgressNotification(0.75));
            
            notifyPreloader(new LabelNotification("...Creating Search Index..."));
            Thread.sleep(300);
            logic_Layer.createLuceneIndexes();
            
            notifyPreloader(new ProgressNotification(1));
            notifyPreloader(new LabelNotification("...Finished..."));
            Thread.sleep(300);

            notifyPreloader(new StateChangeNotification(
                    StateChangeNotification.Type.BEFORE_START));
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(Spiceworks_Archive_Presentation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void start(Stage mainStage)
    {

        TabPane primary_Tab_Pane = new TabPane();
        Tab ticket_Tab = new Tab("TICKET SEARCH");
        Tab knowledgebase_Tab = new Tab("KNOWLEDGEBASE SEARCH");
        Tab software_Library_Tab = new Tab("SOFTWARE LIBRARY");
        ticket_Tab.setClosable(false);
        ticket_Tab.setStyle("-fx-background-color: skyblue; -fx-color: deepskyblue; ");
        knowledgebase_Tab.setStyle("-fx-background-color: deepskyblue;");
        knowledgebase_Tab.setClosable(false);
        software_Library_Tab.setClosable(false);
        
        ticket_Layout.initializeTicketPane(mainStage);
        library_Layout.initializeSoftwareLibraryPane(mainStage);
        
        ticket_Tab.setContent(ticket_Layout.getTicketPane());
        software_Library_Tab.setContent(library_Layout.getSoftwareLibraryPane());
        //SETUP MAIN SCENE BORDERPANE:
        primary_Tab_Pane.getTabs().addAll(ticket_Tab,
                knowledgebase_Tab,
                software_Library_Tab);
        
        
       

        Scene scene = new Scene(primary_Tab_Pane, 1050, 500);

        
        Image icon64 = new Image("spiceworks_archive_javafx/Images/64x64.png");
        Image icon32 = new Image("spiceworks_archive_javafx/Images/32x32.png");
        Image icon16 = new Image("spiceworks_archive_javafx/Images/16x16.png");
        //SETUP COSMETIC SETTIGNS
        mainStage.setTitle("Jordans Spicework Ticket Search");
        mainStage.getIcons().addAll(icon64, icon32, icon16);
        mainStage.setScene(scene);
        mainStage.setMaximized(true);
        mainStage.show();

    }

    

    /**
     * @param args THERE ARE NO COMMANDLINE ARGUMENTS
     */
    public static void main(String[] args)
    {
        LauncherImpl.launchApplication(Spiceworks_Archive_Presentation.class, Spiceworks_Archive_Load.class, args);

    }

}
