import java.io.File;
import java.util.Scanner;

/**
 * Processes commands for the Graph project.
 * Handles insert, remove, and print commands.
 * 
 * @author Sashank, Aashish
 * @version 2025-04-28
 */
public class CommandProcessor {
    private HashTable artistTable;
    private HashTable songTable;
    private Graph graph;

    /**
     * Initializes the command processor with a hash table size.
     * 
     * @param size
     *            Initial size of the hash tables
     */
    public CommandProcessor(int size) {
        artistTable = new HashTable(size);
        songTable = new HashTable(size);
        graph = new Graph();
    }


    /**
     * Getter for the artist hash table (for testing purposes).
     * 
     * @return artist hash table
     */
    public HashTable getArtistTable() {
        return artistTable;
    }


    /**
     * Getter for the song hash table (for testing purposes).
     * 
     * @return song hash table
     */
    public HashTable getSongTable() {
        return songTable;
    }


    /**
     * Reads and processes each command from the specified file.
     *
     * @param fileName
     *            the path to the input file
     * @throws Exception
     *             if the file cannot be read
     */
    public void processFile(String fileName) throws Exception {
        Scanner scanner = new Scanner(new File(fileName));
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }
            commandHandler(line);
        }
        scanner.close();
    }


    /**
     * Processes a single command line (for testing only).
     * 
     * @param line
     *            The command line string to process
     */
    public void processCommand(String line) {
        commandHandler(line);
    }


    /**
     * Parses and executes a single command line.
     * 
     * @param line
     *            The line containing the command
     */
    private void commandHandler(String line) {
        Scanner scanner = new Scanner(line);
        if (!scanner.hasNext()) {
            scanner.close();
            return;
        }
        String command = scanner.next();
        switch (command) {
            case "insert":
                insertHandler(scanner);
                break;
            case "remove":
                removeHandler(scanner);
                break;
            case "print":
                printHandler(scanner);
                break;
            default:
                break;
        }
        scanner.close();
    }


    /**
     * Handles the "insert" command.
     * 
     * @param scanner
     *            Scanner positioned after "insert"
     */
    private void insertHandler(Scanner scanner) {
        scanner.useDelimiter("<SEP>");
        String artistName = scanner.next().trim();
        String songName = scanner.next().trim();

        Node artistNode = artistTable.search(artistName);
        if (artistNode == null) {
            artistNode = new Node(artistName);
            artistTable.insert(artistName, artistNode);
            graph.addNode(artistNode);
        }

        Node songNode = songTable.search(songName);
        boolean isNewSong = false;
        if (songNode == null) {
            songNode = new Node(songName);
            isNewSong = true;
            graph.addNode(songNode);
        }

        if (artistNode.getAdjNodes().contains(songNode)) {
            System.out.println("|" + artistName + "<SEP>" + songName
                + "| duplicates a record already in the database.");
        }
        else {
            graph.insertEdge(artistNode, songNode);
            if (isNewSong) {
                songTable.insert(songName, songNode);
            }
        }
    }


    /**
     * Handles the "remove" command.
     * 
     * @param scanner
     *            Scanner positioned after "remove"
     */
    private void removeHandler(Scanner scanner) {
        if (!scanner.hasNext()) {
            return;
        }
        String type = scanner.next();
        String name = scanner.nextLine().trim();

        if (type.equals("artist")) {
            Node nodeA = artistTable.remove(name);
            if (nodeA == null) {
                System.out.println("|" + name
                    + "| does not exist in the Artist database.");
            }
            else {
                graph.removeNode(nodeA);
                System.out.println("|" + name
                    + "| is removed from the Artist database");
            }
        }
        else if (type.equals("song")) {
            Node nodeS = songTable.remove(name);
            if (nodeS == null) {
                System.out.println("|" + name
                    + "| does not exist in the Song database.");
            }
            else {
                graph.removeNode(nodeS);
                System.out.println("|" + name
                    + "| is removed from the Song database");
            }
        }
    }


    /**
     * Handles the "print" command.
     * 
     * @param scanner
     *            Scanner positioned after "print"
     */
    private void printHandler(Scanner scanner) {
        if (!scanner.hasNext()) {
            return;
        }
        String type = scanner.next();
        if (type.equals("artist")) {
            artistTable.print("artist");
        }
        else if (type.equals("song")) {
            songTable.print("song");
        }
        else if (type.equals("graph")) {
            graph.printGraphStats();
        }
    }
}
