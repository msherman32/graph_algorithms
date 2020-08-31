package core;

import java.util.Objects;

public class Edge {

    private Node src;
    private Node dest;

    public Edge(Node src, Node dest) {
        this.src = src;
        this.dest = dest;
    }

    @Override
    public int hashCode() {
        return Objects.hash(src, dest);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Edge other = (Edge) obj;
        return Objects.equals(this.src, other.src)
                && Objects.equals(this.dest, other.dest);
    }

    public Node getSrc() {
        return src;
    }

    public Node getDest() {
        return dest;
    }

    @Override
    public String toString() {
        return "core.Edge: " + src.getVal() + " ==> " + dest.getVal();
    }
}
