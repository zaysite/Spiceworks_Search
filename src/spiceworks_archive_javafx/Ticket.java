/*
 * The MIT License
 *
 * Copyright 2016 Jordan Kahtava (it.student).
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
import javafx.beans.property.SimpleBooleanProperty;

/**
 *
 * This is a Data Model class used by the TableView control.
 *
 * @author Jordan Kahtava (it.student)
 */
public class Ticket
{

    private final SimpleStringProperty id;
    private final SimpleStringProperty summary;
    private final SimpleStringProperty description;
    private final SimpleStringProperty first_Name;
    private final SimpleStringProperty last_Name;
    private final SimpleStringProperty attachment_Name;
    private final SimpleBooleanProperty has_Attachment;

    public Ticket(String id, String summary, String description, String first_Name, String last_Name, String attachment_Name)
    {
        this.id = new SimpleStringProperty(id);
        this.summary = new SimpleStringProperty(summary);
        this.description = new SimpleStringProperty(description);
        this.first_Name = new SimpleStringProperty(first_Name);
        this.last_Name = new SimpleStringProperty(last_Name);
        this.attachment_Name = new SimpleStringProperty(attachment_Name);

        if (attachment_Name == null || attachment_Name.equalsIgnoreCase(""))
        {
            this.has_Attachment = new SimpleBooleanProperty(false);
        }
        else
        {
            this.has_Attachment = new SimpleBooleanProperty(true);
        }

    }

    public String getId()
    {
        return id.get();
    }

    public String getFirst_Name()
    {
        return first_Name.get();
    }

    public String getLast_Name()
    {
        return last_Name.get();
    }

    public String getSummary()
    {
        return summary.get();
    }

    public String getDescription()
    {
        return description.get();

    }

    public String getAttachmentName()
    {
        return attachment_Name.get();
    }

    public boolean hasAttachment()
    {
        return has_Attachment.get();
    }

}
