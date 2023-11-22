package by.dlstudio.containerApp.graph;

import by.dlstudio.containerApp.graph.exception.GraphException;
import by.dlstudio.containerApp.graph.iterator.EdgeIterator;
import by.dlstudio.containerApp.graph.iterator.EdgePair;
import by.dlstudio.containerApp.graph.iterator.VertexIterator;

import java.util.*;

public class Graph<T> implements Iterable<T>{

    private final List<List<Boolean>> incMatrix;
    private final Map<T, List<T>> vertexesAndEdges;

    public Graph() {
        incMatrix = new ArrayList<>();
        vertexesAndEdges = new HashMap<>();
    }

    /**
     * This method adds a new vertex to the graph
     * @param vertex is a vertex to be added
     * @throws GraphException if this vertex already exists
     */
    public void add(T vertex) throws GraphException {
        if (vertexesAndEdges.containsKey(vertex)) throw new GraphException("This vertex already exists!");
        vertexesAndEdges.put(vertex, new ArrayList<>());
        List<Boolean> edges = new ArrayList<>();
        if (!incMatrix.isEmpty()) {
            for (int i = 0; i < incMatrix.get(0).size(); i++) {
                edges.add(false);
            }
        }
        incMatrix.add(edges);
    }

    /**
     * This method adds an edge to the graph, between existing two vertexes
     * @param start is a first vertex
     * @param end is a second vertex
     * @throws GraphException if one or both selected vertexes don't exist or the edge already exists
     */
    public void add(T start, T end) throws GraphException {
        if (!(vertexesAndEdges.containsKey(start) && vertexesAndEdges.containsKey(end)))
            throw new GraphException("There are no such vertexes");
        if (vertexesAndEdges.get(start).contains(end))
            throw new GraphException("This edge already exists");
        for (int i = 0; i < vertexesAndEdges.size(); i++) {
            T currentVertex = vertexesAndEdges.keySet().stream().toList().get(i);
            incMatrix.get(i).add(currentVertex.equals(start) || currentVertex.equals(end));
        }
        vertexesAndEdges.get(start).add(end);
        vertexesAndEdges.get(end).add(start);
    }

    /**
     * This method removes a vertex from the Graph, if it exists.
     * It also clears all the deleted vertex's edges.
     * @param vertex is a vertex to be deleted
     * @throws GraphException if selected vertex doesn't exist
     */
    public void remove(T vertex) throws GraphException {
        if (indexOf(vertex) == -1) throw new GraphException("There is no such vertex");
        List<Boolean> verEdgeList = incMatrix.get(indexOf(vertex));
        ArrayList<Integer> deleteIndexes = new ArrayList<>();
        for (int i = 0; i < verEdgeList.size(); i++) {
            if (verEdgeList.get(i)) deleteIndexes.add(i);
        }
        for (List<Boolean> matrix : incMatrix) {
            if (!deleteIndexes.isEmpty()) {
                matrix.subList(0, deleteIndexes.size()).clear();
            }
        }
        incMatrix.remove(indexOf(vertex));
        vertexesAndEdges.remove(vertex);
        vertexesAndEdges.forEach((key, value) -> value.remove(vertex));
    }

    /**
     * This method removes the existing edge between two existing vertexes of a graph
     * @param start is a first vertex
     * @param end is a second vertex
     * @throws GraphException if the selected edge or one of the vertexes don't exist
     */
    public void remove(T start, T end) throws GraphException {
        if (!(vertexesAndEdges.containsKey(start) && vertexesAndEdges.containsKey(end)))
            throw new GraphException("There are no such vertexes");
        List<Boolean> startEdgeList = incMatrix.get(indexOf(start));
        List<Boolean> endEdgeList = incMatrix.get(indexOf(end));
        int i = 0;
        try {
            while ((!startEdgeList.get(i)) || (!endEdgeList.get(i))) i++;
        } catch (IndexOutOfBoundsException ex) {
            throw new GraphException("There is no such edge");
        }
        for (List<Boolean> edges : incMatrix) {
            edges.remove(i);
        }
        vertexesAndEdges.get(start).remove(end);
        vertexesAndEdges.get(end).remove(start);
    }

    /**
     * This method checks if the specified vertex is present in Graph
     * @param vertex is a vertex we want to check
     * @return true if this graph contains the specified vertex
     */
    public boolean contains(T vertex) {
        return vertexesAndEdges.containsKey(vertex);
    }

    /**
     * This method checks if an edge between two vertexes exists.
     * @param start is a first vertex
     * @param end is a second vertex
     * @return true if such edge exists
     * @throws GraphException if one or both vertexes don't exist
     */
    public boolean contains(T start, T end) throws GraphException {
        if (!(vertexesAndEdges.containsKey(start) && vertexesAndEdges.containsKey(end)))
            throw new GraphException("There are no such vertexes");
        return vertexesAndEdges.get(start).contains(end);
    }

    /**
     * This method gets the edges number of a graph using an incidence matrix.
     * @return edges number of a graph
     */
    public int getEdgesNumber() {
        return incMatrix.get(0).size();
    }

    /**
     * This method gets the vertexes number of a graph.
     * @return vertexes number of a graph
     */
    public int getVertexesNumber() {
        return vertexesAndEdges.size();
    }

    /**
     * This method gets the specified vertex's degree.
     * Vertex degree is a number of edges that are connected to that vertex.
     * @param vertex we want to find a degree of
     * @return vertex's degree as a number
     * @throws GraphException if such vertex doesn't exist
     */
    public int getVertexDegree(T vertex) throws GraphException {
        if (!vertexesAndEdges.containsKey(vertex)) throw new GraphException("There is no such vertex");
        return vertexesAndEdges.get(vertex).size();
    }

    /**
     * this method finds the index of the vertex in graph
     * @param vertex we want to find index of
     * @return found index, -1 if there is no such element
     */
    public int indexOf(T vertex) {
        return vertexesAndEdges.keySet().stream().toList().indexOf(vertex);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Graph.class.getSimpleName() + "[", "]")
                .add("vertexesAndEdges=" + vertexesAndEdges)
                .toString();
    }

    @Override
    public Iterator<T> iterator() {
        return new VertexIterator<>(vertexesAndEdges);
    }

    /**\
     * This method provides an Iterable to use the for-each loop
     * on graph's edges
     * @return {@link EdgePair} that is a pair of two vertexes representing an edge
     */
    public Iterable<EdgePair<T>> edgeIterable() {
        return () -> new EdgeIterator<>(vertexesAndEdges);
    }
}
