package playground;

import java.util.HashMap;
import java.util.Map;

public class ProbabilityCalculator {

    private Map<Integer, Percent> probability;

    public ProbabilityCalculator(int numSides) {
        probability = new HashMap<>();
        double denominator = Math.pow(numSides, 2);
        for (int i = 0; i <= numSides; i++) {
            probability.put(1 + i, new Percent(i/denominator));
            probability.put(numSides * 2 + 1 - i, new Percent(i/denominator ));
        }
    }

    public static void main(String[] args) {
        ProbabilityCalculator calculator = new ProbabilityCalculator(2);
        printRows(calculator.probability);;
    }

    static void printRows(Map map) {
        for (Object o : map.keySet()) {
            System.out.println(o + " : " + map.get(o));
        }
    }

    class Percent {

        Double value;

        public Percent(Double value) {
            this.value = value;
        }

        @Override
        public String toString() {
            if (value == 0.0) {
                return "0.00%";
            }
            Double percent = value * 100;
            String result = Double.toString(percent);
            return result.substring(0,5) + "%";
        }
    }
}

