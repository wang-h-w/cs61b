import org.xml.sax.SAXException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.LinkedList;

/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    private final Map<Long, Node> nodes = new HashMap<>(); // id -> Node
    private final Map<Long, Set<Long>> adj = new HashMap<>(); // id -> neighbors' id
    private final Set<Long> isolate = new HashSet<>(); // remove nodes without connections
    private final KdTree kd = new KdTree();
    private final Map<Long, String> wayNameDict = new HashMap<>();
    private final Trie trie = new Trie();

    /**
     * Database constructor (based on graph).
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            File inputFile = new File(dbPath);
            FileInputStream inputStream = new FileInputStream(inputFile);
            // GZIPInputStream stream = new GZIPInputStream(inputStream);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputStream, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    /**
     * Add node to the graph.
     * @param n Node waiting to be added.
     */
    void addNode(Node n) {
        this.nodes.put(n.id, n);
        this.adj.put(n.id, new HashSet<>());
        this.isolate.add(n.id);
        if (n.extraInfo.containsKey("name")) {
            this.trie.add(cleanString(n.extraInfo.get("name")));
        }
    }

    /**
     * If node's id doesn't exist in current graph, throw IllegalArgumentException.
     * @param id Node's id waiting to be validated.
     */
    private void validateNode(long id) {
        if (!this.nodes.containsKey(id)) {
            throw new IllegalArgumentException("Node" + id + "doesn't exist in current graph.");
        }
    }

    /**
     * Add edge to the graph.
     * @param e Edge waiting to be added.
     */
    void addEdge(Edge e) {
        if (e.nds.isEmpty()) {
            return;
        }
        if (e.extraInfo.containsKey("name")) {
            this.wayNameDict.put(e.wayID, e.extraInfo.get("name"));
        }
        for (int i = 0; i < e.nds.size() - 1; i++) {
            long idFrom = e.nds.get(i);
            long idTo = e.nds.get(i + 1);
            validateNode(idFrom);
            validateNode(idTo);
            this.adj.get(idFrom).add(idTo);
            this.adj.get(idTo).add(idFrom);
            this.nodes.get(idFrom).addEdgeInfo(e.wayID);
            this.nodes.get(idTo).addEdgeInfo(e.wayID);
            this.isolate.remove(idFrom);
            this.isolate.remove(idTo);
        }
    }

    Map<Long, Node> nodesMap() {
        return this.nodes;
    }

    Map<Long, String> wayNameMap() {
        return this.wayNameDict;
    }

    List<String> getPrefix(String prefix) {
        return this.trie.keyWithPrefix(prefix);
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        for (long iso: this.isolate) {
            this.nodes.remove(iso);
            this.adj.remove(iso);
        }
        for (long key: this.nodes.keySet()) {
            this.kd.add(this.nodes.get(key));
        }
    }

    /**
     * Returns an iterable of all vertex IDs in the graph.
     * @return An iterable of id's of all vertices in the graph.
     */
    Iterable<Long> vertices() {
        return this.nodes.keySet();
    }

    /**
     * Returns ids of all vertices adjacent to v.
     * @param v The id of the vertex we are looking adjacent to.
     * @return An iterable of the ids of the neighbors of v.
     */
    Iterable<Long> adjacent(long v) {
        return this.adj.get(v);
    }

    /**
     * Returns the great-circle distance between vertices v and w in miles.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The great-circle distance between the two locations from the graph.
     */
    double distance(long v, long w) {
        return distance(lon(v), lat(v), lon(w), lat(w));
    }

    static double distance(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double dphi = Math.toRadians(latW - latV);
        double dlambda = Math.toRadians(lonW - lonV);

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 3963 * c;
    }

    /**
     * Returns the initial bearing (angle) between vertices v and w in degrees.
     * The initial bearing is the angle that, if followed in a straight line
     * along a great-circle arc from the starting point, would take you to the
     * end point.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The initial bearing between the vertices.
     */
    double bearing(long v, long w) {
        return bearing(lon(v), lat(v), lon(w), lat(w));
    }

    static double bearing(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double lambda1 = Math.toRadians(lonV);
        double lambda2 = Math.toRadians(lonW);

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    long closest(double lon, double lat) {
        Node near = kd.nearest(new Node(-1, lon, lat));
        return near.id;
    }

    /**
     * Gets the longitude of a vertex.
     * @param v The id of the vertex.
     * @return The longitude of the vertex.
     */
    double lon(long v) {
        return this.nodes.get(v).lon;
    }

    /**
     * Gets the latitude of a vertex.
     * @param v The id of the vertex.
     * @return The latitude of the vertex.
     */
    double lat(long v) {
        return this.nodes.get(v).lat;
    }

    /**
     * Helper class to construct a node.
     * Have id, lon, lat and extraInfo (e.g. name).
     */
    static class Node {
        long id;
        double lon;
        double lat;
        Map<String, String> extraInfo;
        List<Long> edge;

        Node(long id, double lon, double lat) {
            this.id = id;
            this.lon = lon;
            this.lat = lat;
            this.extraInfo = new HashMap<>();
            this.edge = new LinkedList<>();
        }

        public void addEdgeInfo(long way) {
            this.edge.add(way);
        }
    }

    /**
     * Helper class to construct an edge.
     * Have nds and extraInfo (e.g. highway).
     */
    static class Edge {
        long wayID;
        List<Long> nds;
        Map<String, String> extraInfo;

        Edge(long wayID) {
            this.wayID = wayID;
            this.nds = new LinkedList<>();
            this.extraInfo = new HashMap<>();
        }

        public void addNodes(long nd) {
            this.nds.add(nd);
        }
    }
}
