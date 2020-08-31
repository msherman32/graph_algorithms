package prüferTreeEncoding;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

import core.Edge;
import core.Node;

public class PrüferTreeGenerator {

    public static List<Edge> createCompleteGraph(int n) {
        ArrayList<Edge> result = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            for (int j = i + 1; j <= n; j++) {
                result.add(new Edge(new core.Node(i), new core.Node(j)));
            }
        }
        return result;
    }

    public static List<Edge> createCompleteBipartiteGraph(int m, int n) {
        ArrayList<Edge> result = new ArrayList<>();
        for (int i = 1; i <= m; i++) {
            for (int j = m + 1; j <= m + n; j++) {
                result.add(new Edge(new core.Node(i), new core.Node(j)));
            }
        }
        return result;
    }

    public static List<Edge> generateSnowflake(int i) {
        if (i <= 2) {
            throw new RuntimeException("i must be greater than 2");
        }
        ArrayList<Edge> result = new ArrayList<>();
        for (int x = 2; x <= i; x++) {
            result.add(new Edge(new core.Node(1), new core.Node(x)));
        }
        return result;
    }

    public static PrüferTree generatePrüferTree(List<Node> nodes) {
        if (nodes == null) {
            throw new NullPointerException("list of nodes is null");
        }
        int numNodes = nodes.size() + 2;

        HashSet<Node> uniqueNodes = new HashSet<>();
        for (Node node : nodes) {
            if (node.getVal() > numNodes) {
                throw new IndexOutOfBoundsException("core.Node " + node + " has value "
                        + node.getVal() + " that is greater than" + numNodes);
            }
            uniqueNodes.add(node);
        }

        PriorityQueue<Node> leaves = new PriorityQueue<>();
        int i = 1;
        while (i <= numNodes) {
            Node potentialLeaf = new Node(i);
            if (!uniqueNodes.contains(potentialLeaf)) {
                leaves.add(potentialLeaf);
            }
            i++;
        }

        List<Node> mutableCopy = new ArrayList<>(nodes);
        List<Edge> edges = new ArrayList<>();
        Iterator<Node> iterator = nodes.iterator();

        while (!mutableCopy.isEmpty()) {
            Node minLeaf = leaves.poll();
            Node destLeaf = iterator.next();
            edges.add(new Edge(minLeaf, destLeaf)); //add edge from leaf to parent
            mutableCopy.remove(destLeaf);
            if (!mutableCopy.contains(destLeaf)) {
                leaves.add(destLeaf);
            }
        }

        Node penUltLeaf = leaves.poll();
        Node ultLeaf = leaves.poll();
        edges.add(new Edge(penUltLeaf, ultLeaf)); //add the final connection

        return new PrüferTree(edges);
    }

    public static void main(String[] args) {
        System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        //        int[] prüferCode = {4,9,4,9,5,7,7,9,9};
        int[] prüferCode = {3, 1, 4, 9, 7, 10, 4, 3};
        //        int[] prüferCode = {3,3,5};
        //        int[] prüferCode = {1, 2};
        //        int[] prüferCode = {1, 2, 28};
        //        int[] prüferCode = {1, 1, 1, 1, 6, 5};
        //        int[] prüferCode = {4,4,4,4,4,5,4,4,4,4,4,4,4,5,5,5,6};

        List<Node> nodeList = new ArrayList<>();
        for (int i : prüferCode) {
            nodeList.add(new Node(i));
        }

        PrüferTree prüferTree = generatePrüferTree(nodeList);
        prüferTree.displayGraph();
        System.out.println(prüferTree);

    }
}
