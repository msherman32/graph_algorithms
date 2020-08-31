package comparabilityGraph;

import java.util.Objects;

//represents an interval [min, max]
public class Interval {

    private static int id = 1; //fixme: start at 1 or 0?
    private int realid;
    private Character label;
    private int min;
    private int max;

    public Interval(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException(String.format("min (%d) is greater than max (%d)",min,max));
        }
        this.label = generateNewCharacter(id);
        realid = id;
        id++;
        this.min = min;
        this.max = max;
    }

    public void setMin(int min) {
        if (min > max) {
            throw new IllegalArgumentException(String.format("min (%d) is greater than max (%d)",min,max));
        }
        this.min = min;
    }

    public void setMax(int max) {
        if (max < this.min) {
            throw new IllegalArgumentException(String.format("max (%d) is less than min(%d)",max,min));
        }
        this.max = max;
    }

    public Character getLabel() {
        return label;
    }

    public int getRealid() {
        return realid;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public boolean isLowerThan(Interval other) {
        return this.max < other.min;
    }

    public boolean isHigherThan(Interval other) {
        return this.min > other.max;
    }

    public boolean intersects(Interval that) {
        if (this.max < that.min) return false;
        if (that.max < this.min) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(label, min, max);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Interval other = (Interval) obj;
        return Objects.equals(this.label, other.label)
                && Objects.equals(this.min, other.min)
                && Objects.equals(this.max, other.max);
    }

    @Override
    public String toString() {
        return String.format("%s(%d) [%d,%d]",label, realid, min, max);
    }

    private Character generateNewCharacter(int index) {
        int asciiAlphabetStart = 64; //right before where 'A' begins in Ascii
        if (index >= 26) { //greater than the size of the alphabet
            int offset = 6;
            int asciiTableSize = 128;
            return (char) ((asciiAlphabetStart + index + offset) % asciiTableSize) ; //todo: loop around to the front of ascii table
        } else {
            return (char) (asciiAlphabetStart + index);
        }
    }
}
