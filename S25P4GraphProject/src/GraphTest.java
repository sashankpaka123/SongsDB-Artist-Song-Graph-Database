import student.TestCase;

/**
 * Tests the Graph class functionality:
 * 
 * @author Sashank, Aashish
 * @version 2025-04-27
 */
public class GraphTest extends TestCase {

    private Graph graph;
    private Node artist1;
    private Node artist2;
    private Node song1;
    private Node song2;

    /**
     * Sets up a fresh graph and nodes before each test.
     */
    public void setUp() {
        graph = new Graph();
        artist1 = new Node("Artist1");
        artist2 = new Node("Artist2");
        song1 = new Node("Song1");
        song2 = new Node("Song2");
    }


    /**
     * Tests adding nodes to the graph.
     */
    public void testAddNode() {
        graph.addNode(artist1);
        graph.addNode(song1);

        // No crash and correct structure internally assumed
        assertEquals(0, artist1.getAdjNodes().size());
        assertEquals(0, song1.getAdjNodes().size());
    }


    /**
     * Tests inserting an edge between two nodes.
     */
    public void testInsertEdge() {
        graph.addNode(artist1);
        graph.addNode(song1);

        graph.insertEdge(artist1, song1);

        assertTrue(artist1.getAdjNodes().contains(song1));
        assertTrue(song1.getAdjNodes().contains(artist1));
    }


    /**
     * Tests removing a node from the graph and cleaning up connections.
     */
    public void testRemoveNode() {
        graph.addNode(artist1);
        graph.addNode(song1);
        graph.insertEdge(artist1, song1);

        graph.removeNode(song1);

        assertFalse(artist1.getAdjNodes().contains(song1));
    }


    /**
     * Tests printing graph stats when the graph is empty.
     */
    public void testPrintGraphStatsEmpty() {
        graph.printGraphStats();
        String output = systemOut().getHistory();
        assertTrue(output.contains("There are 0 connected components"));
        assertTrue(output.contains(
            "largest connected component has 0 elements"));
    }


    /**
     * Tests printing graph stats for one node (1 component of size 1).
     */
    public void testPrintGraphStatsSingleNode() {
        graph.addNode(artist1);
        graph.printGraphStats();

        String output = systemOut().getHistory();
        assertTrue(output.contains("There are 1 connected components"));
        assertTrue(output.contains(
            "largest connected component has 1 elements"));
    }


    /**
     * Tests a connected component with multiple nodes.
     */
    public void testPrintGraphStatsConnected() {
        graph.addNode(artist1);
        graph.addNode(song1);
        graph.insertEdge(artist1, song1);

        graph.printGraphStats();

        String output = systemOut().getHistory();
        assertTrue(output.contains("There are 1 connected components"));
        assertTrue(output.contains(
            "largest connected component has 2 elements"));
    }


    /**
     * Tests two disconnected components.
     */
    public void testPrintGraphStatsDisconnected() {
        graph.addNode(artist1);
        graph.addNode(artist2);
        graph.addNode(song1);
        graph.addNode(song2);

        graph.insertEdge(artist1, song1);
        graph.insertEdge(artist2, song2);

        graph.printGraphStats();

        String output = systemOut().getHistory();
        assertTrue(output.contains("There are 2 connected components"));
        assertTrue(output.contains(
            "largest connected component has 2 elements"));
    }


    /**
     * Tests deep union-find behavior after multiple edges.
     */
    public void testComplexGraphStructure() {
        Node a = new Node("A");
        Node b = new Node("B");
        Node c = new Node("C");

        graph.addNode(a);
        graph.addNode(b);
        graph.addNode(c);

        graph.insertEdge(a, b); // A-B connected
        // C isolated

        graph.printGraphStats();
        String output = systemOut().getHistory();

        assertTrue(output.contains("There are 2 connected components"));
        assertTrue(output.contains(
            "largest connected component has 2 elements"));
    }


    /**
     * Tests removing a node from a more complex component.
     */
    public void testRemoveNodeFromComponent() {
        graph.addNode(artist1);
        graph.addNode(song1);
        graph.addNode(song2);

        graph.insertEdge(artist1, song1);
        graph.insertEdge(artist1, song2);

        graph.removeNode(artist1);

        assertFalse(song1.getAdjNodes().contains(artist1));
        assertFalse(song2.getAdjNodes().contains(artist1));

        graph.printGraphStats();
        String output = systemOut().getHistory();
        assertTrue(output.contains("There are 2 connected components"));
        assertTrue(output.contains(
            "largest connected component has 1 elements"));
    }


    /**
     * Tests if inserting duplicate edges doesn't inflate component count.
     */
    public void testDuplicateEdgeDoesNotBreakUnion() {
        graph.addNode(artist1);
        graph.addNode(song1);

        graph.insertEdge(artist1, song1);
        graph.insertEdge(artist1, song1);

        graph.printGraphStats();
        String output = systemOut().getHistory();
        assertTrue(output.contains("There are 1 connected components"));
        assertTrue(output.contains(
            "largest connected component has 2 elements"));
    }
    
    /**
     * Tests when remove null node does nothing
     */
    public void testRemoveNullNodeDoesNothing() {
        graph.removeNode(null);
        String output = systemOut().getHistory();
        assertEquals("", output.trim());
    }

}
