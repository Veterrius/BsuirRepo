package by.dlstudio.containerApp;

import by.dlstudio.containerApp.graph.Graph;
import by.dlstudio.containerApp.graph.exception.GraphException;

public class App 
{
    //TODO TESTS
    public static void main( String[] args ) throws GraphException {
        Graph<String> myGraph = new Graph<>();
        myGraph.add("A");
        myGraph.add("B");
        myGraph.add("C");
        myGraph.add("A","B");
        myGraph.add("A","C");
        System.out.println(myGraph);
        myGraph.remove("A","C");
        System.out.println(myGraph);
    }
}
