package com.fourplusone.bilkentmaps;

import java.util.ArrayList;

/**
 * Route class with methods for calculating a route between two Nodes and store it in properties
 * @author fourplusone
 * @version 08.05.2019
 */
public class Route
{
    //properties
    private static ArrayList<Node> passedNodes = new ArrayList<Node>();
    private static ArrayList<Route> basicRoutes = new ArrayList<Route>();
    private static ArrayList<Route> allRoutes = new ArrayList<Route>();
    private ArrayList<Node> routeNodes;
    private Node start;
    private Node destination;

    //constructors

    /**
     * Creates a route from specified start and destination points
     * @param start is the starting Node of route
     * @param destination is the end Node of route
     */
    public Route( Node start, Node destination)
    {
        routeNodes = new ArrayList<Node>();
        if ( start.isParentOf(destination))
        {
            routeNodes.add(start);
            routeNodes.add(destination);
        }
        this.start = start;
        this.destination = destination;
    }

    /**
     * Copy constructor
     */
    public Route( Route route)
    {
        routeNodes = new ArrayList<Node>();
        for ( Node node : route.getNodes())
        {
            routeNodes.add(node);
        }
        start = route.getStart();
        destination = route.getDestination();
    }

    //methods

    /**
     * String represantation of route
     * @return is represantation
     */
    public String toString()
    {
        return routeNodes.toString();
    }

    /**
     * Checks whether two routes are equal or not
     * @param route is the given route
     * @return result is true if given route is equal with this one
     */
    public boolean equals(Route route)
    {
        boolean result = true;
        if ( route.size() == this.size() )
        {
            for ( int i = 0 ; i < this.size(); i++)
            {
                if ( route.get(i) != this.get(i))
                {
                    result = false;
                }
            }
        }
        else
        {
            result = false;
        }
        return result;
    }

    /**
     * Checks whether this route has specified node
     * @param node is the specified node
     * @return is true if the route has that node
     */
    public boolean contains(Node node)
    {
        if ( routeNodes.contains(node))
        {
            return true;
        }
        return false;
    }

    /**
     * Gets the node in specified index
     * @param index is the specified index
     * @return is the wanted node
     */
    public Node get(int index)
    {
        return routeNodes.get(index);
    }

    /**
     * Returns how much nodes the route has
     * @return is the number of nodes in the route
     */
    public int size()
    {
        return routeNodes.size();
    }

    /**
     * Accessor method for start node of route
     * @return start is the start node
     */
    public Node getStart()
    {
        return start;
    }

    /**
     * Accessor method for ending node of route
     * @return destination is the ending node
     */
    public Node getDestination()
    {
        return destination;
    }

    /**
     * Accessor method for ArrayList of nodes in route
     * @return routeNodes is the ArrayList
     */
    public ArrayList<Node> getNodes()
    {
        return routeNodes;
    }

    /**
     * Removes a node in the specified index
     * @param index is the int value of index
     */
    private void remove( int index)
    {
        routeNodes.remove(index);
    }

    /**
     * Deletes all elements in the static ArrayLists passedNodes and possibleRoutes
     */
    private static void resetRouteData()
    {
        passedNodes.clear();
        basicRoutes.clear();
        allRoutes.clear();
    }

    /**
     * Recursive method that finds basic routes from start node to destination node
     * @param start is the starting Node
     * @param destination is the end Node
     * @return possibleRoutes is the ArrayList of basic routes
     */
    private static ArrayList<Route> findBasicRoutes(Node start, Node destination)
    {
        int isStuck = 0;
        for ( Node parent : start.getParents()) //Indicating whether all parent nodes of start are passed or not
        {
            if ( passedNodes.contains(parent) )
            {
                isStuck++;
            }
        }

        if ( isStuck >= start.parentNumber() ) //If all parent nodes are passed, which means stucked, return no route
        {
            return new ArrayList<Route>();
        }
        else if ( start.isParentOf(destination)) //If a parent of destination is found, return a route contains them
        {
            passedNodes.add(start);
            Route route = new Route(start,destination);
            basicRoutes.add(route);
            return basicRoutes;
        }
        else
        {
            passedNodes.add(start);
            for ( Node parent : start.getParents())
            {
                boolean isPassed = false;
                for ( Node element : passedNodes )
                {
                    if ( element == parent )
                    {
                        isPassed = true;
                    }
                }

                if ( !isPassed ) //If the parent which will be controlled is not passed before
                {
                    ArrayList<Route> temporaryRoutes = Route.findBasicRoutes(parent, destination);
                    //Find all possible routes from that parent to destination

                    if ( temporaryRoutes.size() == 0)
                    {
                    }
                    else if ( !temporaryRoutes.get(temporaryRoutes.size() - 1).contains(start))
                    {
                        boolean firstNodeisEqual = false;
                        int i = 0;
                        while ( !firstNodeisEqual && i < basicRoutes.size() - 1)
                        {
                            if ( basicRoutes.get(i).get(0).equals(basicRoutes.get(i + 1).get(0)))
                            {
                                firstNodeisEqual = true;
                            }
                            i++;
                        }
                        if ( !firstNodeisEqual)
                        {
                            temporaryRoutes.get(temporaryRoutes.size() - 1).addNodeToStart(start);
                        }
                        else
                        {
                            for ( Route route : temporaryRoutes)
                            {
                                route.addNodeToStart(start);
                            }
                        }
                    }
                }
            }
            return basicRoutes;
        }
    }

    /**
     * Find all routes by getting basic routes from getBasicRoutes() method and creating new routes from those routes
     * @return result is the all possible routes from start node to destination node
     */
    private ArrayList<Route> findAllRoutes()
    {
        Route.findBasicRoutes(start, destination);
        ArrayList<Route> routes = new ArrayList<Route>();

        for ( Route route : basicRoutes)
        {
            Route.shortenRoute(route);
        }

        for ( Route route : allRoutes)
        {
            boolean exists = false;
            for ( Route element : routes)
            {
                if ( element.equals(route))
                {
                    exists = true;
                }
            }
            if( !exists)
            {
                routes.add(route);
            }
        }
        routes.addAll(basicRoutes);
        Route.resetRouteData();
        return routes;
    }

    /**
     * Returns the shortest route
     * @return result is shortest route
     */
    public Route findShortestRoute()
    {
        ArrayList<Route> routes = this.findAllRoutes();
        Route smallest = routes.get(0);
        for ( Route route : routes)
        {
            if ( route.getLength() < smallest.getLength())
            {
                smallest = route;
            }
        }
        routeNodes.addAll(smallest.getNodes());
        return smallest;
    }

    /**
     * Recursive method that shortens a route if possible
     * @param route is the route that will be shortened
     * @return result is the ArrayList of all possible routes that is generated from parameter
     */
    private static ArrayList<Route> shortenRoute(Route route)
    {
        boolean canbeShortened = false;

        for ( int i = 0; i < route.size(); i++)
        {
            for ( int j = route.size() - 1; j > i + 1; j--)
            {
                if ( route.get(j).isParentOf(route.get(i)))
                {
                    canbeShortened = true;
                }
            }
        }
        if ( !canbeShortened)
        {
            return new ArrayList<Route>();
        }
        else
        {
            ArrayList<Route> routes = new ArrayList<Route>();
            for ( int i = 0; i < route.size(); i++)
            {
                for ( int j = route.size() - 1; j > i + 1; j--)
                {
                    if ( route.get(j).isParentOf(route.get(i)))
                    {
                        Route temporaryRoute = new Route(route);
                        for ( int k = i + 1; k < j; k++)
                        {
                            temporaryRoute.remove(i + 1);
                        }
                        allRoutes.addAll(shortenRoute(temporaryRoute));
                        allRoutes.add(temporaryRoute);
                    }
                }
            }
            return routes;
        }
    }

    /**
     * Adds node to the beginning of this route
     * @param node is the node which will be added
     */
    private void addNodeToStart( Node node)
    {
        routeNodes.add(0, node);
    }

    /**
     * Gets the length of route by looking the distance between nodes
     * @return result is the total length
     */
    public double getLength()
    {
        double result = 0;
        for ( int i = 0; i < routeNodes.size() - 1; i++)
        {
            result = result + Node.length(routeNodes.get(i), routeNodes.get(i + 1));
        }
        return result;
    }
}
