import student.TestCase;

/**
 * Tests the Node class.
 * Covers constructor, getters, addAdjNodes, removeAdjNodes.
 * 
 * @author Sashank, Aashish
 * @version 2025-04-27
 */
public class NodeTest extends TestCase {

    private Node artist;
    private Node song;
    private Node anotherSong;

    /**
     * Sets up new Node instances for testing.
     */
    public void setUp() {
        artist = new Node("Artist");
        song = new Node("Song");
        anotherSong = new Node("Another Song");
    }


    /**
     * Tests that name is stored correctly.
     */
    public void testGetName() {
        assertEquals("Artist", artist.getName());
        assertEquals("Song", song.getName());
    }


    /**
     * Tests that initially the adjacency list is empty.
     */
    public void testEmpty() {
        assertTrue(artist.getAdjNodes().isEmpty());
    }


    /**
     * Tests adding a neighbor.
     */
    public void testAddAdjNodes() {
        artist.addAdjNodes(song);
        assertEquals(1, artist.getAdjNodes().size());
        assertTrue(artist.getAdjNodes().contains(song));
    }


    /**
     * Tests that adding a duplicate neighbor doesn't add again.
     */
    public void testAddDuplicate() {
        artist.addAdjNodes(song);
        artist.addAdjNodes(song); // Should NOT add again
        assertEquals(1, artist.getAdjNodes().size());
    }


    /**
     * Tests removing a neighbor node.
     */
    public void testRemoveAdjNodes() {
        artist.addAdjNodes(song);
        artist.removeAdjNodes(song);
        assertTrue(artist.getAdjNodes().isEmpty());
    }


    /**
     * Tests removing a neighbor that is not there (no crash).
     */
    public void testRemoveNonexistent() {
        artist.removeAdjNodes(anotherSong);
        assertTrue(artist.getAdjNodes().isEmpty()); // Still empty
    }
}
