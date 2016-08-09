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

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author it.student
 */
public class TicketButtonCell extends TableCell<Ticket, Boolean>
{

    // a button for adding a new person.
    final Button link_Button = new Button("Spiceworks Link");
    // pads and centers the add button in the cell.
    final StackPane button_Padding = new StackPane();
    // records the y pos of the last button press so that the add person dialog can be shown next to the cell.
    //final DoubleProperty buttonY = new SimpleDoubleProperty();

    public TicketButtonCell(Stage stage, TableView ticket_View)
    {
        button_Padding.setPadding(new Insets(3));
        button_Padding.getChildren().add(link_Button);
        link_Button.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                ticket_View.getSelectionModel().select(getTableRow().getIndex());
                Ticket current = (Ticket) ticket_View.getSelectionModel().getSelectedItem();
                URI uri;
                try
                {
                    uri = new URI("http://CityHelpDesk/tickets/list/single_ticket/" +  String.valueOf(current.getId()));
                    Desktop dt = Desktop.getDesktop();
                    dt.browse(uri.resolve(uri));
                }
                catch (URISyntaxException ex)
                {
                    System.err.println("URI SYNTAX WRONG");
                }
                catch (IOException ex)
                {
                    System.err.println("URI NOT FOUND");
                }

            }

        });
    }

    /**
     * places a button in the row only if the row is not empty.
     */
    @Override
    protected void updateItem(Boolean item, boolean empty)
    {
        super.updateItem(item, empty);
        if (!empty)
        {
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            setGraphic(button_Padding);
        }
        else
        {
            setGraphic(null);
        }
    }

}
