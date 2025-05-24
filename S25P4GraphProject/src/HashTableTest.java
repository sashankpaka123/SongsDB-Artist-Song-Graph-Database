import student.TestCase;

/**
 * Tests the basic functionality of the HashTable class.
 * 
 * Only insertions and rehashing are tested for now.
 * Later, remove(), search(), and print() can be added.
 * 
 * @author Sashank Paka, Aashish
 * @version 2025-04-27
 */
public class HashTableTest extends TestCase {
    private HashTable table;

    /**
     * Sets up a new HashTable for each test.
     */
    public void setUp() {
        table = new HashTable(4);
    }


    /**
     * Tests inserting normally into a null slot (empty table).
     */
    public void testInsertIntoNullSlot() {
        Node artist = new Node("Artist");
        table.insert("Artist", artist);

        assertEquals(artist, table.search("Artist"));
    }


    /**
     * Tests Insert
     */
    /**
     * Tests inserting into a tombstone slot.
     */
    public void testInsertIntoTombstoneSlot() {
        Node artist = new Node("Artist1");
        table.insert("Artist1", artist);
        table.remove("Artist1");

        Node newArtist = new Node("Artist2");
        table.insert("Artist2", newArtist);

        assertEquals(newArtist, table.search("Artist2"));
    }


    /**
     * Tests that rehash happens when >50% full.
     */
    public void testInsertTriggersRehash() {
        Node a = new Node("A");
        Node b = new Node("B");
        Node c = new Node("C");

        table.insert("A", a);
        table.insert("B", b);
        table.insert("C", c);

        assertEquals(a, table.search("A"));
        assertEquals(b, table.search("B"));
        assertEquals(c, table.search("C"));
    }


    /**
     * Tests inserting an Artist node (empty adj list).
     */
    public void testInsertArtistNode() {
        Node artist = new Node("Artist");
        table.insert("Artist", artist);

        String output = systemOut().getHistory();
        assertTrue(output.contains("|Artist| is added to the Artist database"));
    }


    /**
     * Tests inserting a Song node (non-empty adj list).
     */
    public void testInsertSongNode() {
        Node song = new Node("Song");
        song.getAdjNodes().add(new Node("ConnectedArtist"));

        table.insert("Song", song);

        String output = systemOut().getHistory();
        assertTrue(output.contains("|Song| is added to the Song database"));
    }


    /**
     * Tests that quadratic probing occurs when a collision happens
     * during insertion into the hash table.
     */
    public void testInsertCausesProbing() {
        Node node1 = new Node("Key1");
        Node node2 = new Node("Key1collision");

        table.insert("Key1", node1);
        table.insert("Key1collision", node2);

        assertEquals(node1, table.search("Key1"));
        assertEquals(node2, table.search("Key1collision"));
    }


    /**
     * Forces multiple probes and triggers rehash.
     */
    public void testInsertMultipleProbesAndRehash() {
        Node a = new Node("A");
        Node e = new Node("E");
        Node i = new Node("I");
        Node o = new Node("O");

        table.insert("A", a);
        table.insert("E", e);
        table.insert("I", i);
        table.insert("O", o);

        assertEquals(a, table.search("A"));
        assertEquals(e, table.search("E"));
        assertEquals(i, table.search("I"));
        assertEquals(o, table.search("O"));
    }


    /**
     * Tests Remove
     */

    /**
     * 
     * Removes an
     * existing record successfully.
     */

    public void testRemoveSuccess() {
        Node artist = new Node("Artist");
        table.insert("Artist", artist);

        Node removed = table.remove("Artist");
        assertEquals(artist, removed);

        assertNull(table.search("Artist"));
    }


    /**
     * Tries removing from an empty table (null slot).
     */
    public void testRemoveFromEmptySlot() {
        assertNull(table.remove("Nonexistent"));
    }


    /**
     * Removes after collision probing.
     */
    public void testRemoveAfterCollision() {
        Node a = new Node("A");
        Node e = new Node("E");

        table.insert("A", a);
        table.insert("E", e);

        Node removed = table.remove("E");
        assertEquals(e, removed);

        assertEquals(a, table.search("A"));
        assertNull(table.search("E"));
    }


    /**
     * Removing something twice (after it's already removed).
     */
    public void testRemoveTwice() {
        Node artist = new Node("Artist");

        table.insert("Artist", artist);
        table.remove("Artist");

        assertNull(table.remove("Artist"));
    }


    /**
     * Insert -> Remove -> Insert again (slot reuse after tombstone).
     */
    public void testInsertAfterRemoveSlotReuse() {
        Node artist1 = new Node("Artist1");
        Node artist2 = new Node("Artist2");

        table.insert("Artist1", artist1);
        table.remove("Artist1");

        table.insert("Artist2", artist2);
        assertEquals(artist2, table.search("Artist2"));
    }


    /**
     * Tests search
     */
    /**
     * Tests successful search for an existing record.
     */
    public void testSearchFound() {
        Node artist = new Node("Artist");
        table.insert("Artist", artist);

        assertEquals(artist, table.search("Artist"));
    }


    /**
     * Tests search for a missing key (hits null slot).
     */
    public void testSearchNotFound() {
        assertNull(table.search("Nonexistent"));
    }


    /**
     * Tests searching after a collision (probing needed).
     */
    public void testSearchWithProbing() {
        Node a = new Node("A");
        Node e = new Node("E");
        table.insert("A", a);
        table.insert("E", e);

        assertEquals(a, table.search("A"));
        assertEquals(e, table.search("E"));
    }


    /**
     * Tests searching past a tombstone slot.
     */
    public void testSearchPastTombstone() {
        Node artist1 = new Node("Artist1");
        Node artist2 = new Node("Artist2");

        table.insert("Artist1", artist1);
        table.insert("Artist2", artist2);

        table.remove("Artist1");

        assertEquals(artist2, table.search("Artist2"));
    }


    /**
     * Prints when table has active entries (artists).
     */
    public void testPrintWithEntries() {
        Node artist1 = new Node("Artist1");
        Node artist2 = new Node("Artist2");

        table.insert("Artist1", artist1);
        table.insert("Artist2", artist2);

        table.print("artist");

        String output = systemOut().getHistory();
        assertTrue(output.contains("|Artist1|"));
        assertTrue(output.contains("|Artist2|"));
        assertTrue(output.contains("total artists: 2"));
    }


    /**
     * Prints after removal
     */
    public void testPrintWithTombstones() {
        Node artist = new Node("Artist");

        table.insert("Artist", artist);
        table.remove("Artist");

        table.print("artist");

        String output = systemOut().getHistory();
        assertTrue(output.contains("TOMBSTONE"));
        assertTrue(output.contains("total artists: 0"));
    }


    /**
     * Heavy testing for mutation test
     */
    public void testHeavy() {
        Node n1 = new Node("A");
        Node n2 = new Node("E");
        Node n3 = new Node("I");
        Node n4 = new Node("M");
        Node n5 = new Node("Q");
        Node n6 = new Node("U");
        Node n7 = new Node("Y");

        table.insert("A", n1);
        table.insert("E", n2);
        table.insert("I", n3);
        table.insert("M", n4);
        table.insert("Q", n5);
        table.insert("U", n6);
        table.insert("Y", n7);

        assertEquals(n1, table.search("A"));
        assertEquals(n2, table.search("E"));
        assertEquals(n3, table.search("I"));
        assertEquals(n4, table.search("M"));
        assertEquals(n5, table.search("Q"));
        assertEquals(n6, table.search("U"));
        assertEquals(n7, table.search("Y"));

        Node removed1 = table.remove("E");
        Node removed2 = table.remove("M");
        Node removed3 = table.remove("U");

        assertEquals(n2, removed1);
        assertEquals(n4, removed2);
        assertEquals(n6, removed3);

        assertNull(table.search("E"));
        assertNull(table.search("M"));
        assertNull(table.search("U"));

        assertEquals(n1, table.search("A"));
        assertEquals(n5, table.search("Q"));
        assertEquals(n7, table.search("Y"));

        Node n8 = new Node("B");
        Node n9 = new Node("F");
        Node n10 = new Node("J");

        table.insert("B", n8);
        table.insert("F", n9);
        table.insert("J", n10);

        assertEquals(n8, table.search("B"));
        assertEquals(n9, table.search("F"));
        assertEquals(n10, table.search("J"));

        table.print("artist");
        String output = systemOut().getHistory();

        assertTrue(output.contains("|A|"));
        assertTrue(output.contains("|B|"));
        assertTrue(output.contains("|F|"));
        assertTrue(output.contains("|J|"));
        assertTrue(output.contains("|Q|"));
        assertTrue(output.contains("|Y|"));
        assertTrue(output.contains("TOMBSTONE"));

    }


    /**
     * Full stress test for insertion, removal, probing, rehashing, tombstone
     * 
     */
    public void testDeepCollisionStress() {
        Node a = new Node("A");
        Node e = new Node("E");
        Node i = new Node("I");
        Node m = new Node("M");
        Node q = new Node("Q");
        Node u = new Node("U");
        Node y = new Node("Y");

        table.insert("A", a);
        table.insert("E", e);
        table.insert("I", i);
        table.insert("M", m);
        table.insert("Q", q);
        table.insert("U", u);
        table.insert("Y", y);

        table.remove("E");
        table.remove("M");

        assertNull(table.search("E"));
        assertNull(table.search("M"));
        assertEquals(a, table.search("A"));
        assertEquals(q, table.search("Q"));

        Node b = new Node("B");
        Node f = new Node("F");
        table.insert("B", b);
        table.insert("F", f);

        assertEquals(b, table.search("B"));
        assertEquals(f, table.search("F"));

        Node foundA = table.search("A");
        Node foundQ = table.search("Q");

        assertNotNull(foundA);
        assertNotNull(foundQ);
        table.print("artist");
        String output = systemOut().getHistory();
        assertTrue(output.contains("TOMBSTONE"));
        assertTrue(output.contains("|A|"));
        assertTrue(output.contains("|B|"));
        assertTrue(output.contains("|F|"));
        assertTrue(output.contains("|Q|"));
        assertTrue(output.contains("|U|"));
        assertTrue(output.contains("|Y|"));

        HashTable guessTable = new HashTable(4);
        Node song = new Node("Song");
        song.getAdjNodes().add(new Node("Another Artist"));
        guessTable.insert("Song", song);
        assertEquals("songs", guessTable.guessType());

        HashTable guessTable2 = new HashTable(4);
        Node artist = new Node("Artist");
        guessTable2.insert("Artist", artist);
        assertEquals("artists", guessTable2.guessType());
    }


    /**
     * More Insert testing for mutation
     */
    public void testInsertWithoutPrint() {
        HashTable smallTable = new HashTable(2);

        Node n1 = new Node("KeyA");
        Node n2 = new Node("KeyB");
        Node n3 = new Node("KeyC");
        smallTable.insert("KeyA", n1);
        smallTable.insert("KeyB", n2);
        smallTable.insert("KeyC", n3);
        assertEquals(n1, smallTable.search("KeyA"));
        assertEquals(n2, smallTable.search("KeyB"));
        assertEquals(n3, smallTable.search("KeyC"));
    }
}
