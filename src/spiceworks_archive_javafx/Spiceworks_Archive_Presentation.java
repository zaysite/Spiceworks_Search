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
package spiceworks_archive_javafx;

import com.sun.javafx.application.LauncherImpl;
import java.text.Collator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Preloader.ProgressNotification;
import javafx.application.Preloader.StateChangeNotification;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TableView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import spiceworks_archive_search.Spiceworks_Archive_Logic;

/**
 *
 * @author it.student
 */
public class Spiceworks_Archive_Presentation extends Application
{

    private static Spiceworks_Archive_Logic logic_Layer;
    private static ComboBox<String> technician_Combo;
    private static TextArea description_Area;
    private static TextField search_Query;
    private static Label submitted_Query;
    private static Tooltip submitted_Tooltip;
    private TableView ticket_View;
    private static CheckBox has_Attachment;
    private static ObservableList<Ticket> ticket_List;

    @Override
    public void init()
    {
        try
        {
            notifyPreloader(new LabelNotification("...Loading..."));
            notifyPreloader(new ProgressNotification(0.0));
            logic_Layer = new Spiceworks_Archive_Logic();
            notifyPreloader(new ProgressNotification(0.25));
            notifyPreloader(new LabelNotification("...Retrieving Tickets..."));
            ticket_List = logic_Layer.getTickets();
            notifyPreloader(new ProgressNotification(0.5));
            notifyPreloader(new LabelNotification("...Creating Search Index..."));
            //logic_Layer.createSearchTree(ticket_List);
            logic_Layer.createLuceneIndexes();
            notifyPreloader(new ProgressNotification(1));
            notifyPreloader(new LabelNotification("...Finished..."));
            //logic_Layer.writeTreeToFile();
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

        HBox toolbar = createToolbar();
        ticket_View = createTableView(mainStage);

        BorderPane left_Border_Pane = new BorderPane();

        BorderPane right_Border_Pane = new BorderPane();
        description_Area = new TextArea();
        description_Area.setEditable(false);
        right_Border_Pane.setCenter(description_Area);
        //border2.getChildren().addAll();

        SplitPane split_Pane = new SplitPane();
        split_Pane.getItems().addAll(ticket_View, description_Area);
        split_Pane.setDividerPositions(0.8);
        left_Border_Pane.setBottom(toolbar);

        left_Border_Pane.setCenter(split_Pane);
        //border.setRight(border2);

        Scene scene = new Scene(left_Border_Pane, 1050, 500);

        mainStage.setTitle("Spiceworks Advanced Search");
        Image icon64 = new Image("spiceworks_archive_javafx/Images/64x64.png");
        Image icon32 = new Image("spiceworks_archive_javafx/Images/32x32.png");
        Image icon16 = new Image("spiceworks_archive_javafx/Images/16x16.png");
        mainStage.getIcons().addAll(icon64, icon32, icon16);
        mainStage.setScene(scene);
        mainStage.setMaximized(true);
        mainStage.show();

    }

    /**
     *
     * @return HBox a HBox object with a textField, Button, CheckBox, and labels
     */
    private HBox createToolbar()
    {
        //Setup the default values of the HBox
        HBox hbox_Toolbar = new HBox();
        hbox_Toolbar.setPadding(new Insets(15, 12, 15, 12));
        hbox_Toolbar.setSpacing(10);
        hbox_Toolbar.setAlignment(Pos.CENTER);

        Tooltip tooltip = new Tooltip("Commands: \n Quotations return only tickets matching the exact phrase ex.\"Jordan Kahtava\" \n "
                + "The AND keyword can be used to return tickets that have both words or phrases ex. \"Jordan Kahtava\" AND APPENTRY \n"
                + "The OR keyword is used by default for all searches ex. Search OR Term is the same as Search Term \n"
                + "The + can be added to a term to only return results containing that word ex. +Jordan \n"
                + "The - can be added to return tickets that do not contain that word ex. +Jordan -Kahtava");
        //hbox_Toolbar.setStyle("-fx-background-color: #336699;");

        //hbox_Toolbar.setStyle("-fx-background-image: url(\"spiceworks_archive_javafx/Images/coffee-777612_1280.jpg\");");
        //Create Textfield and set default values
        search_Query = new TextField();
        search_Query.setText("Search Terms");

        search_Query.setTooltip(tooltip);
        search_Query.setOnKeyPressed(new EventHandler<KeyEvent>()
        {

            @Override
            public void handle(KeyEvent keyEvent)
            {
                if (keyEvent.getCode().equals(KeyCode.ENTER))
                {
                    ticket_View.setItems(logic_Layer.search(search_Query.getText(), technician_Combo.getSelectionModel().getSelectedItem(), submitted_Tooltip));
                    ticket_View.getSelectionModel().clearSelection();

                }

            }

        });
        submitted_Tooltip = new Tooltip();
        submitted_Query = new Label("Hover to Show Query");
        submitted_Query.setTooltip(submitted_Tooltip);
        //Create CheckBox
        has_Attachment = new CheckBox();
        has_Attachment.setOnAction((ActionEvent event)
                -> 
                {
                    logic_Layer.toggleAttachmentQuery();
        });
        //Create Button and set the event that runs when clicked.
        Button search_Button = new Button();
        search_Button.setText("Search");
        search_Button.setOnAction((ActionEvent event)
                -> 
                {
                    //TO DO: ADD Search Options
                    ticket_View.setItems(logic_Layer.search(search_Query.getText(), technician_Combo.getSelectionModel().getSelectedItem(), submitted_Tooltip));
                    ticket_View.getSelectionModel().clearSelection();
        });

        //Create ComboBox and fill with technician names.
        technician_Combo = new ComboBox<>();
        //Initialize the list that will be used by the ComboBox
        ObservableList<String> technician_observable_List = logic_Layer.getTechnicians();
        //sort combobox list
        technician_Combo.setItems(new SortedList<>(technician_observable_List, Collator.getInstance()));
        //select first element to prevent null
        technician_Combo.getSelectionModel().selectFirst();

        //Create the Labels for the above Controls
        Label search_Label = new Label("Search Database: ");
        Label technician_Label = new Label("Technician: ");
        Label attachment_Label = new Label("Has Attachment: ");

        //Add the controls and labels to the HBox
        hbox_Toolbar.getChildren().addAll(search_Label, search_Query, technician_Label, technician_Combo, search_Button, attachment_Label, has_Attachment, submitted_Query);

        return hbox_Toolbar;
    }

    private TableView createTableView(Stage mainStage)
    {
        TableView<Ticket> table_View = new TableView<>();
        table_View.setEditable(false);
        table_View.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent mouseEvent)
            {
                if (ticket_View.getSelectionModel().isEmpty() == false)
                {
                    Ticket current = (Ticket) ticket_View.getItems().get(ticket_View.getSelectionModel().getSelectedIndex());
                    description_Area.setText(current.getDescription());
                }
            }

        });
        table_View.setOnKeyPressed(new EventHandler<KeyEvent>()
        {

            @Override
            public void handle(KeyEvent keyEvent)
            {
                if (keyEvent.getCode().equals(KeyCode.UP))
                {
                    if (ticket_View.getSelectionModel().getSelectedIndex() > 0)
                    {
                        Ticket current = (Ticket) ticket_View.getItems().get(ticket_View.getSelectionModel().getSelectedIndex() - 1);
                        description_Area.setText(current.getDescription());
                    }

                }
                else if (keyEvent.getCode().equals(KeyCode.DOWN))
                {
                    if (ticket_View.getSelectionModel().getSelectedIndex() < ticket_View.getItems().size() - 1)
                    {
                        Ticket current = (Ticket) ticket_View.getItems().get(ticket_View.getSelectionModel().getSelectedIndex() + 1);
                        description_Area.setText(current.getDescription());
                    }

                }
            }

        });
        // table_View.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // define the table columns.
        TableColumn<Ticket, String> ticket_ID = new TableColumn<>("ID");
        ticket_ID.setCellValueFactory(new PropertyValueFactory("id"));

        TableColumn<Ticket, String> ticket_Summary = new TableColumn<>("Summary");
        ticket_Summary.setCellValueFactory(new PropertyValueFactory("summary"));

        TableColumn<Ticket, String> ticket_Attachment = new TableColumn<>("Attachment");
        ticket_Attachment.setCellValueFactory(new PropertyValueFactory("attachment_Name"));

        TableColumn<Ticket, String> ticket_First_Name = new TableColumn<>("First Name");
        ticket_First_Name.setCellValueFactory(new PropertyValueFactory("first_Name"));

        TableColumn<Ticket, String> ticket_Last_Name = new TableColumn<>("Last Name");
        ticket_Last_Name.setCellValueFactory(new PropertyValueFactory("last_Name"));

        TableColumn<Ticket, Boolean> actionCol = new TableColumn<>("Go To Ticket");
        actionCol.setSortable(false);

        //Set Column preferred sizes.
        ticket_ID.setPrefWidth(50);
        ticket_Summary.setPrefWidth(600);
        ticket_First_Name.setPrefWidth(120);
        ticket_Last_Name.setPrefWidth(120);
        actionCol.setPrefWidth(125);

        // define a simple boolean cell value for the action column so that the column will only be shown for non-empty rows.
        actionCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Ticket, Boolean>, ObservableValue<Boolean>>()
        {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Ticket, Boolean> features)
            {
                return new SimpleBooleanProperty(features.getValue() != null);
            }

        });

        // create a cell value factory with an add button for each row in the table.
        actionCol.setCellFactory(new Callback<TableColumn<Ticket, Boolean>, TableCell<Ticket, Boolean>>()
        {
            @Override
            public TableCell<Ticket, Boolean> call(TableColumn<Ticket, Boolean> ticketBooleanTableColumn)
            {
                return new TicketButtonCell(mainStage, table_View);
            }

        });

        table_View.getColumns().addAll(ticket_ID, ticket_Summary, ticket_First_Name, ticket_Last_Name, actionCol, ticket_Attachment);

        table_View.setItems(ticket_List);

        return table_View;

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        LauncherImpl.launchApplication(Spiceworks_Archive_Presentation.class, Spiceworks_Archive_Load.class, args);

    }

}
