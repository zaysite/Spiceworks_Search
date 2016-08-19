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

import java.text.Collator;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import spiceworks_archive_javafx.Ticket;
import spiceworks_archive_javafx.TicketButtonCell;

/**
 *
 * @author it.student
 */
public class Spiceworks_Archive_Presentation_Tickets
{
    private static ComboBox<String> technician_Combo;
    private static TextArea description_Area;
    private static TextField search_Query;
    private static TextField search_Commands;
    private static TableView<Ticket> ticket_View;
    private static CheckBox has_Attachment;
    private static ObservableList<Ticket> ticket_List;
    private final Spiceworks_Archive_Logic logic_Layer;
    
    BorderPane primary_Border_Pane;
    
    public Spiceworks_Archive_Presentation_Tickets(Spiceworks_Archive_Logic logic_Layer)
    {
        
        this.logic_Layer = logic_Layer;
        ticket_List = logic_Layer.getTickets();
        
        
        
        
    }
    public void initializeTicketPane(Stage primary_Stage)
    {
        //CREATE BORDERPANES:
        primary_Border_Pane = new BorderPane();
        BorderPane right_Border_Pane = new BorderPane();
        
        description_Area = new TextArea();
        description_Area.setEditable(false);
        right_Border_Pane.setCenter(description_Area);
       
        SplitPane split_Pane = new SplitPane();
        ticket_View = createTableView(primary_Stage);
        split_Pane.getItems().addAll(ticket_View, description_Area);
        split_Pane.setDividerPositions(0.8);
        
        VBox toolbar = createToolbar();
        primary_Border_Pane.setBottom(toolbar);
        primary_Border_Pane.setCenter(split_Pane);
    }
    public Node getTicketPane()
    {
        return primary_Border_Pane;
    }
    /**
     * Creates a toolbar used for searching ticket information.
     * @return VBox creates two HBox containers that are encapsulated by a VBox
     * The first HBox consists of labels, technician combobox, search textfield, search button, and attachment checkbox.
     * The second HBox contains a single textfield that displays querys and search results.
     */
    private VBox createToolbar()
    {
        //MAIN TOOLBAR:
        VBox vbox_Toolbar_Parent = new VBox();
        
        //CREATE BOTH HBOX CONTAINERS:
        //TOP TOOLBAR/HBOX
        HBox hbox_Toolbar_Top = new HBox();
        hbox_Toolbar_Top.setPadding(new Insets(15, 12, 15, 12));
        hbox_Toolbar_Top.setSpacing(10);
        hbox_Toolbar_Top.setAlignment(Pos.CENTER);
        //BOTTOM TOOLBAR/HBOX
        HBox hbox_Toolbar_Bottom = new HBox();
        hbox_Toolbar_Bottom.setPadding(new Insets(15, 12, 15, 12));
        hbox_Toolbar_Bottom.setSpacing(10);
        hbox_Toolbar_Bottom.setAlignment(Pos.CENTER);
        
        //SEARCH_QUERY TOOLTIP EXPLAINING BASIC SEARCH COMMANDS
        Tooltip search_Query_Tooltip = new Tooltip("Commands: \n Quotations return only tickets matching the exact phrase ex.\"Jordan Kahtava\" \n "
                + "The AND keyword can be used to return tickets that have both words or phrases ex. \"Jordan Kahtava\" AND APPENTRY \n"
                + "The OR keyword is used by default for all searches ex. Search OR Term is the same as Search Term \n"
                + "The + can be added to a term to only return results containing that word ex. +Jordan \n"
                + "The - can be added to return tickets that do not contain that word ex. +Jordan -Kahtava");

        //CREATE TEXTFIELDS:
        search_Commands = new TextField();  //SEARCH COMMANDS DISPLAYS RECORDS FOUND AND QUERY SENT
        search_Commands.setEditable(false);
        search_Commands.setAlignment(Pos.CENTER);
        search_Commands.setDisable(true);
        search_Commands.setStyle("-fx-opacity: 1;"); //MAKE DISABLE TEXTFIELD EASIER TO READ.
        logic_Layer.setMessageBox(search_Commands);
        
        search_Query = new TextField(); //ENTER SEARCH TERMS OR PHRASES
        search_Query.setText("Search Terms");
        search_Query.setTooltip(search_Query_Tooltip);
        search_Query.setOnKeyPressed(new EventHandler<KeyEvent>()
        {

            @Override
            public void handle(KeyEvent keyEvent)
            {
                //When a user hits enter send the query/text for search consistency/user expectations
                if (keyEvent.getCode().equals(KeyCode.ENTER))
                {

                    ticket_View.<Ticket>setItems(logic_Layer.searchTicket(search_Query.getText(), technician_Combo.getSelectionModel().getSelectedItem()));
                    ticket_View.getSelectionModel().clearSelection();

                }

            }

        });
        
        //MAKE TEXTFIELDS GROW BASED ON WINDOW SIZE:
        HBox.setHgrow(search_Commands, Priority.ALWAYS);
        HBox.setHgrow(search_Query, Priority.ALWAYS);
       
        
        //CHECKBOX
        has_Attachment = new CheckBox();
        has_Attachment.setOnAction((ActionEvent event)
                -> 
                {
                    logic_Layer.toggleAttachmentQuery();
        });
        
        //SEARCH BUTTON USED TO RETURN SEARCH RESULTS
        Button search_Button = new Button();
        search_Button.setText("Search");
        search_Button.setOnAction((ActionEvent event)
                -> 
                {
                    
                    ticket_View.setItems(logic_Layer.searchTicket(search_Query.getText(), technician_Combo.getSelectionModel().getSelectedItem()));
                    ticket_View.getSelectionModel().clearSelection();   //CLEAR SELECTION TO PREVENT NULL ROWS
        });

        //CREATE TECHNICIAN COMBOBOX
        technician_Combo = new ComboBox<>();
        ObservableList<String> technician_observable_List = logic_Layer.getTechnicians();
        technician_Combo.setItems(new SortedList<>(technician_observable_List, Collator.getInstance())); //SORT LIST BEFORE ADDING TO COMBOBOX
        technician_Combo.getSelectionModel().selectFirst(); //PREVENTS NULL SELECTIONS

        //CREATE TOOLBAR LABELS
        Label search_Label = new Label("Search Database: ");
        Label technician_Label = new Label("Technician: ");
        Label attachment_Label = new Label("Has Attachment: ");

        //ADD CONTROLS TO HBOX THEN TO VBOX:
        hbox_Toolbar_Top.getChildren().addAll(search_Label, search_Query, technician_Label, technician_Combo, search_Button, attachment_Label, has_Attachment);
        hbox_Toolbar_Bottom.getChildren().addAll(search_Commands);
        //ADD BOTH HBOX TO VBOX
        vbox_Toolbar_Parent.getChildren().addAll(hbox_Toolbar_Top,hbox_Toolbar_Bottom);
        
        
        return vbox_Toolbar_Parent;
    }

    /**
     * This method creates that TableView that is used in displaying search results.
     * @param primary_Stage The parent stage that will display all the information
     * @return TableView<Ticket> A tableview consisting of Ticket Objects.
     */
    private TableView<Ticket> createTableView(Stage primary_Stage)
    {
        TableView<Ticket> table_View = new TableView<>();
        table_View.setEditable(false);
        //BOTH EVENTS GET THE TICKET THAT MATCHES THE ROW AND DISPLAYS ITS DESCRIPTION IN THE RIGHT PANE
        //SETUP MOUSE EVENTS: MOUSE CLICK
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
        //SETUP KEY EVENTS: UP AND DOWN ARROWS 
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


        //CREATE TABLEVIEW COLUMNS
        TableColumn<Ticket, String> ticket_ID = new TableColumn<>("ID");
        ticket_ID.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Ticket, String> ticket_Summary = new TableColumn<>("Summary");
        ticket_Summary.setCellValueFactory(new PropertyValueFactory<>("summary"));

        TableColumn<Ticket, String> ticket_Attachment = new TableColumn<>("Attachment");
        ticket_Attachment.setCellValueFactory(new PropertyValueFactory<>("attachment_Name"));

        TableColumn<Ticket, String> ticket_Technician_Name = new TableColumn<>("Technician Name");
        ticket_Technician_Name.setCellValueFactory(new PropertyValueFactory<>("technician_Name"));

        TableColumn<Ticket, Boolean> actionCol = new TableColumn<>("Go To Ticket");
        actionCol.setSortable(false);

        //SET COLUMN SIZE/WIDTH.
        ticket_ID.setPrefWidth(50);
        ticket_Summary.setPrefWidth(600);
        ticket_Technician_Name.setPrefWidth(150);
        actionCol.setPrefWidth(125);

        //DEFINE A BOOLEAN CELL VALUE SO THAT THE BUTTON COLUMN IS ONLY SHOWN IF THE ROW IS NOT EMPTY
        actionCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Ticket, Boolean>, ObservableValue<Boolean>>()
        {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Ticket, Boolean> features)
            {
                return new SimpleBooleanProperty(features.getValue() != null);
            }

        });

        //CREATE THE LINK BUTTON FOR EACH ROW IN THE TABLE.
        actionCol.setCellFactory(new Callback<TableColumn<Ticket, Boolean>, TableCell<Ticket, Boolean>>()
        {
            @Override
            public TableCell<Ticket, Boolean> call(TableColumn<Ticket, Boolean> ticketBooleanTableColumn)
            {
                return new TicketButtonCell(primary_Stage, table_View);
            }

        });
        
        //OBSERVABLE LIST IS CREATED MANUALLY TO ENSURE TYPE SAFETY WITH GENERICS.
        ObservableList<TableColumn<Ticket, ?>> list_Table_Columns = FXCollections.observableArrayList();
        list_Table_Columns.add(ticket_ID);
        list_Table_Columns.add(ticket_Summary);
        list_Table_Columns.add(ticket_Technician_Name);
        list_Table_Columns.add(actionCol);
        list_Table_Columns.add(ticket_Attachment);
        
        //SET THE COLUMNS
        table_View.getColumns().addAll(list_Table_Columns);
        //SET THE DATA THAT CORRESPONDS WITH COLUMNS
        table_View.setItems(ticket_List);

        return table_View;

    }
}
