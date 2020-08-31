package comparabilityGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

//create the smallest possible interval order from an interval order
public class IntervalOrderGenerator {

    private ArrayList<ComparableSet> downSets;
    private ArrayList<ComparableSet> upSets;
    private HashMap<Interval, ComparableSet> upsetMap = new HashMap<>();
    private HashMap<Interval, ComparableSet> downSetMap = new HashMap<>();


    public IntervalOrderGenerator() {
        downSets = new ArrayList<>();
        upSets = new ArrayList<>();
    }

    public IntervalOrder optimizeIntervalOrder(IntervalOrder intervalOrder) {
        generateComparableSets(intervalOrder);
        rankComparableSets();
        ArrayList<Interval> optimizedIntervals = new ArrayList<>();
        int intervalId = 0;
        for (Interval interval : intervalOrder.getIntervals()) { //mutate all objects
            interval.setMin(downSets.get(intervalId).getRank());
            interval.setMax(upSets.get(intervalId).getRank());
            optimizedIntervals.add(interval);
            intervalId++;
        }
        return new IntervalOrder(optimizedIntervals);
    }

    private void generateComparableSets(IntervalOrder intervalOrder) {
        for (Interval interval : intervalOrder.getIntervals()) {
            ComparableSet downSet = new ComparableSet(interval);
            ComparableSet upSet = new ComparableSet(interval);
            for (Interval other : intervalOrder.getIntervals()) {
                if (!interval.equals(other)) {
                    if (interval.isHigherThan(other)) {
                        downSet.addInterval(other);
                    }
                    if (interval.isLowerThan(other)) {
                        upSet.addInterval(other);
                    }
                }
            }
            downSets.add(downSet);
            upSets.add(upSet);
            downSetMap.put(interval, downSet);
            upsetMap.put(interval, upSet);
        }
        if (downSets == null || upSets == null) {
            throw new RuntimeException("Either downSets or upSets is null");
        }
        if (downSets.size() != upSets.size()) {
            throw new RuntimeException("Size of downSets does not equal size of upSets");
        }
    }

    private void rankComparableSets() {
        rankDownSets();
        rankUpSets();
    }

    private void rankDownSets() {
        Set<Integer> downSetSizes = new HashSet<>();
        for (ComparableSet downSet : downSets) {
            if (!downSetSizes.contains(downSet.getSize())) {
                downSetSizes.add(downSet.getSize());
            }
        }

        Iterator downSetSizeIterator = downSetSizes.iterator(); //contains the unique sizes of the downsets
        int rank = 1;
        while (downSetSizeIterator.hasNext()) {
            Integer size = (Integer) downSetSizeIterator.next();
            for (ComparableSet downSet : downSets) {
                if (downSet.getSize() == size) {
                    if (rank > downSetSizes.size()) {
                        throw new IllegalArgumentException(String.format("rank (%d) is too large", rank));
                    }
                    downSet.setRank(rank);
                }
            }
            rank++;
        }
    }

    private void rankUpSets() {
        Set<Integer> upSetSizes = new HashSet<>();
        for (ComparableSet upSet : upSets) {
            if (!upSetSizes.contains(upSet.getSize())) {
                upSetSizes.add(upSet.getSize());
            }
        }

        int rank = upSetSizes.size(); //rank should return to 4
        Iterator upSetSizeIterator = upSetSizes.iterator();
        while (upSetSizeIterator.hasNext()) {
            Integer size = (Integer) upSetSizeIterator.next();
            for (ComparableSet upSet : upSets) {
                if (upSet.getSize() == size) {
                    if (rank < 1) {
                        throw new IllegalArgumentException(String.format("rank (%d) is too small", rank));
                    }
                    upSet.setRank(rank);
                }
            }
            rank--;
        }
    }

    public static void main(String[] args) {
//        IntervalOrder intervalOrder = createFinalExamIntervalOrder();
//        IntervalOrder intervalOrder = createSequentialIntervalOrder();
        IntervalOrder intervalOrder = createTextBookIntervalOrder();

        System.out.println(intervalOrder);
        IntervalOrderGenerator generator = new IntervalOrderGenerator();
        IntervalOrder optimizedIntervalOrder = generator.optimizeIntervalOrder(intervalOrder);
        System.out.println(optimizedIntervalOrder);
    }

    public static IntervalOrder createFinalExamIntervalOrder() {
        ArrayList<Interval> intervals = new ArrayList<>();
        intervals.add(new Interval(1, 7)); //A
        intervals.add(new Interval(4, 12)); //B
        intervals.add(new Interval(2, 5)); //C
        intervals.add(new Interval(18, 20)); //D
        intervals.add(new Interval(14, 17)); //E
        intervals.add(new Interval(10, 13)); //F
        intervals.add(new Interval(20, 22)); //G
        IntervalOrder intervalOrder = new IntervalOrder(intervals);
        return intervalOrder;
    }

    public static IntervalOrder createTextBookIntervalOrder() {
        ArrayList<Interval> intervals = new ArrayList<>();
        intervals.add(new Interval(2, 3)); //A
        intervals.add(new Interval(1, 2)); //B
        intervals.add(new Interval(4, 7)); //C
        intervals.add(new Interval(3, 4)); //D
        intervals.add(new Interval(5, 6)); //E
        intervals.add(new Interval(7, 8)); //F
        IntervalOrder intervalOrder = new IntervalOrder(intervals);
        return intervalOrder;
    }

    public static IntervalOrder createSequentialIntervalOrder() {
        ArrayList<Interval> intervals = new ArrayList<>();
        intervals.add(new Interval(1, 2)); //A
        intervals.add(new Interval(2, 3)); //B
        intervals.add(new Interval(3, 4)); //C
        intervals.add(new Interval(4, 5)); //D
        intervals.add(new Interval(5, 6)); //E
        IntervalOrder intervalOrder = new IntervalOrder(intervals);
        return intervalOrder;
    }

    public HashMap<Interval, ComparableSet> getUpsetMap() {
        return upsetMap;
    }

    public HashMap<Interval, ComparableSet> getDownSetMap() {
        return downSetMap;
    }
}
