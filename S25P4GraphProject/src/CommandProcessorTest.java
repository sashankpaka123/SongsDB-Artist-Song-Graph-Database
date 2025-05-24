import student.TestCase;

/**
 * Test class for CommandProcessor
 * 
 * @author Sashank, Aashish
 * @version 2025-05-4
 */
public class CommandProcessorTest extends TestCase {

    private CommandProcessor command;

    /**
     * Sets up a CommandProcessor with size 4.
     */
    public void setUp() {
        command = new CommandProcessor(4);
    }


    /**
     * Tests inserting an artist and song.
     */
    public void testInsert() {
        command.processCommand("insert Artist1<SEP>Song1");
        String output = systemOut().getHistory();
        assertTrue(output.contains(
            "|Artist1| is added to the Artist database"));
        assertTrue(output.contains("|Song1| is added to the Song database"));
    }


    /**
     * Tests inserting when the artist already exists but the song is new.
     */
    public void testDoubleSongInsert() {
        command.processCommand("insert Artist1<SEP>Song1");
        command.processCommand("insert Artist1<SEP>Song2");

        String output = systemOut().getHistory();
        assertTrue(output.contains("|Song2| is added to the Song database"));
        assertTrue(output.contains("|Artist1|"));
    }


    /**
     * Tests inserting when the song already exists but the artist is new.
     */
    public void testDoubleArtistnsert() {
        command.processCommand("insert Artist1<SEP>Song1");
        command.processCommand("insert Artist2<SEP>Song1");

        String output = systemOut().getHistory();
        assertTrue(output.contains(
            "|Artist2| is added to the Artist database"));
        assertTrue(output.contains("|Song1|"));
    }


    /**
     * Tests inserting a duplicate artist-song connection.
     */
    public void testDuplicate() {
        command.processCommand("insert Artist1<SEP>Song1");
        systemOut().clearHistory();
        command.processCommand("insert Artist1<SEP>Song1");
        String output = systemOut().getHistory();
        assertTrue(output.contains("|Artist1<SEP>Song1| duplicates a "
            + "record already in the database."));
    }


    /**
     * Tests removing an artist that exists.
     */
    public void testRemoveArtist() {
        command.processCommand("insert Artist1<SEP>Song1");
        command.processCommand("remove artist Artist1");
        String output = systemOut().getHistory();
        assertTrue(output.contains(
            "|Artist1| is removed from the Artist database"));
    }


    /**
     * Tests removing a song that exists.
     */
    public void testRemoveSong() {
        command.processCommand("insert Artist1<SEP>Song1");
        command.processCommand("remove song Song1");
        String output = systemOut().getHistory();
        assertTrue(output.contains(
            "|Song1| is removed from the Song database"));
    }


    /**
     * Tests removing a non-existent artist.
     */
    public void testArtistNonexistent() {
        command.processCommand("remove artist UnknownArtist");
        String output = systemOut().getHistory();
        assertTrue(output.contains(
            "|UnknownArtist| does not exist in the Artist database."));
    }


    /**
     * Tests removing a non-existent song.
     */
    public void testSongNonexistent() {
        command.processCommand("remove song UnknownSong");
        String output = systemOut().getHistory();
        assertTrue(output.contains(
            "|UnknownSong| does not exist in the Song database."));
    }


    /**
     * Tests printing the artist hash table.
     */
    public void testPrintArtist() {
        Node artist = new Node("Artist1"); // empty adj list = artist
        command.getArtistTable().insert("Artist1", artist);
        command.processCommand("print artist");
        String output = systemOut().getHistory();
        assertTrue(output.contains("|Artist1|"));
        assertTrue(output.contains("total artists: 1"));
    }


    /**
     * Tests printing the song hash table.
     */
    public void testPrintSong() {
        command.processCommand("insert Artist1<SEP>Song1");
        command.processCommand("print song");
        String output = systemOut().getHistory();
        assertTrue(output.contains("|Song1|"));
        assertTrue(output.contains("total songs: 1"));
    }


    /**
     * Tests printing graph statistics.
     */
    public void testGraph() {
        command.processCommand("insert Artist1<SEP>Song1");
        command.processCommand("print graph");
        String output = systemOut().getHistory();
        assertTrue(output.contains("There are"));
        assertTrue(output.contains("connected components"));
    }


    /**
     * Tests handling an invalid command.
     */
    public void testInvalidCommand() {
        command.processCommand("invalidcommand stuff");
        String output = systemOut().getHistory();
        assertEquals("", output.trim());
    }


    /**
     * Tests processFile with multiple commands and blank lines.
     */
    public void testProcessFileWithBlankLines() throws Exception {
        java.io.File tempFile = java.io.File.createTempFile("test", ".txt");
        java.io.PrintWriter out = new java.io.PrintWriter(tempFile);
        out.println(""); // blank line
        out.println("insert ArtistA<SEP>SongA");
        out.println(""); // another blank line
        out.println("insert ArtistB<SEP>SongB");
        out.close();

        CommandProcessor newCommand = new CommandProcessor(4);
        newCommand.processFile(tempFile.getAbsolutePath());

        String output = systemOut().getHistory();
        assertTrue(output.contains(
            "|ArtistA| is added to the Artist database"));
        assertTrue(output.contains("|SongA| is added to the Song database"));
        assertTrue(output.contains(
            "|ArtistB| is added to the Artist database"));
        assertTrue(output.contains("|SongB| is added to the Song database"));

        tempFile.delete();
    }


    /**
     * Tests that an empty line does not crash or do anything
     */
    public void testInsertEmptyCommand() {
        command.processCommand(""); // Should trigger early return
        String output = systemOut().getHistory();
        assertEquals("", output.trim());
    }


    /**
     * Tests that removeHandler with no second word doesn't crash (early
     * return).
     */
    public void testRemoveHandlerEmpty() {
        command.processCommand("remove"); // Only 'remove', no artist/song or
                                          // name
        String output = systemOut().getHistory();
        assertEquals("", output.trim());
    }


    /**
     * Tests that printHandler with no second word doesn't crash (early return).
     */
    public void testPrintHandlerEmpty() {
        command.processCommand("print"); // Only 'print', no type
        String output = systemOut().getHistory();
        assertEquals("", output.trim());
    }


    /**
     * Tests that a new song node is still added to the hash table
     * even if the artist-song edge is a duplicate.
     */
    public void testInsertDuplicateEdgeButNewSongNode() {
        command.processCommand("insert ArtistX<SEP>SongY");
        command.processCommand("remove song SongY");
        command.processCommand("insert ArtistX<SEP>SongY");
        command.processCommand("insert ArtistX<SEP>SongY");
        String output = systemOut().getHistory();
        assertTrue(output.contains("|SongY| is added to the Song database"));
        assertTrue(output.contains(
            "|ArtistX<SEP>SongY| duplicates a "
            + "record already in the database."));
        assertNotNull(command.getSongTable().search("SongY"));
    }


    /**
     * Tests that removing an artist cleans up graph and allows reinsert
     * without duplicate edge bugs.
     */
    public void testRemoveArtistAndReinsert() {
        command.processCommand("insert ArtistA<SEP>SongA");
        command.processCommand("remove artist ArtistA");
        command.processCommand("insert ArtistA<SEP>SongA");
        String output = systemOut().getHistory();
        assertTrue(output.contains(
            "|ArtistA| is removed from the Artist database"));
        assertTrue(output.contains(
            "|ArtistA| is added to the Artist database"));
        assertTrue(output.contains("|SongA| is added to the Song database"));
    }
    
    /**
     * Tests printSong specifically
     */
    public void testPrintSongOnly() {
        command.processCommand("insert Artist1<SEP>Song1");
        systemOut().clearHistory();
        command.processCommand("print song");
        String output = systemOut().getHistory();

        assertTrue(output.contains("|Song1|"));
        assertTrue(output.contains("total songs:"));
        assertFalse(output.contains("|Artist1|"));
    }
    
    /**
     * Tests printGraph specifically
     */
    public void testPrintGraphOnly() {
        command.processCommand("insert Artist1<SEP>Song1");
        systemOut().clearHistory();
        command.processCommand("print graph");
        String output = systemOut().getHistory();

        assertTrue(output.contains("connected components"));
        assertFalse(output.contains("|Artist1|"));
        assertFalse(output.contains("|Song1|"));
    }


}
