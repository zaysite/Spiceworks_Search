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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;

/**
 * THIS WAS NOT CREATED BY JORDAN KAHTAVA
 *
 * THIS CODE WAS FOUND AT:
 * http://algorithms.tutorialhorizon.com/binary-search-tree-complete-implementation/
 *
 * WHICH USED CODE FROM:
 * https://gist.github.com/thmain/449545d18617a670c68f#file-binarysearchtree-java
 *
 * WHICH WAS CREATED BY: thmain
 *
 * @author thmain
 *
 * THE CODE WAS MODIFIED BY: JORDAN KAHTAVA MODIFICATION: Converted to use
 * Strings and store id numbers
 */
public class BinarySearchTree
{

    public static Node root;

    public BinarySearchTree()
    {
        this.root = null;
    }
    public Node getRoot()
    {
        return this.root;
    }
    
    public Node find(String id)
    {
        Node current = root;
        Node nodes_Found = null;

        while (current != null)
        {
          
            switch (current.keyword.compareTo(id))
            {
                case 0:
                    nodes_Found = current;
                    //nodes_Found.add(current);
                    //return nodes_Found;
                    current = null;
                    break;
                case 1:
                    current = current.left;
                    break;
                default:
                    current = current.right;
                    break;
            }
        }
        return nodes_Found;
    }

    public boolean delete(String id)
    {
        Node parent = root;
        Node current = root;
        boolean isLeftChild = false;
        while (!current.keyword.equalsIgnoreCase(id))
        {
            parent = current;
            if (current.keyword.compareTo(id) == 1)
            {
                isLeftChild = true;
                current = current.left;
            }
            else
            {
                isLeftChild = false;
                current = current.right;
            }
            if (current == null)
            {
                return false;
            }
        }
        //if i am here that means we have found the node
        //Case 1: if node to be deleted has no children
        if (current.left == null && current.right == null)
        {
            if (current == root)
            {
                root = null;
            }
            if (isLeftChild == true)
            {
                parent.left = null;
            }
            else
            {
                parent.right = null;
            }
        }
        //Case 2 : if node to be deleted has only one child
        else if (current.right == null)
        {
            if (current == root)
            {
                root = current.left;
            }
            else if (isLeftChild)
            {
                parent.left = current.left;
            }
            else
            {
                parent.right = current.left;
            }
        }
        else if (current.left == null)
        {
            if (current == root)
            {
                root = current.right;
            }
            else if (isLeftChild)
            {
                parent.left = current.right;
            }
            else
            {
                parent.right = current.right;
            }
        }
        else if (current.left != null && current.right != null)
        {

            //now we have found the minimum element in the right sub tree
            Node successor = getSuccessor(current);
            if (current == root)
            {
                root = successor;
            }
            else if (isLeftChild)
            {
                parent.left = successor;
            }
            else
            {
                parent.right = successor;
            }
            successor.left = current.left;
        }
        return true;
    }

    public Node getSuccessor(Node deleleNode)
    {
        Node successsor = null;
        Node successsorParent = null;
        Node current = deleleNode.right;
        while (current != null)
        {
            successsorParent = successsor;
            successsor = current;
            current = current.left;
        }
        //check if successor has the right child, it cannot have left child for sure
        // if it does have the right child, add it to the left of successorParent.
//		successsorParent
        if (successsor != deleleNode.right)
        {
            successsorParent.left = successsor.right;
            successsor.right = deleleNode.right;
        }
        return successsor;
    }

    public void insert(String id, String ticket_Identifier)
    {
        Node newNode = new Node(id, ticket_Identifier);
        if (root == null)
        {
            root = newNode;
            return;
        }
        Node current = root;
        Node parent = null;
        while (true)
        {
            parent = current;
            switch (id.compareTo(current.keyword))
            {
                case 0:
                    current.addIdentifier(ticket_Identifier);
                    return;

                case -1:
                    current = current.left;
                    if (current == null)
                    {
                        parent.left = newNode;
                        return;
                    }
                    break;
                default:
                    current = current.right;
                    if (current == null)
                    {
                        parent.right = newNode;
                        return;
                    }
                    break;
            }
        }
    }

    public void display(Node root)
    {
        if (root != null)
        {
            display(root.left);
            System.out.print(root.keyword + ": ");
            for(int i=0; i< root.getIdentifiers().size(); i++)
            {
                System.out.print(root.getIdentifiers().get(i) + " , ");
            }
            System.out.println();
            display(root.right);
        }
    }
    
    private static void writeToFile(String content, String file_Location)
    {
        try
        {

            File file = new File(file_Location);

            // if file doesnt exists, then create it
            if (!file.exists())
            {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            try (BufferedWriter buffered_writer = new BufferedWriter(fw))
            {
                buffered_writer.write(content);
                
            }

        } catch (Exception e)
        {
        }
    }


  

}


