package comparabilityGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import core.Edge;
import core.Node;
import firstFitAlgorithm.FirstFitGenerator;

import static comparabilityGraph.IntervalOrderGenerator.createTextBookIntervalOrder;

//from an interval order, create the comparability graph
public class ComparabilityGraphGenerator {

    public static void main(String[] args) {
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        ArrayList<Edge> result = new ArrayList<>();
        IntervalOrderGenerator intervalOrderGenerator = new IntervalOrderGenerator();
        IntervalOrder intervalOrder = intervalOrderGenerator.optimizeIntervalOrder(createTextBookIntervalOrder());
        System.out.println(intervalOrder);
        for (Interval interval : intervalOrder.getIntervals()) {
            HashMap<Interval, ComparableSet> downSets = intervalOrderGenerator.getDownSetMap();
            HashMap<Interval, ComparableSet> upSets = intervalOrderGenerator.getUpsetMap();
            for (Interval other : intervalOrder.getIntervals()) {
                if (!interval.equals(other)) {
                    Edge newEdge = new Edge(new Node(interval.getRealid()), new Node(other.getRealid()));
                    Edge flippedEdge = new Edge(newEdge.getDest(), newEdge.getSrc());
                    if (!interval.intersects(other) && !result.contains(newEdge) && !result.contains(flippedEdge)
                        && isDirectRoute(interval, other, upSets.get(interval))) {
                        result.add(newEdge);
                    }
                }
            }
        }
        //todo: create a generator that removes unecessary edges (use a set instead of list of edges?)
        FirstFitGenerator firstFitGenerator = new FirstFitGenerator(result);
        firstFitGenerator.generate();

        //create list of edges and pass it into first fit generator
    }

    private static boolean isDirectRoute(Interval interval, Interval other, ComparableSet comparableSet) { //fixme: this method needs to be fixed
        HashSet<Interval> foo = new HashSet<>();
        if (comparableSet.getComparableIntervals() != null) {
            for (Interval intervalAbove : comparableSet.getComparableIntervals()) {
                if (!intervalAbove.equals(other)) {
                    if (interval.isLowerThan(intervalAbove) && other.isHigherThan(intervalAbove)) {
                        foo.add(intervalAbove);
                    }
                }
            }
            if (foo.size() > 0) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

}
