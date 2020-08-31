package test.firstFitTest;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;

import core.Edge;
import firstFitAlgorithm.FirstFitGenerator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
//import org.junit.*;

import static prüferTreeEncoding.PrüferTreeGenerator.createCompleteGraph;

//import static org.assertj.core.api.Assertions;

//todo: figure out why junit doesn't work
public class Tests {

//    @Test
    void test_complete_graph_correct_vertices() {
        int n = 5;
        List<Edge> edges = createCompleteGraph(n);
        FirstFitGenerator firstFitGenerator = new FirstFitGenerator(edges);
        Graph graph = firstFitGenerator.getGraph();
        List<Color> vertices = firstFitGenerator.getColors();
        assert (vertices.size()) == n;
        Iterator<Node> iterator = graph.iterator();
        while (iterator.hasNext()) {
            assert iterator.next().getDegree() == n - 1;
        }
    }
}
