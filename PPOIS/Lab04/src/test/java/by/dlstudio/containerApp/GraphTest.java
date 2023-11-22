package by.dlstudio.containerApp;

import by.dlstudio.containerApp.graph.Graph;
import by.dlstudio.containerApp.graph.exception.GraphException;
import by.dlstudio.containerApp.graph.iterator.EdgePair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GraphTest {

    private static Graph<String> testGraph;

    @BeforeAll
    static void loadData() {
        testGraph = new Graph<>();
        try {
            testGraph.add("A");
            testGraph.add("B");
            testGraph.add("C");
            testGraph.add("A","C");
            testGraph.add("D");
            testGraph.add("B","D");
        } catch (GraphException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void addVertexTest_Success() {
        try {
            testGraph.add("E");
        } catch (GraphException e) {
            throw new RuntimeException(e);
        }
        assertNotEquals(-1,testGraph.indexOf("E"));
    }

    @Test
    void addVertexTest_GraphException() {
        assertThrows(GraphException.class, ()-> testGraph.add("A"));
    }

    @Test
    void addEdgeTest_Success() {
        try {
            testGraph.add("A","B");
        } catch (GraphException e) {
            throw new RuntimeException(e);
        }
        try {
            assertTrue(testGraph.contains("A","B"));
        } catch (GraphException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void addEdgeTest_GraphException_NoSuchVertexes() {
        assertThrows(GraphException.class, ()->testGraph.add("A","0"));
        try{
            testGraph.add("A","0");
        } catch (GraphException e) {
            assertEquals("There are no such vertexes", e.getMessage());
        }
    }

    @Test
    void addEdgeTest_GraphException_EdgeAlreadyExists() {
        assertThrows(GraphException.class, ()->testGraph.add("A","C"));
        try{
            testGraph.add("A","C");
        } catch (GraphException e) {
            assertEquals("This edge already exists", e.getMessage());
        }
    }

    @Test
    void removeVertexTest_Success() {
        try {
            int prevDegree = testGraph.getVertexDegree("B");
            testGraph.remove("D");
            assertFalse(testGraph.contains("D"));
            assertEquals(prevDegree-1,testGraph.getVertexDegree("B"));
        } catch (GraphException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void removeVertexTest_GraphException_ThereIsNoSuchVertex() {
        assertThrows(GraphException.class, ()->testGraph.remove("0"));
    }

    @Test
    void removeEdgeTest_Success() {
        try {
            testGraph.add("A","B");
            int prevDegree = testGraph.getVertexDegree("A");
            testGraph.remove("A","B");
            assertEquals(prevDegree-1,testGraph.getVertexDegree("A"));
        } catch (GraphException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void removeEdgeTest_GraphException_ThereIsNoSuchVertexes() {
        assertThrows(GraphException.class,()->testGraph.remove("A","0"));
    }

    @Test
    void removeEdgeTest_GraphException_ThereIsNoSuchEdge() {
        try {
            testGraph.add("Z");
            assertThrows(GraphException.class,()->testGraph.remove("A","Z"));
            testGraph.remove("Z");
        } catch (GraphException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getVertexesAndEdgesNumberTest_Success() {
        assertNotEquals(0, testGraph.getVertexesNumber());
        assertNotEquals(0, testGraph.getEdgesNumber());
    }

    @Test
    void containsEdgeTest_GraphException_ThereIsNoSuchVertexes() {
        assertThrows(GraphException.class,()->testGraph.contains("A","0"));
    }

    @Test
    void vertexIteratorAndForEachTest() {
        assertTrue(testGraph.iterator().hasNext());
        assertNotNull(testGraph.iterator().next());
        testGraph.iterator().forEachRemaining(s->{
            assertTrue(testGraph.contains(s));
        });
        for (String vertex : testGraph) {
            assertTrue(testGraph.contains(vertex));
        }
    }

    @Test
    void edgeForEachTest() {
        for (EdgePair<String> edge : testGraph.edgeIterable()) {
            try {
                assertTrue(testGraph.contains(edge.getFirstElement(), edge.getSecondElement()));
            } catch (GraphException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
