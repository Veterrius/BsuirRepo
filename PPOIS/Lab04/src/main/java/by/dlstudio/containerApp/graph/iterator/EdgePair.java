package by.dlstudio.containerApp.graph.iterator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class EdgePair<T> {
    private final List<T> pair;

    public EdgePair(T vertex1, T vertex2) {
        pair = new ArrayList<>();
        pair.add(vertex1);
        pair.add(vertex2);
    }

    public T getFirstElement() {
        return pair.get(0);
    }

    public T getSecondElement() {
        return pair.get(1);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", EdgePair.class.getSimpleName() + "[", "]")
                .add("pair=" + pair)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EdgePair<?> edgePair = (EdgePair<?>) o;
        boolean eq1 = pair.get(0).equals(edgePair.getFirstElement()) ||
                pair.get(0).equals(edgePair.getSecondElement());
        boolean eq2 = pair.get(1).equals(edgePair.getSecondElement()) ||
                pair.get(1).equals(edgePair.getFirstElement());
        return eq1 && eq2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pair);
    }
}
