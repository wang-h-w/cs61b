package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Arrays;
import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;
    private static final Random RANDOM = new Random();
    private static int[][] mask;
    private static int sideLength;
    private static int width;
    private static int height;
    private final TETile[][] tiles;

    public HexWorld(int sL) {
        sideLength = sL;
        height = sideLength * 2;
        width = 3 * sideLength - 2;
        tiles = new TETile[HEIGHT][WIDTH];
    }

    /**
     * Initialize world -> NOTHING.
     */
    public void initWorld() {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }

    /**
     * Calculating the mask of hexagon in a square area.
     * @param sideLength side length
     */
    public int[][] calcSquareMask(int sideLength) {
        int[][] mask = new int[width][height];
        for (int i = 0; i < width; i++) {
            Arrays.fill(mask[i], 1);
        }
        for (int i = 0; i < sideLength; i++) {
            for (int j = 0; j < sideLength - 1 - i; j++) {
                mask[i][j] = 0;
                mask[i][height - 1 - j] = 0;
            }
            mask[width - 1 - i] = mask[i];
        }
        return mask;
    }

    private boolean checkMask(int i, int j) {
        if (mask == null) {
            mask = calcSquareMask(sideLength);
        }
        return mask[i][j] == 1;
    }

    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(7);
        return switch (tileNum) {
            case 0 -> Tileset.WALL;
            case 1 -> Tileset.FLOWER;
            case 2 -> Tileset.WATER;
            case 3 -> Tileset.UNLOCKED_DOOR;
            case 4 -> Tileset.SAND;
            case 5 -> Tileset.MOUNTAIN;
            case 6 -> Tileset.TREE;
            default -> Tileset.FLOOR;
        };
    }

    /**
     * Add a hexagon to the world.
     * @param posX start x position (square area)
     * @param posY start y position (square area)
     * */
    private void addHex(int posX, int posY) {
        TETile tileType = randomTile();
        for (int x = posX; x < posX + width; x += 1) {
            for (int y = posY; y < posY + height; y += 1) {
                if (checkMask(x - posX, y - posY)) {
                    tiles[x][y] = tileType;
                }
            }
        }
    }

    /**
     * Draw the whole world!
     */
    public void drawHexWorld() {
        for (int i = 0; i < 3; i++) {
            int startY = (2 - i) * sideLength;
            for (int j = 0; j < i + 3; j++) {
                addHex(i * (2 * sideLength - 1), startY + j * 2 * sideLength);
                addHex((4 - i) * (2 * sideLength - 1), startY + j * 2 * sideLength);
            }
        }
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT, 5, 5);
        HexWorld hex = new HexWorld(4);
        hex.initWorld();
        hex.drawHexWorld();
        ter.renderFrame(hex.tiles);
    }
}
