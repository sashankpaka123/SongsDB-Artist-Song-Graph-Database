/**
 * A hash table that stores artist or song names mapped to their graph nodes.
 * Uses quadratic probing and supports rehashing.
 * 
 * @author Sashank Paka, Aashish
 * @version 2025-04-26
 */
public class HashTable {

    private Record[] table;
    private int size;
    private static final Record TOMBSTONE = new Record("TOMBSTONE", null);

    /**
     * Creates a new hash table with the given initial size.
     * 
     * @param initSize
     *            Initial number of slots.
     */
    public HashTable(int initSize) {
        table = new Record[initSize];
        size = 0;
    }


    /**
     * Inserts a name-node pair into the hash table.
     * 
     * @param name
     *            The artist or song name.
     * @param node
     *            The associated node.
     */
    public void insert(String name, Node node) {
        if ((size + 1) > (table.length / 2)) {
            rehash();
        }

        int homeSlot = Hash.h(name, table.length);
        int slot = homeSlot;
        int i = 1;
        int probes = 0;

        while (probes < table.length) {
            if (table[slot] == null || table[slot] == TOMBSTONE) {
                table[slot] = new Record(name, node);
                size++;

                String type;
                if (node.getAdjNodes().isEmpty()) {
                    type = "Artist";
                }
                else {
                    type = "Song";
                }
                System.out.println("|" + name + "| is added to the " + type
                    + " database");
                return;
            }
            slot = (homeSlot + i * i) % table.length;
            i++;
            probes++;
        }
    }


    /**
     * Searches for a name in the hash table.
     * 
     * @param name
     *            The name to search for.
     * @return The node if found, or null.
     */
    public Node search(String name) {
        int homeSlot = Hash.h(name, table.length);
        int slot = homeSlot;
        int i = 1;
        int probes = 0;

        while (probes < table.length) {
            if (table[slot] == null) {
                return null;
            }
            else {
                if (table[slot] != TOMBSTONE && table[slot].getName().equals(
                    name)) {
                    return table[slot].getNode();
                }
            }
            slot = (homeSlot + i * i) % table.length;
            i++;
            probes++;
        }

        return null;
    }


    /**
     * Removes a name from the hash table.
     * 
     * @param name
     *            The name to remove.
     * @return The node that was removed, or null if not found.
     */
    public Node remove(String name) {
        int homeSlot = Hash.h(name, table.length);
        int slot = homeSlot;
        int i = 1;
        int probes = 0;

        while (probes < table.length) {
            if (table[slot] == null) {
                return null;
            }
            else if (table[slot] != TOMBSTONE && table[slot].getName().equals(
                name)) {
                Node removedNode = table[slot].getNode();
                table[slot] = TOMBSTONE;
                size--;
                return removedNode;
            }
            slot = (homeSlot + i * i) % table.length;
            i++;
            probes++;
        }
        return null;
    }


    /**
     * Prints the contents of the hash table, including tombstones.
     * Also prints the total number of active records of the given type.
     *
     * @param type
     *            the type of records stored ("artist" or "song")
     */
    public void print(String type) {
        int count = 0;
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                if (table[i] == TOMBSTONE) {
                    System.out.println(i + ": TOMBSTONE");
                }
                else {
                    System.out.println(i + ": |" + table[i].getName() + "|");
                    count++;
                }
            }
        }

        System.out.println("total " + type + "s: " + count);
    }


    /**
     * Doubles the size of the table and rehashes all active records.
     */
    private void rehash() {
        Record[] old = table;
        table = new Record[old.length * 2];
        size = 0;

        for (int i = 0; i < old.length; i++) {
            if (old[i] != null && old[i] != TOMBSTONE) {
                insertWithoutPrint(old[i].getName(), old[i].getNode());
            }
        }

        String type = guessType();
        type = type.substring(0, type.length() - 1);
        System.out.println(type.substring(0, 1).toUpperCase() + type.substring(
            1) + " hash table size doubled.");
    }


    /**
     * Inserts into the table without printing anything (used during rehash).
     * 
     * @param name
     *            The name to insert.
     * @param node
     *            The associated node.
     */
    private void insertWithoutPrint(String name, Node node) {
        int homeSlot = Hash.h(name, table.length);
        int slot = homeSlot;
        int i = 1;
        int probes = 0;

        while (probes < table.length) {
            if (table[slot] == null) {
                table[slot] = new Record(name, node);
                size++;
                return;
            }
            slot = (homeSlot + i * i) % table.length;
            i++;
            probes++;
        }
    }


    /**
     * Guesses whether the table holds artists or songs, based on node contents.
     * 
     * @return "artists" or "songs"
     */
    public String guessType() {
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null && table[i] != TOMBSTONE) {
                if (table[i].getNode().getAdjNodes().isEmpty()) {
                    return "artists";
                }
                else {
                    return "songs";
                }
            }
        }
        return "artists";
    }

}
