import java.util.LinkedList;

/**
 * Represents the graph of artists and songs using an adjacency list.
 * connected components are calculated using array based Union Find.
 * 
 * @author Sashank, Aashish
 * @version 2025-04-28
 */
public class Graph {
    private LinkedList<Node> nodes;

    /**
     * Creates a new empty graph with no nodes.
     */
    public Graph() {
        nodes = new LinkedList<>();
    }


    /**
     * Inserts an undirected edge between an artist and a song.
     * 
     * @param artist
     *            the artist node
     * @param song
     *            the song node
     */
    public void insertEdge(Node artist, Node song) {
        artist.addAdjNodes(song);
        song.addAdjNodes(artist);
    }


    /**
     * Adds a node to the graph.
     * 
     * @param node
     *            the node to add
     */
    public void addNode(Node node) {
        nodes.add(node);
    }


    /**
     * Removes a node and its edges from the graph.
     * 
     * @param node
     *            the node to remove
     */
    public void removeNode(Node node) {
        if (node == null) {
            return;
        }
        for (Node neighbor : node.getAdjNodes()) {
            neighbor.removeAdjNodes(node);
        }
        node.getAdjNodes().clear();
        nodes.remove(node);
    }


    /**
     * Prints the number of connected components and the largest component size.
     * Uses array-based Union-Find with path compression.
     */
    public void printGraphStats() {
        int n = nodes.size();
        if (n == 0) {
            System.out.println("There are 0 connected components");
            System.out.println(
                "The largest connected component has 0 elements");
            return;
        }

        int[] parent = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }

        for (int i = 0; i < n; i++) {
            Node node1 = nodes.get(i);
            for (Node node2 : node1.getAdjNodes()) {
                int node1Index = i;
                int node2Index = nodes.indexOf(node2);
                if (node2Index != -1) {
                    union(node1Index, node2Index, parent);
                }
            }
        }

        int[] size = new int[n];
        int numComponents = 0;
        int maxSize = 0;

        for (int i = 0; i < n; i++) {
            int root = find(i, parent);
            size[root]++;
        }

        for (int count : size) {
            if (count > 0) {
                numComponents++;
                if (count > maxSize) {
                    maxSize = count;
                }
            }
        }

        System.out.println("There are " + numComponents
            + " connected components");
        System.out.println("The largest connected " + "component has " + maxSize
            + " elements");
    }


    /**
     * Union-Find: Finds the representative of the set containing x.
     * 
     * @param x
     *            int value
     * @param parent
     *            array of ints
     * @return int value
     */
    private int find(int x, int[] parent) {
        if (parent[x] != x) {
            parent[x] = find(parent[x], parent);
        }
        return parent[x];
    }


    /**
     * Union-Find: Unions the sets containing x and y.
     * 
     * @param x
     *            int value
     * @param y
     *            int value
     * @param parent
     *            array of ints
     */
    private void union(int x, int y, int[] parent) {
        int rootX = find(x, parent);
        int rootY = find(y, parent);
        if (rootX != rootY) {
            parent[rootX] = rootY;
        }
    }
}
