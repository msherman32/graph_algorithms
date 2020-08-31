package firstFitAlgorithm;

import java.awt.Color;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import core.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import static prüferTreeEncoding.PrüferTreeGenerator.createCompleteBipartiteGraph;
import static prüferTreeEncoding.PrüferTreeGenerator.createCompleteGraph;

/**
 * given a graph, initialize each node's color to black. loop through each node
 * look at each nodes neighbors and add distinct colors to a set
 * go through a list of colors and color currentNode with the first color not contained in the set break the loop.
 */
public class FirstFitGenerator {

    private Graph graph;
    private List<String> vertexIDs = new ArrayList<>(); //instead of using a set, use a list to maintain order (default ordering)
    private List<Color> colors = new ArrayList<>();
    private Map<Color, Character> labels = new HashMap<>();

    public Graph getGraph() {
        return graph;
    }

    public List<Color> getColors() {
        return colors;
    }

    public Map<Color, Character> getLabels() {
        return labels;
    }

    private Color generateNewColor(int increment, int index) {
        int red = index * increment;
        int green = (vertexIDs.size() - index) * increment;
        int blue = 0; //grey?

        Color newColor = new Color(red, green, blue);
        if (newColor.equals(Color.BLACK)) {//make sure black is never represented as a color
            newColor = new Color(0, 0, vertexIDs.size() - index * increment);
        }
        return newColor;
    }

    private Character generateNewCharacter(int index) {
        int asciiAlphabetStart = 65; //where 'A' begins in Ascii
        if (index >= 26) { //greater than the size of the alphabet
            int offset = 6;
            int asciiTableSize = 128;
            return (char) ((asciiAlphabetStart + index + offset) % asciiTableSize); //todo: loop around to the front of ascii table
        } else {
            return (char) (asciiAlphabetStart + index);
        }
    }

    public FirstFitGenerator(List<Edge> edges) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime currentTime = LocalDateTime.now();
        this.graph = new SingleGraph("Graph with First Fit Coloring: " + dtf.format(currentTime));
        graph.addAttribute("ui.stylesheet", "url('stylesheet.css')");

        for (Edge edge : edges) {
            if (edge.getSrc().getVal() == 0) {
                throw new RuntimeException(
                        String.format("Node %s has value %s (and we want to start with 1)", edge.getSrc(), edge.getSrc().getVal()));
            }
            String srcId = edge.getSrc().toString();
            if (!vertexIDs.contains(srcId)) {
                vertexIDs.add(srcId);
                graph.addNode(srcId).addAttribute("ui.color", Color.BLACK);
            }
            if (edge.getDest().getVal() == 0) {
                throw new RuntimeException(
                        String.format("Node %s has value %s (and we want to start with 1)", edge.getDest(), edge.getDest().getVal()));
            }
            String destId = edge.getDest().toString();
            if (!vertexIDs.contains(destId)) {
                vertexIDs.add(destId);
                graph.addNode(destId).addAttribute("ui.color", Color.BLACK);
            }

            graph.addEdge(edge.getSrc() + ":" + edge.getDest(), srcId, destId);
        }
        graph.display();
    }

    /**
     * generates the first fit coloring in the default order of the vertices set
     */
    public void generate() {
        if (vertexIDs.size() == 0) {
            throw new RuntimeException("there are no nodes in this graph");
        }
        int increment = 255 / vertexIDs.size();
        for (int i = 0; i < vertexIDs.size(); i++) {
            Color newColor = generateNewColor(increment, i);
            colors.add(newColor);
            Character newCharacter = generateNewCharacter(i);
            labels.put(newColor, newCharacter);
        }
        sleep();
        for (String vertexID : vertexIDs) {
            Node curr = graph.getNode(vertexID);

            Iterator<Node> neighbors = curr.getNeighborNodeIterator();
            HashSet<Color> neighborColors = new HashSet<>();
            while (neighbors.hasNext()) {
                Color neighborColor = neighbors.next().getAttribute("ui.color");
                if (!neighborColor.equals(Color.BLACK)) {
                    neighborColors.add(neighborColor);
                }
            }
            for (Color color : colors) {
                if (!neighborColors.contains(color)) {
                    curr.setAttribute("ui.color", color); //or add attribute?
                    curr.setAttribute("ui.label", vertexID + ":" + labels.get(color)); //todo: add documentation for how nodes appear in graph
                    sleep();
                    break;
                }
            }
        }
//        graph.display();
    }

    /**
     * generates the first fit coloring in the order passed in
     * @param order the order of the vertices to be visited
     */
    private void generate(List<core.Node> order) {
        if (vertexIDs.size() == 0) {
            throw new RuntimeException("there are no nodes in this graph");
        }
        if (vertexIDs.size() != order.size()) {
            throw new RuntimeException("The order provided does not contain the same number of vertices as the graph");
        }
        List<String> ordering = new ArrayList<>();
        for (core.Node node : order) {
            if (!vertexIDs.contains(node.toString())) {
                throw new RuntimeException(String.format("Node %s is not contained in the set of vertices", node.toString()));
            }
            ordering.add(node.toString());
        }

        int increment = 255 / vertexIDs.size();
        for (int i = 0; i < vertexIDs.size(); i++) {
            Color newColor = generateNewColor(increment, i);
            colors.add(newColor);
            Character newCharacter = generateNewCharacter(i);
            labels.put(newColor, newCharacter);
        }

        for (String id : ordering) {
            Node curr = graph.getNode(id);

            Iterator<Node> nodeNeighbors = curr.getNeighborNodeIterator();
            HashSet<Color> neighborColors = new HashSet<>();
            while (nodeNeighbors.hasNext()) {
                Color neighborColor = nodeNeighbors.next().getAttribute("ui.color");
                if (!neighborColor.equals(Color.BLACK)) {
                    neighborColors.add(neighborColor);
                }
            }
            for (Color color : colors) {
                if (!neighborColors.contains(color)) {
                    curr.setAttribute("ui.color", color); //or add attribute?
                    curr.setAttribute("ui.label", id + ":" + labels.get(color));
                    sleep();
                    break;
                }
            }
        }
//        graph.display();
    }

    public static void main(String[] args) {
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
//        testWithEdges(playground());
//                testWithEdges(createCompleteBipartiteGraph(4, 4));
                testWithEdges(createCompleteGraph(10));
//                int n = 10;
//                testWithNumber(createSimpleGraph(n), null);
        //        testWithNumber(createSimpleGraph(n),createReverseOrder(n));
//                testWithNumber(createSimpleGraph(n),createRandomOrder(n));
    }

    private static void testWithEdges(List<Edge> edges) {
        FirstFitGenerator firstFitGenerator = new FirstFitGenerator(edges);
        firstFitGenerator.generate();
    }

    private static void testWithNumber(List<Edge> edges, List<core.Node> order) {
        FirstFitGenerator firstFitGenerator = new FirstFitGenerator(edges);
        if (order == null) {
            firstFitGenerator.generate();
            return;
        }
        firstFitGenerator.generate(order);
    }

    private static List<core.Node> createRandomOrder(int n) {
        Random rnd = new Random();
        ArrayList<Integer> order = new ArrayList<>();
        while (order.size() < n) {
            int random = rnd.nextInt(n) + 1;
            if (!order.contains(random)) {
                order.add(random);
            }
        }
        List<core.Node> randomOrder = new ArrayList<>();
        for (Integer num : order) {
            randomOrder.add(new core.Node(num));
        }
        System.out.println(order);
        return randomOrder;
    }

    private static List<core.Node> createReverseOrder(int n) {
        List<core.Node> order = new ArrayList<>();
        for (int i = n; i > 0; i--) {
            order.add(new core.Node(i));
        }
        return order;
    }

    private static List<Edge> createSimpleGraph(int n) {
        if (n < 4) {
            throw new RuntimeException("n must not be less than 4");
        }
        List<Edge> result = new ArrayList<>();
        for (int i = 1; i < n; i++) {
            result.add(new Edge(new core.Node(i), new core.Node(i + 1)));
        }

        result.add(new Edge(new core.Node(n / 2 - 1), new core.Node(n / 2 + 1)));
        result.add(new Edge(new core.Node(n / 2 - 2), new core.Node(n - 1)));
        result.add(new Edge(new core.Node(n / 2), new core.Node(n - 2)));

        return result;
    }

    private static List<Edge> playground() {
        int[] nodes = {1, 2, 3, 4, 5, 6, 7, 8};

        List<Edge> edges = new ArrayList<>();
        for (int i = 1; i <= nodes.length; i++) {
            Edge edge = new Edge(new core.Node(i), new core.Node(i + 1));
            edges.add(edge);
        }

        edges.add(new Edge(new core.Node(1), new core.Node(5)));
        edges.add(new Edge(new core.Node(1), new core.Node(3)));

        edges.add(new Edge(new core.Node(2), new core.Node(5)));
        edges.add(new Edge(new core.Node(2), new core.Node(4)));
        edges.add(new Edge(new core.Node(3), new core.Node(5)));
        edges.add(new Edge(new core.Node(1), new core.Node(4)));
        edges.add(new Edge(new core.Node(1), new core.Node(7)));
        edges.add(new Edge(new core.Node(1), new core.Node(8)));
        return edges;
    }

    protected static void sleep() {
        try { Thread.sleep(1500); } catch (Exception e) {}
    }
}