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
package spiceworks_archive_datastructures;

import java.util.ArrayList;

/**
 *
 * @author it.student
 */
public class Node
{

    String keyword;
    ArrayList<String> ticket_Identifiers;
    Node left;
    Node right;

    public Node(String keyword, String ticket_Identifier)
    {
        this.keyword = keyword;
        this.ticket_Identifiers = new ArrayList<>(1);
        this.ticket_Identifiers.add(ticket_Identifier);
        left = null;
        right = null;
    }

    public void addIdentifier(String ticket_ID)
    {
        this.ticket_Identifiers.add(ticket_ID);
    }

    public ArrayList<String> getIdentifiers()
    {
        return ticket_Identifiers;
    }

}
