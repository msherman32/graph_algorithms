package comparabilityGraph;

import java.util.ArrayList;

public class IntervalOrder {

    private ArrayList<Interval> intervals;

    public IntervalOrder(ArrayList<Interval> intervals) {
        this.intervals = intervals;
    }

    public ArrayList<Interval> getIntervals() {
        return intervals;
    }

    @Override
    public String toString() {
        return "IntervalOrder{" +
                "intervals=" + intervals +
                '}';
    }
}
