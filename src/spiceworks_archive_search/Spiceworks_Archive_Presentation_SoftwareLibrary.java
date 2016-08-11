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

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
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

    private BorderPane primary_Border_Pane;
    private final Spiceworks_Archive_Logic logic_Layer;
    private static ObservableList<Software> software_List;
    private static TableView<Software> software_View;
    private static TextArea note_Area;

    public Spiceworks_Archive_Presentation_SoftwareLibrary(Spiceworks_Archive_Logic logic_Layer)
    {
        this.logic_Layer = logic_Layer;
        software_List = logic_Layer.getSoftware();
    }

    public void initializeSoftwareLibraryPane(Stage primary_Stage)
    {
        primary_Border_Pane = new BorderPane();
        BorderPane right_Border_Pane = new BorderPane();

        note_Area = new TextArea();
        note_Area.setEditable(false);
        right_Border_Pane.setCenter(note_Area);

        SplitPane split_Pane = new SplitPane();
        software_View = createTableView(primary_Stage);
        split_Pane.getItems().addAll(software_View, note_Area);
        split_Pane.setDividerPositions(0.8);

        //VBox toolbar = createToolbar();
        //primary_Border_Pane.setBottom(toolbar);
        primary_Border_Pane.setCenter(split_Pane);
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

                if (software_View.getSelectionModel().isEmpty() == false)
                {
                    Software current = (Software) software_View.getItems().get(software_View.getSelectionModel().getSelectedIndex());
                    note_Area.setText(current.getNote());
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

                    if (software_View.getSelectionModel().getSelectedIndex() > 0)
                    {
                        Software current = (Software) software_View.getItems().get(software_View.getSelectionModel().getSelectedIndex() - 1);
                        note_Area.setText(current.getNote());
                    }

                }
                else if (keyEvent.getCode().equals(KeyCode.DOWN))
                {
                    if (software_View.getSelectionModel().getSelectedIndex() < software_View.getItems().size() - 1)
                    {
                       Software current = (Software) software_View.getItems().get(software_View.getSelectionModel().getSelectedIndex()+1);
                        note_Area.setText(current.getNote());
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
        table_View.setItems(software_List);

        return table_View;

    }

}
