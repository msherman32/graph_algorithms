package prüferTreeEncoding;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

import firstFitAlgorithm.FirstFitGenerator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.Graphs;

import static playground.Environment.generateFullBinaryTree;
import static prüferTreeEncoding.PrüferTreeGenerator.generatePrüferTree;
import static prüferTreeEncoding.PrüferTreeGenerator.generateSnowflake;

public class PrüferCodeGenerator {

    public static ArrayList<Integer> generatePrüferCode(Graph graph) { //assuming that graph is a tree
        if (graph.getNodeCount() <= 2) {
            throw new RuntimeException("Graph must have more than 2 leaves. Cannot generate Prüfer Code");
        }
        Graph myGraph = Graphs.clone(graph);
        ArrayList<Integer> prüferCode = new ArrayList<>();
        PriorityQueue<core.Node> nodePriorityQueue = new PriorityQueue<>(); //order the nodes from min to max (based on their val)
        while (myGraph.getNodeCount() > 2) {
            for (Node node : myGraph.getNodeSet()) {
                String label = node.getAttribute("ui.label");
                core.Node nd = new core.Node(Integer.parseInt(label));
                if (node.getDegree() == 1 && !nodePriorityQueue.contains(nd)) { //node.isLeaf()
                    nodePriorityQueue.add(nd);
                }
            }
            core.Node minNode = nodePriorityQueue.poll();
            Node leaf = myGraph.getNode(Integer.toString(minNode.getVal()));
            Iterator neighbors = leaf.getNeighborNodeIterator();
            int i = 0;
            while (neighbors.hasNext()) {
                if (i > 0) { //checking that the graph is in fact a tree
                    throw new RuntimeException(String.format("Leaf %d has more than one parent", minNode.getVal()));
                }
                Node parent = (Node) neighbors.next();
                String label = parent.getAttribute("ui.label");
                prüferCode.add((Integer.parseInt(label)));
                myGraph.removeNode(leaf);
                i++;
            }
        }
        if (prüferCode.size() != graph.getNodeCount() - 2) {
            throw new RuntimeException(String.format("Prüfer Code has length [%d] but should have length [%d] (number of nodes - 2)",
                    prüferCode.size(), graph.getNodeCount() - 2));
        }
        return prüferCode;
    }

    public static void main(String[] args) {
        System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        int[] prüferCode = {3, 1, 4, 9, 7, 10, 4, 3};
        //        int[] prüferCode = {4,9,4,9,5,7,7,9,9};
        List<core.Node> nodeList = new ArrayList<>();
        for (int i : prüferCode) {
            nodeList.add(new core.Node(i));
        }
        PrüferTree prüferTree = generatePrüferTree(nodeList);
        //        PrüferTree prüferTree = new PrüferTree(generateSnowflake(10));
        Graph graph = prüferTree.getGraph();
        ArrayList generatePrüferCode = generatePrüferCode(graph);
        prüferTree.displayGraph();
        System.out.println("Prüfer Code: " + generatePrüferCode);
    }
}
