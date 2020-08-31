package playground;

import java.awt.Color;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

//todo: make it possible to remove and add nodes using heap algorithms?
public class Environment {

    private static Color startColor = new Color(139, 69, 19);
    private static int startSize = 30;

    public static Graph generateFullBinaryTree(int numLevels) {
        if (numLevels < 0) {
            throw new RuntimeException("numLevels must be positive");
        }
        if (numLevels == 0) {
            return new SingleGraph("Empty Graph");
        }
        Graph graph = new SingleGraph(String.format("Binary Tree with %d levels", numLevels));
        graph.display();
        graph.addAttribute("ui.stylesheet", "url('stylesheet.css')");
        int i = 1;
        for (int level = 0; level < numLevels; level++) {
            int numNodes = (int) Math.pow(2, level);
            int j = 0;
            while (j < numNodes) {
                String name = Integer.toString(i);
                Node child = graph.addNode(name);
                Color edgeColor;
                if (level == 0) {
                    child.setAttribute("ui.color", startColor);
                    edgeColor = startColor;
                } else if (level == numLevels - 1) {
                    child.setAttribute("ui.color", Color.RED);
                    edgeColor = Color.RED;
                } else {
                    int range = numLevels - 2;
                    int redInc = startColor.getRed() / range;
                    int blueInc = startColor.getBlue() / range;
                    int greenInc = (255 - startColor.getGreen()) / range;

                    Color color = new Color(
                            startColor.getRed() - redInc * level,
                            startColor.getGreen() + greenInc * level,
                            startColor.getBlue() - blueInc);
                    edgeColor = color;
                    child.setAttribute("ui.color", color);
                }

                if (i == 1) {
                    child.setAttribute("ui.label", "root");
                } else {
                    String parentName = Integer.toString(i / 2);
                    Node parent = graph.getNode(parentName);
                    child.setAttribute("ui.label", name);
                    graph.addEdge(String.format("%s-->%s", parent.getId(), child.getId()), parent, child)
                            .setAttribute("ui.color", edgeColor);
                }

                int sizeInc = startSize / numLevels;
                child.setAttribute("ui.size", startSize - sizeInc * level);

                j++;
                i++;
            }
            sleep();
        }
        if (graph.getNodeCount() != Math.pow(2, numLevels) - 1) {
            throw new RuntimeException("Number of nodes is not equals to 2^(numLevels) - 1");
        }
        return graph;
    }

    protected static void sleep() {
        try { Thread.sleep(1000); } catch (Exception e) {}
    }

    public static void main(String[] args) {
//        for (int i = 0; i < 5; i++) {
//            System.out.println(i);
//        }
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        Graph graph = generateFullBinaryTree(4);
//        graph.display();
    }
}
