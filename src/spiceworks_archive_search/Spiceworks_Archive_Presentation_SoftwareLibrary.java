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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
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
import spiceworks_archive_javafx.Software;

/**
 *
 * @author it.student
 */
public class Spiceworks_Archive_Presentation_SoftwareLibrary
{

    private BorderPane primary_Border_Pane;
    private final Spiceworks_Archive_Logic logic_Layer;
    private static ObservableList<Software> list_Software;
    private static TableView<Software> table_Software_View;
    private static TextArea textarea_Note_Area;

    private static HBox hbox_Record_Create_Toolbar;
    private static HBox hbox_Record_Edit_Toolbar;
    private static VBox vbox_Record_Search_Toolbar;
    private static VBox button_Toolbar;
    private static VBox primary_Toolbar;
    private static VBox VBox_Login_Pane;

    private static Button button_Edit_Note;
    private static Button button_Save_Note;
    private static Button button_Edit_Record;
    private static Button button_Create_Record;
    private static Button button_Check_IN;

    private static Button button_New_Rcrd_Tlbr_Submit_Record;
    private static Button button_New_Rcrd_Tlbr_Submit_Category;
    private static Button button_Edit_Rcrd_Tlbr_Submit_Record;

    private static boolean boolean_Editing_Note = false;
    private static boolean boolean_Editing_Record = false;
    private static boolean boolean_Creating_Record = false;

    private static Label label_Edit_Rcrd_Tlbr_library_ID;
    private static Label label_Edit_Rcrd_Tlbr_Category;
    private static Label label_Edit_Rcrd_Tlbr_Serial_Number;

    private static TextField text_Edit_Rcrd_Tlbr_library_ID;
    private static TextField text_Edit_Rcrd_Tlbr_Serial_Number;
    private static TextField text_Edit_Rcrd_Tlbr_Description;

    private static TextField text_New_Rcrd_Tlbr_Category_Description;
    private static TextField text_New_Rcrd_Tlbr_New_Category;

    private static TextField text_Srch_Tlbr_Commands;
    private static TextField text_Srch_Tlbr_Query;

    private static TextField text_New_Rcrd_Tlbr_library_ID;
    private static TextField text_New_Rcrd_Tlbr_Serial_Number;

    private static ComboBox<String> combo_Edit_Rcrd_Tlbr_Category;
    private static ComboBox<String> combo_Srch_Tlbr_Category;
    private static ComboBox<String> login_Technician;
    private static ComboBox<String> combo_New_Rcrd_Tlbr_Category;

    private static SplitPane split_Pane;
    private static String current_Technician;

    public Spiceworks_Archive_Presentation_SoftwareLibrary(Spiceworks_Archive_Logic logic_Layer)
    {
        this.logic_Layer = logic_Layer;
        list_Software = logic_Layer.getSoftware();

    }

    public void initializeSoftwareLibraryPane(Stage primary_Stage)
    {
        VBox_Login_Pane = new VBox();
        VBox_Login_Pane.setSpacing(10);
        VBox_Login_Pane.setAlignment(Pos.CENTER);
        Label label_Login_Message = new Label("Select Technician");
        login_Technician = new ComboBox<>(logic_Layer.getTechnicians());
        Button button_Login = new Button("Submit");

        button_Login.setOnAction((ActionEvent event)
                -> 
                {
                    buttonPressedLogin();
        });

        VBox_Login_Pane.getChildren().addAll(label_Login_Message, login_Technician, button_Login);

        button_Toolbar = new VBox();

        primary_Border_Pane = new BorderPane();
        BorderPane right_Border_Pane = new BorderPane();

        HBox note_Toolbar = new HBox();
        note_Toolbar.setPadding(new Insets(15, 12, 15, 12));
        note_Toolbar.setSpacing(10);
        button_Check_IN = new Button("CHECK OUT");
        button_Edit_Note = new Button("Edit Note");
        button_Save_Note = new Button("Save Note");
        button_Save_Note.setVisible(false);

        button_Edit_Record = new Button("Edit Record");
        button_Create_Record = new Button("Create Record");

        button_Check_IN.setOnAction((ActionEvent event)
                -> 
                {
                    buttonPressedCheckIn();
        });

        button_Edit_Note.setOnAction((ActionEvent event)
                -> 
                {
                    buttonPressedEditNote();
        });
        button_Edit_Record.setOnAction((ActionEvent event)
                -> 
                {
                    buttonPressedEditRecord();

        });
        button_Create_Record.setOnAction((ActionEvent event)
                -> 
                {
                    buttonPressedCreateRecord();
        });
        button_Save_Note.setOnAction((ActionEvent event)
                -> 
                {
                    buttonPressedSaveNote();
        });

        button_Toolbar.getChildren().addAll(button_Check_IN, button_Create_Record, button_Edit_Record, button_Edit_Note, button_Save_Note);
        button_Toolbar.setAlignment(Pos.CENTER);
        button_Toolbar.setPadding(new Insets(15, 12, 15, 12));
        button_Toolbar.setSpacing(10);

        //note_Toolbar.getChildren().addAll(edit_Note, edit_Record, create_Record);
        //note_Toolbar.setAlignment(Pos.CENTER);
        textarea_Note_Area = new TextArea();
        textarea_Note_Area.setEditable(false);
        right_Border_Pane.setCenter(textarea_Note_Area);
        //right_Border_Pane.setLeft(button_Toolbar);

        primary_Toolbar = new VBox();
        primary_Toolbar.setPadding(new Insets(15, 12, 15, 12));
        primary_Toolbar.setSpacing(10);
        split_Pane = new SplitPane();
        table_Software_View = createTableView(primary_Stage);
        table_Software_View.getSelectionModel().selectFirst();

        split_Pane.getItems().addAll(table_Software_View, right_Border_Pane);
        split_Pane.setDividerPositions(0.8);

        updateCheckInButton(table_Software_View.getSelectionModel().getSelectedItem());

        hbox_Record_Create_Toolbar = createRecordToolbar();
        hbox_Record_Create_Toolbar.setManaged(false);
        hbox_Record_Create_Toolbar.setVisible(false);

        hbox_Record_Edit_Toolbar = createEditRecordToolbar();
        hbox_Record_Edit_Toolbar.setManaged(false);
        hbox_Record_Edit_Toolbar.setVisible(false);

        vbox_Record_Search_Toolbar = createSearchToolbar();

        primary_Toolbar.getChildren().addAll(hbox_Record_Create_Toolbar, hbox_Record_Edit_Toolbar, vbox_Record_Search_Toolbar);

        //VBox toolbar = createToolbar();
        //primary_Border_Pane.setBottom(toolbar);
        primary_Border_Pane.setTop(VBox_Login_Pane);

        primary_Border_Pane.setCenter(split_Pane);
        primary_Border_Pane.setBottom(primary_Toolbar);
        primary_Border_Pane.setLeft(button_Toolbar);

        split_Pane.setManaged(false);
        primary_Toolbar.setManaged(false);
        button_Toolbar.setManaged(false);

        split_Pane.setVisible(false);
        primary_Toolbar.setVisible(false);
        button_Toolbar.setVisible(false);
    }

    private HBox createRecordToolbar()
    {

        HBox create_Record_Toolbar = new HBox();
        create_Record_Toolbar.setAlignment(Pos.CENTER);
        create_Record_Toolbar.setSpacing(10);

        VBox category_Section = new VBox();
        category_Section.setSpacing(10);

        Label label_Category = new Label("Category: ");
        label_Category.setAlignment(Pos.CENTER);

        Label label_library_ID = new Label("Library ID: ");

        text_New_Rcrd_Tlbr_library_ID = new TextField("");

        Label label_Serial_Number = new Label("Serial Number: ");

        text_New_Rcrd_Tlbr_Serial_Number = new TextField("");

        Label label_New_Category = new Label("Description: ");

        text_New_Rcrd_Tlbr_Category_Description = new TextField("");

        button_New_Rcrd_Tlbr_Submit_Record = new Button("Submit Record");

        button_New_Rcrd_Tlbr_Submit_Record.setOnAction((ActionEvent event)
                -> 
                {
                    buttonPressedSubmitRecord();
        });
        button_New_Rcrd_Tlbr_Submit_Record.setDisable(true);

        text_New_Rcrd_Tlbr_Category_Description.setDisable(true);

        text_New_Rcrd_Tlbr_New_Category = new TextField("");
        text_New_Rcrd_Tlbr_New_Category.setVisible(false);

        button_New_Rcrd_Tlbr_Submit_Category = new Button("Submit Category");
        button_New_Rcrd_Tlbr_Submit_Category.setVisible(false);
        button_New_Rcrd_Tlbr_Submit_Category.setOnAction((ActionEvent event)
                -> 
                {
                    buttonPressedCreateNewCategory();
        });

        //MAKE TEXTFIELDS GROW BASED ON WINDOW SIZE:
        HBox.setHgrow(text_New_Rcrd_Tlbr_Category_Description, Priority.ALWAYS);
        HBox.setHgrow(text_New_Rcrd_Tlbr_Serial_Number, Priority.ALWAYS);

        //NEW CATEGORY TOOLBAR
        HBox category_Toolbar = new HBox();
        category_Toolbar.setAlignment(Pos.CENTER);
        category_Toolbar.setSpacing(10);
        category_Toolbar.setManaged(false);

        combo_New_Rcrd_Tlbr_Category = new ComboBox<>();
        ObservableList<String> categories = logic_Layer.getCategories();
        categories.add(0, "");
        categories.add(1, "Create New Category");

        combo_New_Rcrd_Tlbr_Category.setItems(categories); //SORT LIST BEFORE ADDING TO COMBOBOX
        combo_New_Rcrd_Tlbr_Category.getSelectionModel().selectFirst();

        combo_New_Rcrd_Tlbr_Category.setOnAction((ActionEvent event)
                -> 
                {
                    updateNewRecordCategoryControls(combo_New_Rcrd_Tlbr_Category.getSelectionModel().getSelectedIndex(), category_Toolbar);

        });

        category_Toolbar.getChildren().addAll(text_New_Rcrd_Tlbr_New_Category, button_New_Rcrd_Tlbr_Submit_Category);
        //END OF NEW CATEGORY TOOLBAR SETTINGS

        category_Section.getChildren().addAll(combo_New_Rcrd_Tlbr_Category, category_Toolbar);
        create_Record_Toolbar.getChildren().addAll(label_library_ID, text_New_Rcrd_Tlbr_library_ID, label_Serial_Number, text_New_Rcrd_Tlbr_Serial_Number, label_Category, category_Section, label_New_Category, text_New_Rcrd_Tlbr_Category_Description, button_New_Rcrd_Tlbr_Submit_Record);
        return create_Record_Toolbar;
    }

    private HBox createEditRecordToolbar()
    {

        HBox create_Record_Toolbar = new HBox();
        create_Record_Toolbar.setAlignment(Pos.CENTER);
        create_Record_Toolbar.setSpacing(10);

        label_Edit_Rcrd_Tlbr_library_ID = new Label("Library ID: ");

        label_Edit_Rcrd_Tlbr_Serial_Number = new Label("Serial Number: ");

        label_Edit_Rcrd_Tlbr_Category = new Label("Category: ");
        label_Edit_Rcrd_Tlbr_Category.setAlignment(Pos.CENTER);

        combo_Edit_Rcrd_Tlbr_Category = new ComboBox<>();
        ObservableList<String> categories = logic_Layer.getCategories();
        combo_Edit_Rcrd_Tlbr_Category.setItems(categories);
        combo_Edit_Rcrd_Tlbr_Category.getSelectionModel().selectFirst();

        text_Edit_Rcrd_Tlbr_Serial_Number = new TextField("");
        text_Edit_Rcrd_Tlbr_Description = new TextField("");
        text_Edit_Rcrd_Tlbr_library_ID = new TextField("");

        //MAKE TEXTFIELDS GROW BASED ON WINDOW SIZE:
        HBox.setHgrow(text_Edit_Rcrd_Tlbr_Description, Priority.ALWAYS);
        HBox.setHgrow(text_Edit_Rcrd_Tlbr_Serial_Number, Priority.ALWAYS);

        button_Edit_Rcrd_Tlbr_Submit_Record = new Button("Save Record");
        button_Edit_Rcrd_Tlbr_Submit_Record.setOnAction((ActionEvent event)
                -> 
                {
                    buttonPressedSaveEditedRecord();

        });

        create_Record_Toolbar.getChildren().addAll(label_Edit_Rcrd_Tlbr_library_ID, text_Edit_Rcrd_Tlbr_library_ID, label_Edit_Rcrd_Tlbr_Serial_Number, text_Edit_Rcrd_Tlbr_Serial_Number, label_Edit_Rcrd_Tlbr_Category, combo_Edit_Rcrd_Tlbr_Category, text_Edit_Rcrd_Tlbr_Description, button_Edit_Rcrd_Tlbr_Submit_Record);
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
        text_Srch_Tlbr_Commands = new TextField();  //SEARCH COMMANDS DISPLAYS RECORDS FOUND AND QUERY SENT
        text_Srch_Tlbr_Commands.setEditable(false);
        text_Srch_Tlbr_Commands.setAlignment(Pos.CENTER);
        text_Srch_Tlbr_Commands.setDisable(true);
        text_Srch_Tlbr_Commands.setStyle("-fx-opacity: 1;"); //MAKE DISABLE TEXTFIELD EASIER TO READ.

        text_Srch_Tlbr_Query = new TextField(); //ENTER SEARCH TERMS OR PHRASES
        text_Srch_Tlbr_Query.setText("Search Terms");
        text_Srch_Tlbr_Query.setTooltip(search_Query_Tooltip);
        text_Srch_Tlbr_Query.setOnKeyPressed(new EventHandler<KeyEvent>()
        {

            @Override
            public void handle(KeyEvent keyEvent)
            {
                //When a user hits enter send the query/text for search consistency/user expectations
                if (keyEvent.getCode().equals(KeyCode.ENTER))
                {

                    table_Software_View.setItems(logic_Layer.searchSoftware(text_Srch_Tlbr_Query.getText(), combo_Srch_Tlbr_Category.getSelectionModel().getSelectedItem(), text_Srch_Tlbr_Commands));
                    table_Software_View.getSelectionModel().clearSelection();

                }

            }

        });

        combo_Srch_Tlbr_Category = new ComboBox<>();
        ObservableList<String> categories = logic_Layer.getCategories();
        categories.add(0, "");
        combo_Srch_Tlbr_Category.setItems(categories);
        combo_Srch_Tlbr_Category.getSelectionModel().selectFirst();

        //MAKE TEXTFIELDS GROW BASED ON WINDOW SIZE:
        HBox.setHgrow(text_Srch_Tlbr_Commands, Priority.ALWAYS);
        HBox.setHgrow(text_Srch_Tlbr_Query, Priority.ALWAYS);

        //SEARCH BUTTON USED TO RETURN SEARCH RESULTS
        Button search_Button = new Button();
        search_Button.setText("Search");
        search_Button.setOnAction((ActionEvent event)
                -> 
                {

                    table_Software_View.setItems(logic_Layer.searchSoftware(text_Srch_Tlbr_Query.getText(), combo_Srch_Tlbr_Category.getSelectionModel().getSelectedItem(), text_Srch_Tlbr_Commands));
                    table_Software_View.getSelectionModel().clearSelection();   //CLEAR SELECTION TO PREVENT NULL ROWS
        });

        //CREATE TOOLBAR LABELS
        Label search_Label = new Label("Search Database: ");
        Label technician_Label = new Label("Category: ");

        //ADD CONTROLS TO HBOX THEN TO VBOX:
        hbox_Toolbar_Top.getChildren().addAll(search_Label, text_Srch_Tlbr_Query, technician_Label, combo_Srch_Tlbr_Category, search_Button);
        hbox_Toolbar_Bottom.getChildren().addAll(text_Srch_Tlbr_Commands);
        //ADD BOTH HBOX TO VBOX
        create_Search_Toolbar.getChildren().addAll(hbox_Toolbar_Top, hbox_Toolbar_Bottom);

        return create_Search_Toolbar;
    }

    private void updateNewRecordCategoryControls(int selection, HBox category_Toolbar)
    {
        switch (selection)
        {
            case 0:

                text_New_Rcrd_Tlbr_Category_Description.setDisable(true);
                button_New_Rcrd_Tlbr_Submit_Record.setDisable(true);
                text_New_Rcrd_Tlbr_New_Category.setVisible(false);
                button_New_Rcrd_Tlbr_Submit_Category.setVisible(false);
                category_Toolbar.setManaged(false);
                button_New_Rcrd_Tlbr_Submit_Record.setDisable(true);
                break;
            case 1:

                text_New_Rcrd_Tlbr_Category_Description.setDisable(true);
                button_New_Rcrd_Tlbr_Submit_Record.setDisable(true);
                text_New_Rcrd_Tlbr_New_Category.setText("Category Name");
                text_New_Rcrd_Tlbr_New_Category.setVisible(true);
                button_New_Rcrd_Tlbr_Submit_Category.setVisible(true);
                category_Toolbar.setManaged(true);
                button_New_Rcrd_Tlbr_Submit_Record.setDisable(true);
                break;
            default:

                text_New_Rcrd_Tlbr_Category_Description.setDisable(false);
                button_New_Rcrd_Tlbr_Submit_Record.setDisable(false);
                text_New_Rcrd_Tlbr_New_Category.setVisible(false);
                button_New_Rcrd_Tlbr_Submit_Category.setVisible(false);
                category_Toolbar.setManaged(false);
                button_New_Rcrd_Tlbr_Submit_Record.setDisable(false);
                break;
        }
    }

    private void buttonPressedEditNote()
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

    private void buttonPressedEditRecord()
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
            Software selected_Row = table_Software_View.getSelectionModel().getSelectedItem();
            text_Edit_Rcrd_Tlbr_library_ID.setText(selected_Row.getLibraryID());
            text_Edit_Rcrd_Tlbr_Serial_Number.setText(selected_Row.getSerialNumber());
            String[] category = selected_Row.getCategory().split("[-]", 2);

            //text_Edit_Category.setText(category[0].trim());
            combo_Edit_Rcrd_Tlbr_Category.getSelectionModel().select(category[0].trim());

            text_Edit_Rcrd_Tlbr_Description.setText(category[1].trim());
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

    private void buttonPressedCheckIn()
    {
        Software current_Selection = table_Software_View.getSelectionModel().getSelectedItem();
        if (current_Selection.getStatus().equals("IN"))
        {

            current_Selection.setStatus("OUT");
            current_Selection.setCheckedOutBy(current_Technician);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            current_Selection.setCheckedOutAt(dateFormat.format(date));

        }
        else
        {

            current_Selection.setStatus("IN");
            current_Selection.setLastCheckedOutBy(current_Selection.getCheckedOutBy());
            current_Selection.setLastCheckedOutAt(current_Selection.getCheckedOutAt());
            current_Selection.setCheckedOutAt("");
            current_Selection.setCheckedOutBy("NO ONE");
        }
        logic_Layer.updateCheckInFields(current_Selection.getStatus(), current_Selection.getCheckedOutBy(), current_Selection.getCheckedOutAt(), current_Selection.getLastCheckedOutBy(), current_Selection.getLastCheckedOutAt(), current_Selection.getID());

        updateCheckInButton(current_Selection);
    }

    private void buttonPressedLogin()
    {

        if (login_Technician.getSelectionModel().isSelected(0) == false)
        {
            split_Pane.setManaged(true);
            primary_Toolbar.setManaged(true);
            button_Toolbar.setManaged(true);
            split_Pane.setVisible(true);
            primary_Toolbar.setVisible(true);
            button_Toolbar.setVisible(true);
            current_Technician = login_Technician.getSelectionModel().getSelectedItem();
            VBox_Login_Pane.setManaged(false);
            VBox_Login_Pane.setVisible(false);
        }

    }

    private void buttonPressedSubmitRecord()
    {
        String category_Plus_Description = combo_New_Rcrd_Tlbr_Category.getSelectionModel().getSelectedItem() + " - " + text_New_Rcrd_Tlbr_Category_Description.getText();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        Software new_Record = logic_Layer.insertSoftwareRecord(text_New_Rcrd_Tlbr_library_ID.getText(),
                category_Plus_Description,
                text_New_Rcrd_Tlbr_Serial_Number.getText(),
                textarea_Note_Area.getText(),
                combo_New_Rcrd_Tlbr_Category.getSelectionModel().getSelectedItem(),
                dateFormat.format(date));
        buttonPressedCreateRecord();
        
        list_Software.add(new_Record);
        

    }

    private void buttonPressedCreateNewCategory()
    {
        if(text_New_Rcrd_Tlbr_New_Category.getText() != null && text_New_Rcrd_Tlbr_New_Category.getText().trim().isEmpty() == false)
        {
            combo_Edit_Rcrd_Tlbr_Category.getItems().add(text_New_Rcrd_Tlbr_New_Category.getText());
            combo_New_Rcrd_Tlbr_Category.getItems().add(text_New_Rcrd_Tlbr_New_Category.getText());
            combo_Srch_Tlbr_Category.getItems().add(text_New_Rcrd_Tlbr_New_Category.getText());
        }
        logic_Layer.insertSoftwareCategory(text_New_Rcrd_Tlbr_New_Category.getText());
    }

    private void buttonPressedSaveEditedRecord()
    {
        Software selected_Row = table_Software_View.getSelectionModel().getSelectedItem();
        String library_ID = text_Edit_Rcrd_Tlbr_library_ID.getText();
        String serial_Number = text_Edit_Rcrd_Tlbr_Serial_Number.getText();
        String note = textarea_Note_Area.getText();
        String category = combo_Edit_Rcrd_Tlbr_Category.getSelectionModel().getSelectedItem();
        String description = category + " - " + text_Edit_Rcrd_Tlbr_Description.getText();
        logic_Layer.updateSoftwareRecord(
                selected_Row.getID(),
                library_ID,
                description, 
                serial_Number,
                note,
                category);
        
        selected_Row.setLibraryID(library_ID);
        selected_Row.setCategory(description);
        selected_Row.setSerialNumber(serial_Number);
        selected_Row.setNote(note);
        buttonPressedEditRecord();
    }
    private void buttonPressedSaveNote()
    {
        Software selected_Row = table_Software_View.getSelectionModel().getSelectedItem();
        String note = textarea_Note_Area.getText();
        if(note == null)
        {
            note = "";
        }
        logic_Layer.updateSoftwareRecordNote(selected_Row.getID(), note);
        selected_Row.setNote(note);
        buttonPressedEditNote();
    }

    private void updateCheckInButton(Software current_Selection)
    {
        if (current_Selection.getStatus().equalsIgnoreCase("IN"))
        {
            button_Check_IN.setText("CHECK OUT");
        }
        else
        {
            button_Check_IN.setText("CHECK IN");
        }
    }

    private void buttonPressedCreateRecord()
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
            
            textarea_Note_Area.clear();

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
                    updateCheckInButton(current);
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
                        updateCheckInButton(current);
                    }

                }
                else if (keyEvent.getCode().equals(KeyCode.DOWN))
                {
                    if (table_Software_View.getSelectionModel().getSelectedIndex() < table_Software_View.getItems().size() - 1)
                    {
                        Software current = (Software) table_Software_View.getItems().get(table_Software_View.getSelectionModel().getSelectedIndex() + 1);
                        textarea_Note_Area.setText(current.getNote());
                        updateCheckInButton(current);
                    }

                }
            }

        });

        //CREATE TABLEVIEW COLUMNS
        TableColumn<Software, String> software_Category = new TableColumn<>("Category");
        software_Category.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn<Software, String> software_Serial_Number = new TableColumn<>("Serial Number");
        software_Serial_Number.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));

        TableColumn<Software, String> software_Library_ID = new TableColumn<>("Library ID");
        software_Library_ID.setCellValueFactory(new PropertyValueFactory<>("libraryID"));

        TableColumn<Software, String> software_Created_On = new TableColumn<>("Created On");
        software_Created_On.setCellValueFactory(new PropertyValueFactory<>("createdOn"));

        TableColumn<Software, String> software_Status = new TableColumn<>("Status");
        software_Status.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Software, String> software_Checked_Out_By = new TableColumn<>("Checked Out By");
        software_Checked_Out_By.setCellValueFactory(new PropertyValueFactory<>("checkedOutBy"));

        TableColumn<Software, String> software_Checked_Out_On = new TableColumn<>("Checked Out On");
        software_Checked_Out_On.setCellValueFactory(new PropertyValueFactory<>("checkedOutAt"));

        TableColumn<Software, String> software_Last_Checked_Out_By = new TableColumn<>("Last Checked Out By");
        software_Last_Checked_Out_By.setCellValueFactory(new PropertyValueFactory<>("lastCheckedOutBy"));

        TableColumn<Software, String> software_Last_Checked_Out_On = new TableColumn<>("Last Checked Out On");
        software_Last_Checked_Out_On.setCellValueFactory(new PropertyValueFactory<>("lastCheckedOutAt"));

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
        list_Table_Columns.add(software_Status);
        list_Table_Columns.add(software_Checked_Out_By);
        list_Table_Columns.add(software_Checked_Out_On);
        list_Table_Columns.add(software_Last_Checked_Out_By);
        list_Table_Columns.add(software_Last_Checked_Out_On);
        list_Table_Columns.add(software_Created_On);
        //SET THE COLUMNS
        table_View.getColumns().addAll(list_Table_Columns);
        //SET THE DATA THAT CORRESPONDS WITH COLUMNS

        table_View.setItems(list_Software);
        table_View.getSortOrder().setAll(software_Library_ID);

        return table_View;

    }

}
