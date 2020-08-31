package core;

import java.util.Objects;

public class Node implements Comparable {
    private int val;
    private int degree;

    public Node(int val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return "" + val;
    }

    public int getVal() {
        return val;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    @Override
    public int hashCode() {
        return Objects.hash(val);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Node other = (Node) obj;
        return Objects.equals(this.val, other.val);
    }

    @Override
    public int compareTo(Object o) {
        if (this.val == ((Node) o).val) {
            return 0;
        } else if (this.val > ((Node) o).val) {
            return 1;
        } else {
            return -1;
        }
    }
}
