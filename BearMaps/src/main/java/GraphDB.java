import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.Collections;
import java.util.Iterator;
import java.util.Comparator;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Wraps the parsing functionality of the MapDBHandler as an example.
 * You may choose to add to the functionality of this class if you wish.
 * @author Alan Yao
 */
public class GraphDB {
    private HashMap<Long, Node> nodeMap;

    public HashMap<Long, Node> getNodeMap() {
        return nodeMap;
    }

    /**
     * Example constructor shows how to create and start an XML parser.
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        this.nodeMap = new HashMap<>();
        try {
            File inputFile = new File(dbPath);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            MapDBHandler maphandler = new MapDBHandler(this);
            saxParser.parse(inputFile, maphandler);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        Iterator<Map.Entry<Long, Node>> iterator = nodeMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, Node> entry = iterator.next();
            Node temp = entry.getValue();
            if (temp.getConnectionList().isEmpty()) {
                iterator.remove();
            }
        }
    }

    private double infinity = Double.MAX_VALUE;

    //find route using a*search
    public LinkedList<Node> findRoute(Map<String, Double> params) {
        double sLON = params.get("start_lon");
        double sLAT = params.get("start_lat");
        double eLON = params.get("end_lon");
        double eLAT = params.get("end_lat");

        Node startNode = findNode(sLON, sLAT);
        Node endNode = findNode(eLON, eLAT);

        Map<Node, RouteNode> routeMap = new HashMap<>();
        for (Node node : nodeMap.values()) {
            RouteNode route = new RouteNode(node);
            routeMap.put(node, route);
        }

        //initialize map
        for (Map.Entry<Node, RouteNode> entry : routeMap.entrySet()) {
            double distance = findDistance(entry.getKey(), endNode);
            entry.getValue().setHeuristic(distance);
            entry.getValue().setDistance(infinity);
        }

        //queue with comparator functionality
        Queue<RouteNode> queue = new PriorityQueue<>(
                new Comparator<RouteNode>() {
                    @Override
                    public int compare(RouteNode o1, RouteNode o2) {
                        double hashCode1 =  o1.getDistance() + o1.getHeuristic();
                        double hashCode2 =  o2.getDistance() + o2.getHeuristic();
                        if (hashCode1 > hashCode2) {
                            return 1;
                        } else if (hashCode1 < hashCode2) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                });

        RouteNode startRouteNode = routeMap.get(startNode);
        startRouteNode.setDistance(0);
        queue.add(startRouteNode);

        while (queue.size() > 0) {
            RouteNode currentRouteNode = queue.poll();
            Node curNode = currentRouteNode.getNode();

            if (curNode.equals(endNode)) {
                return buildPath(routeMap, startNode, endNode);
            }

            for (Connection connection : curNode.getConnectionList()) {
                Node node = connection.getNode();
                RouteNode routeNode = routeMap.get(node);

                double newDistance = findDistance(curNode, node)
                        + currentRouteNode.getDistance();
                if (newDistance < routeNode.getDistance()) {
                    queue.remove(routeNode);
                    routeNode.setPrev(curNode);
                    routeNode.setDistance(newDistance);
                    queue.add(routeNode);
                }
            }
        }
        return null;
    }

    //finds node closest to given params
    private Node findNode(double lon, double lat) {
        double minDistance = infinity;
        Node temp = null;

        for (Node node : nodeMap.values()) {
            //gets distance from start/end
            double curDistance = Math.sqrt(Math.pow(lon - node.getLon(), 2)
                    + Math.pow(lat - node.getLat(), 2));
            if (curDistance < minDistance) {
                temp = node;
                minDistance = curDistance;
            }
        }
        return temp;
    }

    private double findDistance(Node n1, Node n2) {
        double distance = Math.sqrt(Math.pow(n1.getLon() - n2.getLon(), 2)
                + Math.pow(n1.getLat() - n2.getLat(), 2));
        return distance;
    }

    //creates linkedlist of nodes
    private LinkedList<Node> buildPath(Map<Node, RouteNode> rtMap, Node startNode, Node endNode) {
        RouteNode routeNode = rtMap.get(endNode);
        Node pointer = routeNode.getNode();

        LinkedList<Node> path = new LinkedList<>();
        while (!startNode.equals(pointer)) {
            path.add(pointer);
            routeNode = rtMap.get(routeNode.getPrev());
            pointer = routeNode.getNode();
        }
        path.add(pointer);

        Collections.reverse(path);
        return path;
    }
}
