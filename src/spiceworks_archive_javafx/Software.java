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

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * This is a Data Model class used by the TableView control.
 *
 * @author Jordan Kahtava (it.student)
 */
public class Software
{

    private final SimpleStringProperty category;
    private final SimpleStringProperty serial_Number;
    private final SimpleStringProperty library_ID;
    private final SimpleStringProperty created_on;
    private final SimpleStringProperty note;
    private final SimpleStringProperty status;
    private final SimpleStringProperty checked_out_by;
    private final SimpleStringProperty checked_out_at;
    private final SimpleStringProperty last_checked_out_by;
    private final SimpleStringProperty last_checked_out_at;

    public Software( String category, String serial_Number, String library_ID, String created_on, String note, String status, String checked_out_by, String checked_out_at, String last_checked_out_by, String last_checked_out_at)
    {
        this.category = new SimpleStringProperty(category);
        this.serial_Number = new SimpleStringProperty(serial_Number);
        this.library_ID = new SimpleStringProperty(library_ID);
        this.created_on = new SimpleStringProperty(created_on);
        this.note = new SimpleStringProperty(note);
        this.status = new SimpleStringProperty(status);
        this.checked_out_by = new SimpleStringProperty(checked_out_by);
        this.checked_out_at = new SimpleStringProperty(checked_out_at);
        this.last_checked_out_by = new SimpleStringProperty(last_checked_out_by);
        this.last_checked_out_at = new SimpleStringProperty(last_checked_out_at);
    }

    public String getCategory()
    {
        return category.get();
    }

    public String getSerial_Number()
    {
        return serial_Number.get();
    }

    public String getLibrary_ID()
    {
        return library_ID.get();
    }

    public String getCreated_on()
    {
        return created_on.get();
    }

    public String getNote()
    {
        return note.get();
    }

    public String getStatus()
    {
        return status.get();
    }

    public String getChecked_out_by()
    {
        return checked_out_by.get();
    }

    public String getChecked_out_at()
    {
        return checked_out_at.get();
    }

    public String getLast_checked_out_by()
    {
        return last_checked_out_by.get();
    }

    public String getLast_checked_out_at()
    {
        return last_checked_out_at.get();
    }

}
