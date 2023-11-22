package by.dlstudio.containerApp.graph.iterator;

import java.util.*;

public class EdgeIterator<T> implements Iterator<EdgePair<T>> {

    private final Iterator<EdgePair<T>> iterator;

    public EdgeIterator(Map<T, List<T>> vertexesAndEdges) {
        this.iterator = getIterator(vertexesAndEdges);
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public EdgePair<T> next() {
        if (!iterator.hasNext()) throw new NoSuchElementException();
        return iterator.next();
    }

    private Iterator<EdgePair<T>> getIterator(Map<T, List<T>> vertexesAndEdges) {
        List<EdgePair<T>> edges = new ArrayList<>();
        EdgePair<T> edgePair = null;
        for (Map.Entry<T,List<T>> entry : vertexesAndEdges.entrySet()) {
            for (int i = 0; i < entry.getValue().size(); i++) {
                edgePair = new EdgePair<>(entry.getKey(), entry.getValue().get(i));
                if (!edges.contains(edgePair)) edges.add(edgePair);
            }
        }
        return edges.iterator();
    }
}
