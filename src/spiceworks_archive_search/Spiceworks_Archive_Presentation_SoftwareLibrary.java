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
import java.util.Comparator;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
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
import spiceworks_archive_javafx.Software;
import spiceworks_archive_javafx.Ticket;
import spiceworks_archive_javafx.TicketButtonCell;

/**
 *
 * @author it.student
 */
public class Spiceworks_Archive_Presentation_SoftwareLibrary
{

    private final Comparator<Software> DEFAULT_SOFTWARE_Comparator = (o1, o2) -> o1.getLibrary_ID().compareTo(o2.getLibrary_ID());
    // private final ObjectProperty<Comparator<? super Software>> DEFAULT_SOFTWARE_COMPARATOR_WRAPPER = new SimpleObjectProperty<>(DEFAULT_SOFTWARE_COMPARATOR);

    private BorderPane primary_Border_Pane;
    private final Spiceworks_Archive_Logic logic_Layer;
    private static ObservableList<Software> list_Software;
    private static TableView<Software> table_Software_View;
    private static TextArea textarea_Note_Area;

    private static HBox hbox_Record_Create_Toolbar;
    private static HBox hbox_Record_Edit_Toolbar;
    private static VBox vbox_Record_Search_Toolbar;

    private static Button button_Edit_Note;
    private static Button button_Save_Note;
    private static Button button_Edit_Record;
    private static Button button_Create_Record;
    
    

    
    private static boolean boolean_Editing_Note = false;
    private static boolean boolean_Editing_Record = false;
    private static boolean boolean_Creating_Record = false;

    private static Label label_Edit_library_ID;
    private static Label label_Edit_Category;
    private static Label label_Edit_Serial_Number;

    private static TextField text_Edit_library_ID;
    private static TextField text_Edit_Serial_Number;
    private static TextField text_Edit_Description;
    
    private static TextField text_Search_Commands;
    private static TextField text_Search_Query;
    
    private static ComboBox<String> combo_Edit_Category;
    private static ComboBox<String> combo_Search_Category;

    public Spiceworks_Archive_Presentation_SoftwareLibrary(Spiceworks_Archive_Logic logic_Layer)
    {
        this.logic_Layer = logic_Layer;
        list_Software = logic_Layer.getSoftware();

    }

    public void initializeSoftwareLibraryPane(Stage primary_Stage)
    {
        VBox button_Toolbar = new VBox();
        
        primary_Border_Pane = new BorderPane();
        BorderPane right_Border_Pane = new BorderPane();

        HBox note_Toolbar = new HBox();
        note_Toolbar.setPadding(new Insets(15, 12, 15, 12));
        note_Toolbar.setSpacing(10);
        button_Edit_Note = new Button("Edit Note");
        button_Save_Note = new Button("Save Note");
        button_Save_Note.setVisible(false);
        
        button_Edit_Record = new Button("Edit Record");
        button_Create_Record = new Button("Create Record");
        

        button_Edit_Note.setOnAction((ActionEvent event)
                -> 
                {
                    editNote();
        });
        button_Edit_Record.setOnAction((ActionEvent event)
                -> 
                {
                    editRecord();
                    setEditRecordFields();
        });
        button_Create_Record.setOnAction((ActionEvent event)
                -> 
                {
                    createRecord();
        });
        
        
        button_Toolbar.getChildren().addAll(button_Create_Record, button_Edit_Record, button_Edit_Note,button_Save_Note);
        button_Toolbar.setAlignment(Pos.CENTER);
        button_Toolbar.setPadding(new Insets(15, 12, 15, 12));
        button_Toolbar.setSpacing(10);
        
        //note_Toolbar.getChildren().addAll(edit_Note, edit_Record, create_Record);

        //note_Toolbar.setAlignment(Pos.CENTER);

        textarea_Note_Area = new TextArea();
        textarea_Note_Area.setEditable(false);
        right_Border_Pane.setCenter(textarea_Note_Area);
        //right_Border_Pane.setLeft(button_Toolbar);

        VBox primary_Toolbar = new VBox();
        primary_Toolbar.setPadding(new Insets(15, 12, 15, 12));
        primary_Toolbar.setSpacing(10);
        SplitPane split_Pane = new SplitPane();
        table_Software_View = createTableView(primary_Stage);
        table_Software_View.getSelectionModel().selectFirst();
        split_Pane.getItems().addAll(table_Software_View, right_Border_Pane);
        split_Pane.setDividerPositions(0.8);

        hbox_Record_Create_Toolbar = createRecordToolbar();
        hbox_Record_Create_Toolbar.setManaged(false);
        hbox_Record_Create_Toolbar.setVisible(false);

        hbox_Record_Edit_Toolbar = createEditRecordToolbar();
        hbox_Record_Edit_Toolbar.setManaged(false);
        hbox_Record_Edit_Toolbar.setVisible(false);
        
        vbox_Record_Search_Toolbar = createSearchToolbar();
                
        primary_Toolbar.getChildren().addAll(hbox_Record_Create_Toolbar, hbox_Record_Edit_Toolbar,vbox_Record_Search_Toolbar);

        //VBox toolbar = createToolbar();
        //primary_Border_Pane.setBottom(toolbar);
        primary_Border_Pane.setCenter(split_Pane);
        primary_Border_Pane.setBottom(primary_Toolbar);
        primary_Border_Pane.setLeft(button_Toolbar);
    }

    private HBox createRecordToolbar()
    {

        HBox category_Toolbar = new HBox();
        category_Toolbar.setAlignment(Pos.CENTER);
        category_Toolbar.setSpacing(10);
        category_Toolbar.setManaged(false);

        HBox create_Record_Toolbar = new HBox();
        create_Record_Toolbar.setAlignment(Pos.CENTER);
        create_Record_Toolbar.setSpacing(10);

        VBox category_Section = new VBox();
        category_Section.setSpacing(10);

        Label label_Category = new Label("Category: ");
        label_Category.setAlignment(Pos.CENTER);

        Label label_library_ID = new Label("Library ID: ");

        TextField text_library_ID = new TextField("");

        Label label_Serial_Number = new Label("Serial Number: ");

        TextField text_Serial_Number = new TextField("");

        Label label_New_Category = new Label("Description: ");
        

        TextField text_Category_Description = new TextField("");
        

        Button button_Submit_Record = new Button("Submit Record");
        
        label_New_Category.setDisable(true);
        text_Category_Description.setDisable(true);
        button_Submit_Record.setDisable(true);

        TextField text_New_Category = new TextField("");
        text_New_Category.setVisible(false);

        Button button_Submit_Category = new Button("Submit Category");
        button_Submit_Category.setVisible(false);

        //MAKE TEXTFIELDS GROW BASED ON WINDOW SIZE:
        HBox.setHgrow(text_Category_Description, Priority.ALWAYS);
        HBox.setHgrow(text_Serial_Number, Priority.ALWAYS);
        
        ComboBox<String> combo_Category = new ComboBox<>();
        ObservableList<String> categories = logic_Layer.getCategories();
        categories.add(0,"");
        categories.add(1,"Create New Category");
        
        combo_Category.setItems(categories); //SORT LIST BEFORE ADDING TO COMBOBOX
        combo_Category.getSelectionModel().selectFirst();

        combo_Category.setOnAction((ActionEvent event)
                -> 
                {
                    switch (combo_Category.getSelectionModel().getSelectedIndex())
                    {
                        case 0:
                            label_New_Category.setDisable(true);
                            text_Category_Description.setDisable(true);
                            button_Submit_Record.setDisable(true);
                            text_New_Category.setVisible(false);
                            button_Submit_Category.setVisible(false);
                            category_Toolbar.setManaged(false);
                            break;
                        case 1:
                            label_New_Category.setDisable(true);
                            text_Category_Description.setDisable(true);
                            button_Submit_Record.setDisable(true);
                            text_New_Category.setText("Category Name");
                            text_New_Category.setVisible(true);
                            button_Submit_Category.setVisible(true);
                            category_Toolbar.setManaged(true);
                            break;
                        default:
                            label_New_Category.setDisable(false);
                            text_Category_Description.setDisable(false);
                            button_Submit_Record.setDisable(false);
                            text_New_Category.setVisible(false);
                            button_Submit_Category.setVisible(false);
                            category_Toolbar.setManaged(false);
                            break;
                    }

        });

        category_Toolbar.getChildren().addAll(text_New_Category, button_Submit_Category);
        category_Section.getChildren().addAll(combo_Category, category_Toolbar);
        create_Record_Toolbar.getChildren().addAll(label_library_ID, text_library_ID, label_Serial_Number, text_Serial_Number, label_Category, category_Section, label_New_Category, text_Category_Description, button_Submit_Record);
        return create_Record_Toolbar;
    }

    private HBox createEditRecordToolbar()
    {

        HBox create_Record_Toolbar = new HBox();
        create_Record_Toolbar.setAlignment(Pos.CENTER);
        create_Record_Toolbar.setSpacing(10);

        label_Edit_library_ID = new Label("Library ID: ");

        

        label_Edit_Serial_Number = new Label("Serial Number: ");

        

        label_Edit_Category = new Label("Category: ");
        label_Edit_Category.setAlignment(Pos.CENTER);

        combo_Edit_Category = new ComboBox<>();
        ObservableList<String> categories = logic_Layer.getCategories();
        combo_Edit_Category.setItems(categories);
        combo_Edit_Category.getSelectionModel().selectFirst();
            
        text_Edit_Serial_Number = new TextField("");
        text_Edit_Description = new TextField("");
        text_Edit_library_ID = new TextField("");
        
        //MAKE TEXTFIELDS GROW BASED ON WINDOW SIZE:
        HBox.setHgrow(text_Edit_Description, Priority.ALWAYS);
        HBox.setHgrow(text_Edit_Serial_Number, Priority.ALWAYS);
        
        Button button_Submit_Record = new Button("Save Record");
        
        
        

        create_Record_Toolbar.getChildren().addAll(label_Edit_library_ID, text_Edit_library_ID, label_Edit_Serial_Number, text_Edit_Serial_Number, label_Edit_Category, combo_Edit_Category, text_Edit_Description,button_Submit_Record);
        return create_Record_Toolbar;
    }
     private VBox createSearchToolbar()
    {
        //MAIN TOOLBAR:
        VBox create_Search_Toolbar = new VBox();
   
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
        text_Search_Commands = new TextField();  //SEARCH COMMANDS DISPLAYS RECORDS FOUND AND QUERY SENT
        text_Search_Commands.setEditable(false);
        text_Search_Commands.setAlignment(Pos.CENTER);
        text_Search_Commands.setDisable(true);
        text_Search_Commands.setStyle("-fx-opacity: 1;"); //MAKE DISABLE TEXTFIELD EASIER TO READ.
        logic_Layer.setMessageBox(text_Search_Commands);
        
        text_Search_Query = new TextField(); //ENTER SEARCH TERMS OR PHRASES
        text_Search_Query.setText("Search Terms");
        text_Search_Query.setTooltip(search_Query_Tooltip);
        text_Search_Query.setOnKeyPressed(new EventHandler<KeyEvent>()
        {

            @Override
            public void handle(KeyEvent keyEvent)
            {
                //When a user hits enter send the query/text for search consistency/user expectations
                if (keyEvent.getCode().equals(KeyCode.ENTER))
                {

                    //table_Software_View.<Ticket>setItems(logic_Layer.search(text_Search_Query.getText(), technician_Combo.getSelectionModel().getSelectedItem()));
                    //table_Software_View.getSelectionModel().clearSelection();

                }

            }

        });
        
        combo_Search_Category = new ComboBox<>();
        ObservableList<String> categories = logic_Layer.getCategories();
        combo_Search_Category.setItems(categories);
        combo_Search_Category.getSelectionModel().selectFirst();
        
        
        //MAKE TEXTFIELDS GROW BASED ON WINDOW SIZE:
        HBox.setHgrow(text_Search_Commands, Priority.ALWAYS);
        HBox.setHgrow(text_Search_Query, Priority.ALWAYS);
       
       
        //SEARCH BUTTON USED TO RETURN SEARCH RESULTS
        Button search_Button = new Button();
        search_Button.setText("Search");
        search_Button.setOnAction((ActionEvent event)
                -> 
                {
                    
                    //table_Software_View.setItems(logic_Layer.search(text_Search_Query.getText(), combo_Search_Category.getSelectionModel().getSelectedItem()));
                    //table_Software_View.getSelectionModel().clearSelection();   //CLEAR SELECTION TO PREVENT NULL ROWS
        });


        //CREATE TOOLBAR LABELS
        Label search_Label = new Label("Search Database: ");
        Label technician_Label = new Label("Category: ");
        

        //ADD CONTROLS TO HBOX THEN TO VBOX:
        hbox_Toolbar_Top.getChildren().addAll(search_Label, text_Search_Query, technician_Label, combo_Search_Category, search_Button);
        hbox_Toolbar_Bottom.getChildren().addAll(text_Search_Commands);
        //ADD BOTH HBOX TO VBOX
        create_Search_Toolbar.getChildren().addAll(hbox_Toolbar_Top,hbox_Toolbar_Bottom);
        
        return create_Search_Toolbar;
    }
    private void setEditRecordFields()
    {
        Software selected_Row = table_Software_View.getSelectionModel().getSelectedItem();
        text_Edit_library_ID.setText(selected_Row.getLibrary_ID());
        text_Edit_Serial_Number.setText(selected_Row.getSerial_Number());
        String[] category = selected_Row.getCategory().split("[-]");
        
        //text_Edit_Category.setText(category[0].trim());
        combo_Edit_Category.getSelectionModel().select(category[0].trim());
        
        text_Edit_Description.setText(category[1].trim());
        
    }

   

    private void editNote()
    {
        if (boolean_Editing_Note)
        {
            button_Edit_Note.setText("Edit Note");
            textarea_Note_Area.setEditable(false);
            boolean_Editing_Note = false;
            table_Software_View.setDisable(false);
            
            button_Save_Note.setVisible(false);
            
            vbox_Record_Search_Toolbar.setManaged(true);
            vbox_Record_Search_Toolbar.setVisible(true);
            
            button_Create_Record.setDisable(false);
            button_Edit_Record.setDisable(false);
            
        }
        else
        {
            
            button_Edit_Note.setText("Cancel");
            textarea_Note_Area.setEditable(true);
            table_Software_View.setDisable(true);
            boolean_Editing_Note = true;
            
            button_Save_Note.setVisible(true);
            
            vbox_Record_Search_Toolbar.setManaged(false);
            vbox_Record_Search_Toolbar.setVisible(false);
            
            button_Create_Record.setDisable(true);
            button_Edit_Record.setDisable(true);
            
            
        }
    }

    private void editRecord()
    {
        if (boolean_Editing_Record)
        {
            button_Edit_Record.setText("Edit Record");
            textarea_Note_Area.setEditable(false);
            boolean_Editing_Record = false;
            table_Software_View.setDisable(false);
            hbox_Record_Edit_Toolbar.setManaged(false);
            hbox_Record_Edit_Toolbar.setVisible(false);
            
            vbox_Record_Search_Toolbar.setManaged(true);
            vbox_Record_Search_Toolbar.setVisible(true);
            
            button_Create_Record.setDisable(false);
            button_Edit_Note.setDisable(false);
            
        }
        else
        {
            button_Edit_Record.setText("Cancel");
            textarea_Note_Area.setEditable(true);
            table_Software_View.setDisable(true);
            boolean_Editing_Record = true;
            hbox_Record_Edit_Toolbar.setManaged(true);
            hbox_Record_Edit_Toolbar.setVisible(true);
            
            vbox_Record_Search_Toolbar.setManaged(false);
            vbox_Record_Search_Toolbar.setVisible(false);
            
            button_Create_Record.setDisable(true);
            button_Edit_Note.setDisable(true);
            
        }
    }
    private void createRecord()
    {
        if (boolean_Creating_Record)
        {
            button_Create_Record.setText("Create Record");
            
            textarea_Note_Area.setEditable(false);
            table_Software_View.setDisable(false);
            
            boolean_Creating_Record = false;
            hbox_Record_Create_Toolbar.setManaged(false);
            hbox_Record_Create_Toolbar.setVisible(false);
            
            vbox_Record_Search_Toolbar.setManaged(true);
            vbox_Record_Search_Toolbar.setVisible(true);
            
            button_Edit_Note.setDisable(false);
            button_Edit_Record.setDisable(false);
            
        }
        else
        {
            button_Create_Record.setText("Cancel");
            textarea_Note_Area.setEditable(true);
            table_Software_View.setDisable(true);
            boolean_Creating_Record = true;
            hbox_Record_Create_Toolbar.setManaged(true);
            hbox_Record_Create_Toolbar.setVisible(true);
            
            vbox_Record_Search_Toolbar.setManaged(false);
            vbox_Record_Search_Toolbar.setVisible(false);
            
            button_Edit_Note.setDisable(true);
            button_Edit_Record.setDisable(true);
            
            
        }
    }
    

    public Node getSoftwareLibraryPane()
    {
        return primary_Border_Pane;
    }

    private TableView<Software> createTableView(Stage primary_Stage)
    {
        TableView<Software> table_View = new TableView<>();

        table_View.setEditable(false);
        //BOTH EVENTS GET THE TICKET THAT MATCHES THE ROW AND DISPLAYS ITS DESCRIPTION IN THE RIGHT PANE
        //SETUP MOUSE EVENTS: MOUSE CLICK
        table_View.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent mouseEvent)
            {

                if (table_Software_View.getSelectionModel().isEmpty() == false)
                {
                    Software current = (Software) table_Software_View.getItems().get(table_Software_View.getSelectionModel().getSelectedIndex());
                    textarea_Note_Area.setText(current.getNote());
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

                    if (table_Software_View.getSelectionModel().getSelectedIndex() > 0)
                    {
                        Software current = (Software) table_Software_View.getItems().get(table_Software_View.getSelectionModel().getSelectedIndex() - 1);
                        textarea_Note_Area.setText(current.getNote());
                    }

                }
                else if (keyEvent.getCode().equals(KeyCode.DOWN))
                {
                    if (table_Software_View.getSelectionModel().getSelectedIndex() < table_Software_View.getItems().size() - 1)
                    {
                        Software current = (Software) table_Software_View.getItems().get(table_Software_View.getSelectionModel().getSelectedIndex() + 1);
                        textarea_Note_Area.setText(current.getNote());
                    }

                }
            }

        });

        //CREATE TABLEVIEW COLUMNS
        TableColumn<Software, String> software_Category = new TableColumn<>("Category");
        software_Category.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn<Software, String> software_Serial_Number = new TableColumn<>("Serial Number");
        software_Serial_Number.setCellValueFactory(new PropertyValueFactory<>("serial_Number"));

        TableColumn<Software, String> software_Library_ID = new TableColumn<>("Library ID");
        software_Library_ID.setCellValueFactory(new PropertyValueFactory<>("library_ID"));

        TableColumn<Software, String> software_Created_On = new TableColumn<>("Created On");
        software_Created_On.setCellValueFactory(new PropertyValueFactory<>("created_on"));

        TableColumn<Software, String> software_Status = new TableColumn<>("Status");
        software_Status.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Software, String> software_Checked_Out_By = new TableColumn<>("Checked Out By");
        software_Checked_Out_By.setCellValueFactory(new PropertyValueFactory<>("checked_out_by"));

        TableColumn<Software, String> software_Checked_Out_On = new TableColumn<>("Checked Out On");
        software_Checked_Out_On.setCellValueFactory(new PropertyValueFactory<>("checked_out_at"));

        TableColumn<Software, String> software_Last_Checked_Out_By = new TableColumn<>("Last Checked Out By");
        software_Last_Checked_Out_By.setCellValueFactory(new PropertyValueFactory<>("last_checked_out_by"));

        TableColumn<Software, String> software_Last_Checked_Out_On = new TableColumn<>("Last Checked Out On");
        software_Last_Checked_Out_On.setCellValueFactory(new PropertyValueFactory<>("last_checked_out_at"));

        /*
        //SET COLUMN SIZE/WIDTH.
        ticket_ID.setPrefWidth(50);
        ticket_Summary.setPrefWidth(600);
        ticket_Technician_Name.setPrefWidth(150);
        actionCol.setPrefWidth(125);

         */
        //OBSERVABLE LIST IS CREATED MANUALLY TO ENSURE TYPE SAFETY WITH GENERICS.
        ObservableList<TableColumn<Software, String>> list_Table_Columns = FXCollections.observableArrayList();
        list_Table_Columns.add(software_Library_ID);
        list_Table_Columns.add(software_Category);
        list_Table_Columns.add(software_Serial_Number);
        list_Table_Columns.add(software_Created_On);
        list_Table_Columns.add(software_Status);
        list_Table_Columns.add(software_Checked_Out_By);
        list_Table_Columns.add(software_Checked_Out_On);
        list_Table_Columns.add(software_Last_Checked_Out_By);
        list_Table_Columns.add(software_Last_Checked_Out_On);
        //SET THE COLUMNS
        table_View.getColumns().addAll(list_Table_Columns);
        //SET THE DATA THAT CORRESPONDS WITH COLUMNS

        table_View.setItems(list_Software.sorted(DEFAULT_SOFTWARE_Comparator));

        return table_View;

    }

}
