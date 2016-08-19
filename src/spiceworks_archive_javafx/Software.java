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
    private final SimpleStringProperty id;
    private final SimpleStringProperty category;
    private final SimpleStringProperty serialNumber;
    private final SimpleStringProperty libraryID;
    private final SimpleStringProperty createdOn;
    private final SimpleStringProperty note;
    private final SimpleStringProperty status;
    private final SimpleStringProperty checkedOutBy;
    private final SimpleStringProperty checkedOutAt;
    private final SimpleStringProperty lastCheckedOutBy;
    private final SimpleStringProperty lastCheckedOutAt;

    public Software(String id, String category, String serialNumber, String libraryID, String createdOn, String note, String status, String checkedOutBy, String checkedOutAt, String lastCheckedOutBy, String lastCheckedOutAt)
    {
        this.id = new SimpleStringProperty(id);
        this.category = new SimpleStringProperty(category);
        this.serialNumber = new SimpleStringProperty(serialNumber);
        this.libraryID = new SimpleStringProperty(libraryID);
        this.createdOn = new SimpleStringProperty(createdOn);
        this.note = new SimpleStringProperty(note);
        this.status = new SimpleStringProperty(status);
        this.checkedOutBy = new SimpleStringProperty(checkedOutBy);
        this.checkedOutAt = new SimpleStringProperty(checkedOutAt);
        this.lastCheckedOutBy = new SimpleStringProperty(lastCheckedOutBy);
        this.lastCheckedOutAt = new SimpleStringProperty(lastCheckedOutAt);
    }

    public String getID()
    {
        return id.get();
    }
    public String getCategory()
    {
        return category.get();
    }

    public String getSerialNumber()
    {
        return serialNumber.get();
    }

    public String getLibraryID()
    {
        return libraryID.get();
    }

    public String getCreatedOn()
    {
        return createdOn.get();
    }

    public String getNote()
    {
        return note.get();
    }

    public String getStatus()
    {
        return status.get();
    }

    public String getCheckedOutBy()
    {
        return checkedOutBy.get();
    }

    public String getCheckedOutAt()
    {
        return checkedOutAt.get();
    }

    public String getLastCheckedOutBy()
    {
        return lastCheckedOutBy.get();
    }

    public String getLastCheckedOutAt()
    {
        return lastCheckedOutAt.get();
    }

    public void setId(String id)
    {
        this.id.set(id);
    }
    public void setCategory(String category)
    {
        this.category.set(category);

    }

    public void setSerialNumber(String serialNumber)
    {
        this.serialNumber.set(serialNumber);

    }

    public void setLibraryID(String libraryID)
    {
        this.libraryID.set(libraryID);

    }

    public void setCreatedOn(String createdOn)
    {
        this.createdOn.set(createdOn);

    }

    public void setNote(String note)
    {
        this.note.set(note);

    }

    public void setStatus(String status)
    {
        this.status.set(status);

    }

    public void setCheckedOutBy(String checkedOutBy)
    {
        this.checkedOutBy.set(checkedOutBy);

    }

    public void setCheckedOutAt(String checkedOutAt)
    {
        this.checkedOutAt.set(checkedOutAt);

    }

    public void setLastCheckedOutBy(String lastCheckedOutBy)
    {
        this.lastCheckedOutBy.set(lastCheckedOutBy);

    }

    public void setLastCheckedOutAt(String lastCheckedOutAt)
    {
        this.lastCheckedOutAt.set(lastCheckedOutAt);
    }

    public SimpleStringProperty idProperty()
    {
        return id;
    }
    public SimpleStringProperty categoryProperty()
    {
        return category;
    }

    public SimpleStringProperty serialNumberProperty()
    {
        return serialNumber;
    }

    public SimpleStringProperty libraryIDProperty()
    {
        return libraryID;
    }

    public SimpleStringProperty createdOnProperty()
    {
        return createdOn;
    }

    public SimpleStringProperty noteProperty()
    {
        return note;
    }

    public SimpleStringProperty statusProperty()
    {
        return status;
    }

    public SimpleStringProperty checkedOutByProperty()
    {
        return checkedOutBy;
    }

    public SimpleStringProperty checkedOutAtProperty()
    {
        return checkedOutAt;
    }

    public SimpleStringProperty lastCheckedOutByProperty()
    {
        return lastCheckedOutBy;
    }

    public SimpleStringProperty lastCheckedOutAtProperty()
    {
        return lastCheckedOutAt;
    }

}
