package comparabilityGraph;

import java.util.HashSet;
import java.util.Objects;

public class ComparableSet {

    private int rank;
    private Interval interval;
    private HashSet<Interval> comparableIntervals;

    public ComparableSet(Interval interval) {
        this.interval = interval;
        comparableIntervals = new HashSet<>();
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getSize() {
        return comparableIntervals.size();
    }

    public HashSet<Interval> getComparableIntervals() {
        return comparableIntervals;
    }

    public void addInterval(Interval interval) {
        comparableIntervals.add(interval);
    }

    @Override
    public String toString() {
        return "ComparableSet of interval " + interval.getLabel();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComparableSet that = (ComparableSet) o;
        return Objects.equals(interval, that.interval) &&
                Objects.equals(comparableIntervals, that.comparableIntervals);
    }

    @Override
    public int hashCode() {
        return Objects.hash(interval, comparableIntervals);
    }
}
