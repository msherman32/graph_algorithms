package prüferTreeEncoding;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import core.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

//Source: http://graphstream-project.org/
//todo: refactor edges list into graph
public class PrüferTree {

    private Graph graph;

    public PrüferTree(List<Edge> edges) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime currentTime = LocalDateTime.now();
        this.graph = new SingleGraph("Prüfer Tree: " + dtf.format(currentTime));
        graph.addAttribute("ui.stylesheet", "url('stylesheet.css')");

        Set<String> vertices = new HashSet<>();

        for (Edge edge : edges) {
            if (!vertices.contains(edge.getSrc().toString())) {
                vertices.add(edge.getSrc().toString());
                graph.addNode(edge.getSrc().toString());
            }
            if (!vertices.contains(edge.getDest().toString())) {
                vertices.add(edge.getDest().toString());
                graph.addNode(edge.getDest().toString());
            }

            graph.addEdge(edge.getSrc() + ":" + edge.getDest(),
                    edge.getSrc().toString(),
                    edge.getDest().toString());
        }

        for (String id : vertices) {
            graph.getNode(id).setAttribute("ui.label", id);
        }
    }

    public Graph getGraph() {
        return graph;
    }

    public void displayGraph() {
        graph.display();
    }

    @Override
    public String toString() {
        return graph.toString();
    }
}
