package by.dlstudio.containerApp.graph.iterator;

import by.dlstudio.containerApp.graph.exception.GraphException;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class VertexIterator<T> implements Iterator<T> {

    private final Iterator<T> iterator;

    public VertexIterator(Map<T, List<T>> vertexesAndEdges) {
        iterator = vertexesAndEdges.keySet().iterator();
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return iterator.next();
    }
}
