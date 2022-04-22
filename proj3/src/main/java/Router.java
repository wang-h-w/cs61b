import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {
    /**
     * Return a List of longs representing the shortest path from the node
     * closest to a start location and the node closest to the destination
     * location.
     * @param g The graph to use.
     * @param stlon The longitude of the start location.
     * @param stlat The latitude of the start location.
     * @param destlon The longitude of the destination location.
     * @param destlat The latitude of the destination location.
     * @return A list of node id's in the order visited on the shortest path.
     */
    public static List<Long> shortestPath(GraphDB g, double stlon, double stlat,
                                          double destlon, double destlat) {
        long startID = g.closest(stlon, stlat);
        long endID = g.closest(destlon, destlat);
        AStar algo = new AStar(g, startID, endID);

        return algo.getPath();
    }

    /**
     * Create the list of directions corresponding to a route on the graph.
     * @param g The graph to use.
     * @param route The route to translate into directions. Each element
     *              corresponds to a node from the graph in the route.
     * @return A list of NavigatiionDirection objects corresponding to the input
     * route.
     */
    public static List<NavigationDirection> routeDirections(GraphDB g, List<Long> route) {
        List<NavigationDirection> directions = new LinkedList<>();
        long prevNodeID;
        long currNodeID;
        long nextNodeID;
        double sumDist = 0.0;

        NavigationDirection start = new NavigationDirection();
        start.direction = NavigationDirection.START;
        start.way = getWayName(g, route.get(0), route.get(1));
        prevNodeID = route.get(0);

        for (int i = 1; i < route.size() - 1; i++) {
            currNodeID = route.get(i);
            nextNodeID = route.get(i + 1);

            // Now, you are in the current node!!!!!
            if (wayChanged(g, prevNodeID, currNodeID, nextNodeID)) {
                sumDist += g.distance(prevNodeID, currNodeID);
                start.distance = sumDist;
                directions.add(start);

//                System.out.println(NavigationDirection.DIRECTIONS[start.direction]);
//                System.out.println(start.way);
//                System.out.println(start.distance);
//                System.out.println("=======================");

                start = new NavigationDirection();
                start.direction = getDirection(g, prevNodeID, currNodeID, nextNodeID);
                start.way = getWayName(g, currNodeID, nextNodeID);
                sumDist = 0.0;
                prevNodeID = currNodeID;
            } else {
                sumDist += g.distance(prevNodeID, currNodeID);
                prevNodeID = currNodeID;
            }
        }

        start.distance = sumDist + g.distance(prevNodeID, route.get(route.size() - 1));
        directions.add(start);

        return directions;
    }

    private static boolean wayChanged(GraphDB g, long prevNodeID,
                                      long currNodeID, long nextNodeID) {
        long id1 = getWayID(g, prevNodeID, currNodeID);
        long id2 = getWayID(g, currNodeID, nextNodeID);

        String name1 = getWayNameFromID(g, id1);
        String name2 = getWayNameFromID(g, id2);

        return id1 != id2 && !name1.equals(name2);
    }

    private static String getWayName(GraphDB g, long prevNodeID, long currNodeID) {
        long wayID = getWayID(g, prevNodeID, currNodeID);
        return getWayNameFromID(g, wayID);
    }

    private static String getWayNameFromID(GraphDB g, long wayID) {
        return g.wayNameMap().getOrDefault(wayID, "");
    }

    private static long getWayID(GraphDB g, long prevNodeID, long currNodeID) {
        List<Long> prevEdges = g.nodesMap().get(prevNodeID).edge;
        List<Long> currEdges = g.nodesMap().get(currNodeID).edge;

        prevEdges.retainAll(currEdges);

        if (!prevEdges.isEmpty()) {
            return prevEdges.get(0);
        } else {
            return -1;
        }
    }

    private static int getDirection(GraphDB g, long prevNodeID, long currNodeID, long nextNodeID) {
        double bearCurr = g.bearing(prevNodeID, currNodeID);
        double bearNext = g.bearing(currNodeID, nextNodeID);
        double relativeBearing = bearNext - bearCurr;

        if (relativeBearing > 180) {
            relativeBearing -= 360;
        } else if (relativeBearing < -180) {
            relativeBearing += 360;
        }

        if (relativeBearing < -100) {
            return NavigationDirection.SHARP_LEFT;
        } else if (relativeBearing < -30) {
            return NavigationDirection.LEFT;
        } else if (relativeBearing < -15) {
            return NavigationDirection.SLIGHT_LEFT;
        } else if (relativeBearing < 15) {
            return NavigationDirection.STRAIGHT;
        } else if (relativeBearing < 30) {
            return NavigationDirection.SLIGHT_RIGHT;
        } else if (relativeBearing < 100) {
            return NavigationDirection.RIGHT;
        } else {
            return NavigationDirection.SHARP_RIGHT;
        }
    }


    /**
     * Class to represent a navigation direction, which consists of 3 attributes:
     * a direction to go, a way, and the distance to travel for.
     */
    public static class NavigationDirection {

        /** Integer constants representing directions. */
        public static final int START = 0;
        public static final int STRAIGHT = 1;
        public static final int SLIGHT_LEFT = 2;
        public static final int SLIGHT_RIGHT = 3;
        public static final int RIGHT = 4;
        public static final int LEFT = 5;
        public static final int SHARP_LEFT = 6;
        public static final int SHARP_RIGHT = 7;

        /** Number of directions supported. */
        public static final int NUM_DIRECTIONS = 8;

        /** A mapping of integer values to directions.*/
        public static final String[] DIRECTIONS = new String[NUM_DIRECTIONS];

        /** Default name for an unknown way. */
        public static final String UNKNOWN_ROAD = "unknown road";
        
        /** Static initializer. */
        static {
            DIRECTIONS[START] = "Start";
            DIRECTIONS[STRAIGHT] = "Go straight";
            DIRECTIONS[SLIGHT_LEFT] = "Slight left";
            DIRECTIONS[SLIGHT_RIGHT] = "Slight right";
            DIRECTIONS[LEFT] = "Turn left";
            DIRECTIONS[RIGHT] = "Turn right";
            DIRECTIONS[SHARP_LEFT] = "Sharp left";
            DIRECTIONS[SHARP_RIGHT] = "Sharp right";
        }

        /** The direction a given NavigationDirection represents.*/
        int direction;
        /** The name of the way I represent. */
        String way;
        /** The distance along this way I represent. */
        double distance;

        /**
         * Create a default, anonymous NavigationDirection.
         */
        public NavigationDirection() {
            this.direction = STRAIGHT;
            this.way = UNKNOWN_ROAD;
            this.distance = 0.0;
        }

        public String toString() {
            return String.format("%s on %s and continue for %.3f miles.",
                    DIRECTIONS[direction], way, distance);
        }

        /**
         * Takes the string representation of a navigation direction and converts it into
         * a Navigation Direction object.
         * @param dirAsString The string representation of the NavigationDirection.
         * @return A NavigationDirection object representing the input string.
         */
        public static NavigationDirection fromString(String dirAsString) {
            String regex = "([a-zA-Z\\s]+) on ([\\w\\s]*) and continue for ([0-9\\.]+) miles\\.";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(dirAsString);
            NavigationDirection nd = new NavigationDirection();
            if (m.matches()) {
                String direction = m.group(1);
                if (direction.equals("Start")) {
                    nd.direction = NavigationDirection.START;
                } else if (direction.equals("Go straight")) {
                    nd.direction = NavigationDirection.STRAIGHT;
                } else if (direction.equals("Slight left")) {
                    nd.direction = NavigationDirection.SLIGHT_LEFT;
                } else if (direction.equals("Slight right")) {
                    nd.direction = NavigationDirection.SLIGHT_RIGHT;
                } else if (direction.equals("Turn right")) {
                    nd.direction = NavigationDirection.RIGHT;
                } else if (direction.equals("Turn left")) {
                    nd.direction = NavigationDirection.LEFT;
                } else if (direction.equals("Sharp left")) {
                    nd.direction = NavigationDirection.SHARP_LEFT;
                } else if (direction.equals("Sharp right")) {
                    nd.direction = NavigationDirection.SHARP_RIGHT;
                } else {
                    return null;
                }

                nd.way = m.group(2);
                try {
                    nd.distance = Double.parseDouble(m.group(3));
                } catch (NumberFormatException e) {
                    return null;
                }
                return nd;
            } else {
                // not a valid nd
                return null;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof NavigationDirection) {
                return direction == ((NavigationDirection) o).direction
                    && way.equals(((NavigationDirection) o).way)
                    && distance == ((NavigationDirection) o).distance;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, way, distance);
        }
    }
}
