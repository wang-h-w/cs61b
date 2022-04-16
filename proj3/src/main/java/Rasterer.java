import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    String[][] renderGrid;
    double rasterUlLon;
    double rasterUlLat;
    double rasterLrLon;
    double rasterLrLat;
    int depth;
    boolean querySuccess;
    int rootNum;
    double perLon;
    double perLat;
    static final double ROOT_ULLAT = 37.892195547244356, ROOT_ULLON = -122.2998046875,
            ROOT_LRLAT = 37.82280243352756, ROOT_LRLON = -122.2119140625;
    static final int TILE_SIZE = 256;
    static final int MAX_DEPTH = 7;

    public Rasterer() {
        renderGrid = null;
        rasterUlLon = 0;
        rasterUlLat = 0;
        rasterLrLon = 0;
        rasterLrLat = 0;
        depth = 0;
        querySuccess = false;
        rootNum = 0;
        perLon = 0;
        perLat = 0;
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        // System.out.println(params);
        Map<String, Object> results = new HashMap<>();

        // get query parameters
        double ullat = params.get("ullat");
        double ullon = params.get("ullon");
        double lrlat = params.get("lrlat");
        double lrlon = params.get("lrlon");
        double w = params.get("w");

        // validate query
        if (ullon >= lrlon || ullat <= lrlat || ullon >= ROOT_LRLON
                || lrlon <= ROOT_ULLON || ullat <= ROOT_LRLAT || lrlat >= ROOT_ULLAT) {
            return returnNull(results);
        } else {
            querySuccess = true;
        }

        // get numerical info
        depth = getDepth(ullon, lrlon, w);
        rootNum = (int) Math.pow(2, depth);
        perLon = Math.abs(ROOT_LRLON - ROOT_ULLON) / rootNum;
        perLat = Math.abs(ROOT_ULLAT - ROOT_LRLAT) / rootNum;

        // fill in render_grid map O(k)
        int[] idx = getRasterCornersIdx(ullon, ullat, lrlon, lrlat);
        renderGrid = fillRenderGrid(idx);

        // fill in raster positions
        double[] pos = fillRasterPos(idx);
        rasterUlLon = pos[0];
        rasterUlLat = pos[1];
        rasterLrLon = pos[2];
        rasterLrLat = pos[3];

        // add to results map
        results.put("query_success", querySuccess);
        results.put("render_grid", renderGrid);
        results.put("raster_ul_lon", rasterUlLon);
        results.put("raster_ul_lat", rasterUlLat);
        results.put("raster_lr_lon", rasterLrLon);
        results.put("raster_lr_lat", rasterLrLat);
        results.put("depth", depth);

        return results;
    }

    private Map<String, Object> returnNull(Map<String, Object> results) {
        results.put("query_success", false);
        results.put("render_grid", null);
        results.put("raster_ul_lon", 0);
        results.put("raster_ul_lat", 0);
        results.put("raster_lr_lon", 0);
        results.put("raster_lr_lat", 0);
        results.put("depth", 0);

        return results;
    }

    private double getLonDPP(double ullon, double lrlon, double width) {
        return (lrlon - ullon) / width;
    }

    private int getDepth(double ullon, double lrlon, double width) {
        double queryDPP = getLonDPP(ullon, lrlon, width);
        double maxDPP = getLonDPP(ROOT_ULLON, ROOT_LRLON, TILE_SIZE);
        int d = (int) Math.ceil(Math.log(maxDPP / queryDPP) / Math.log(2));
        if (d >= MAX_DEPTH) {
            d = MAX_DEPTH;
        }
        return d;
    }

    private int[] getRasterCornersIdx(double ullon, double ullat, double lrlon, double lrlat) {
        int[] idx = new int[4];  // same order with params

        idx[0] = getLonCorner((ullon - ROOT_ULLON) / perLon, rootNum);
        idx[1] = getLatCorner((ullat - ROOT_LRLAT) / perLat, rootNum);
        idx[2] = getLonCorner((lrlon - ROOT_ULLON) / perLon, rootNum);
        idx[3] = getLatCorner((lrlat - ROOT_LRLAT) / perLat, rootNum);

        return idx;
    }

    private int getLonCorner(double x, int size) {
        if (x < 0) {
            return 0;
        } else if (x > size) {
            return size - 1;
        } else {
            return (int) Math.floor(x);
        }
    }

    private int getLatCorner(double x, int size) {
        if (x < 0) {
            return size - 1;
        } else if (x > size) {
            return 0;
        } else {
            return (int) (size - Math.ceil(x));
        }
    }

    private String[][] fillRenderGrid(int[] cornersIdx) {
        int rasterWidthNum = cornersIdx[3] - cornersIdx[1] + 1;
        int rasterHeightNum = cornersIdx[2] - cornersIdx[0] + 1;
        renderGrid = new String[rasterWidthNum][rasterHeightNum];

        int rPointer = 0;
        int cPointer = 0;
        for (int c = cornersIdx[1]; c <= cornersIdx[3]; c++) {
            for (int r = cornersIdx[0]; r <= cornersIdx[2]; r++) {
                renderGrid[rPointer][cPointer] = "d" + depth + "_x" + r + "_y" + c + ".png";
                cPointer++;
            }
            rPointer++;
            cPointer = 0;
        }

        return renderGrid;
    }

    private double[] fillRasterPos(int[] cornersIdx) {
        double[] corner = new double[4];
        corner[0] = ROOT_ULLON + cornersIdx[0] * perLon;
        corner[1] = ROOT_ULLAT - cornersIdx[1] * perLat;
        corner[2] = ROOT_ULLON + (cornersIdx[2] + 1) * perLon;
        corner[3] = ROOT_ULLAT - (cornersIdx[3] + 1) * perLat;

        return corner;
    }

    public static void main(String[] args) {
        Rasterer r = new Rasterer();
        double ullon = -122.24163047377972;
        double lrlon = -122.24053369025242;
        double ullat = 37.87655856892288;
        double lrlat = 37.87548268822065;
        double w = 892;
        double h = 875;

        Map<String, Double> params = new HashMap<>();
        params.put("ullon", ullon);
        params.put("lrlon", lrlon);
        params.put("ullat", ullat);
        params.put("lrlat", lrlat);
        params.put("w", w);
        params.put("h", h);

        Map<String, Object> results = r.getMapRaster(params);
        System.out.println(results);
        String[][] s = (String[][]) results.get("render_grid");
        for (String[] strings : s) {
            for (int j = 0; j < s[0].length; j++) {
                System.out.println(strings[j]);
            }
        }
    }
}
