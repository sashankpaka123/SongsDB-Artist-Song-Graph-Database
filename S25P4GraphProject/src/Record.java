/**
 * A Record class that stores a name and a pointer to a Node in the graph.
 * Used as the value stored in the hash tables.
 * 
 * @author Sashank Paka, Aashish
 * @version 2025-04-26
 */
public class Record {
    private String name;
    private Node node;

    /**
     * Creates a new Record object.
     * 
     * @param name
     *            The name (artist or song)
     * @param node
     *            The corresponding graph node
     */
    public Record(String name, Node node) {
        this.name = name;
        this.node = node;
    }


    /**
     * Returns the name stored in the record.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }


    /**
     * Returns the node stored in the record.
     * 
     * @return the node
     */
    public Node getNode() {
        return node;
    }
}
