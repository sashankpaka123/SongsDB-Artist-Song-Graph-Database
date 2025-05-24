import java.util.LinkedList;

/**
 * Represents a node in the graph.
 * Each node can be an artist or a song, and holds a list of neighbors.
 * 
 * Each adjacent node represents an artist recording a song, or vice-versa.
 * 
 * @author Sashank Paka, Aashish
 * @version 2025-04-26
 */
public class Node {
    private String name;
    private LinkedList<Node> adjNodes;

    /**
     * Creates a new Node with the given name.
     * 
     * @param name
     *            The name of the artist or song.
     */
    public Node(String name) {
        this.name = name;
        adjNodes = new LinkedList<Node>();
    }


    /**
     * Gets the name of this node.
     * 
     * @return The name of the artist or song.
     */
    public String getName() {
        return name;
    }


    /**
     * Gets the list of adjacent nodes of this node.
     * 
     * @return The list of adjacent nodes.
     */
    public LinkedList<Node> getAdjNodes() {
        return adjNodes;
    }


    /**
     * Adds a adjacent node to this node if it is not already present.
     * 
     * @param adjNode
     *            The node to add as a adjacent node.
     */
    public void addAdjNodes(Node adjNode) {
        if (!adjNodes.contains(adjNode)) {
            adjNodes.add(adjNode);
        }
    }


    /**
     * Removes a adjacent node from this node.
     * 
     * @param adjNode
     *            The node to remove.
     */
    public void removeAdjNodes(Node adjNode) {
        adjNodes.remove(adjNode);
    }
}
