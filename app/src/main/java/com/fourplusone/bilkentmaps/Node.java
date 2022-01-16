package com.fourplusone.bilkentmaps;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;

/**
 * A class that represents a node with id, coordinate and its parents
 * @author fourplusone
 * @version 08.05.2019
 */
public class Node
{
    //properties
    private LatLng coordinate;
    private ArrayList<Node> parents;
    private int id;

    //constructors

    /**
     * Constructor with id and a coordinate of node
     * @param id is the id
     * @param coordinate is the Coordinate
     */
    public Node(int id, LatLng coordinate)
    {
        this.coordinate = coordinate;
        parents = new ArrayList<Node>();
        this.id = id;
    }

    //methods

    /**
     * Accessor method of X coordinate
     * @return is x coordinate
     */
    public double getX()
    {
        return coordinate.getLatitude();
    }

    /**
     * Accessor method of Y coordinate
     * @return is y coordinate
     */
    public double getY()
    {
        return coordinate.getLongitude();
    }

    /**
     * Accessor method of parents array
     * @return is the array containing parent nodes
     */
    public ArrayList<Node> getParents()
    {
        return parents;
    }

    /**
     * Returns the number of parents
     * @return is the number
     */
    public int parentNumber()
    {
        return parents.size();
    }

    /**
     * String represantation of Node
     * @return is id of Node
     */
    public String toString()
    {
        return "" + id;
    }

    /**
     * Checks whether specified node is parent of this one or not
     * @param node is the specified node
     * @return result is true if specified node is parent of this one
     */
    public boolean isParentOf( Node node)
    {
        boolean result = false;
        for ( Node element : this.getParents() )
        {
            if ( element == node)
            {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * Adds bunch of parents to the Node
     * @param nodes is parents that will be added
     */
    public void addParent( Node... nodes)
    {
        for ( Node node : nodes)
        {
            parents.add(node);
        }
    }

    /**
     * A static method that calculates the distance between two nodes
     * @param node1 is the first node
     * @param node2 is the second node
     * @return result is the distance value
     */
    public static double length(Node node1, Node node2)
    {
        double result = Math.pow(Math.abs(node1.getX() - node2.getX()),2);
        result = result + Math.pow(Math.abs(node1.getY() - node2.getY()),2);
        result = Math.sqrt(result);
        return result;
    }
}
