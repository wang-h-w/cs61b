import edu.princeton.cs.algs4.Picture;
import java.awt.Color;

public class SeamCarver {
    private final Picture pic;
    private final int width;
    private final int height;
    private final double[][] energyMap;
    private final double[][] M;
    private final int[][] last;
    private final int[][] track;

    public SeamCarver(Picture picture) {
        this.pic = new Picture(picture);
        this.width = pic.width();
        this.height = pic.height();

        int max = Math.max(width, height);
        this.energyMap = new double[max][max];
        this.M = new double[max][max];
        this.last = new int[max][max];
        this.track = new int[max][max];
    }

    public Picture picture() {
        return this.pic;
    }

    public int width() {
        return this.width;
    }

    public int height() {
        return this.height;
    }

    public double energy(int x, int y) {
        Color up, down, left, right;
        up = y > 0 ? this.pic.get(x, y - 1) : this.pic.get(x, height - 1);
        down = y < height - 1 ? this.pic.get(x, y + 1) : this.pic.get(x, 0);
        left = x > 0 ? this.pic.get(x - 1, y) : this.pic.get(width - 1, y);
        right = x < width - 1 ? this.pic.get(x + 1, y) : this.pic.get(0, y);

        double dv = gradient(up, down);
        double dh = gradient(left, right);

        return dv + dh;
    }

    private double gradient(Color prev, Color next) {
        int dr = next.getRed() - prev.getRed();
        int dg = next.getGreen() - prev.getGreen();
        int db = next.getBlue() - prev.getBlue();

        return dr * dr + dg * dg + db * db;
    }

    private void getEnergyAndMAndTrack() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                this.energyMap[j][i] = energy(j, i);
                this.M[j][i] = M(j, i);
            }
            System.arraycopy(this.last, 0, this.track, 0, width);
        }
    }

    public int[] findHorizontalSeam() {
        return null;
    }

    public int[] findVerticalSeam() {
        getEnergyAndMAndTrack();

        double min = Double.MAX_VALUE;
        int minIdx = 0;
        for (int i = 0; i < width; i++) {
            if (min > this.M[i][height - 1]) {
                min = this.M[i][height - 1];
                minIdx = i;
            }
        }

        int[] result = new int[height];
        System.arraycopy(this.track[minIdx], 0, result, 0, height);

        return result;
    }

    private double M(int i, int j) {
        double[] min;

        if (j == 0) {
            this.last[i][j] = i;
            return this.energyMap[i][j];
        }
        if (width == 1) {
            return M[i][j - 1] + this.energyMap[i][j];
        }
        if (i <= 0) {
            min = getMin(Double.MAX_VALUE, M[i][j - 1], M[i + 1][j - 1]);
        } else if (i >= width - 1) {
            min = getMin(M[i - 1][j - 1], M[i][j - 1], Double.MAX_VALUE);
        } else {
            min = getMin(M[i - 1][j - 1], M[i][j - 1], M[i + 1][j - 1]);
        }

        System.arraycopy(this.track[(int) (i + min[1])], 0, last[i], 0, j);
        last[i][j] = i;

        return this.energyMap[i][j] + min[0];
    }

    /**
     * @return min -> double[]: min[0] -> minimum value, min[1] -> offset
     */
    private double[] getMin(double topLeft, double topMiddle, double topRight) {
        double[] min = new double[] {topLeft, -1};
        if (min[0] > topMiddle) {
            min[0] = topMiddle;
            min[1] = 0;
        }
        if (min[0] > topRight) {
            min[0] = topRight;
            min[1] = 1;
        }
        return min;
    }

    public void removeHorizontalSeam(int[] seam) {
        if (seam.length != width || !isValidSeam(seam)) {
            throw new IllegalArgumentException();
        }
        SeamRemover.removeHorizontalSeam(this.pic, seam);
    }

    public void removeVerticalSeam(int[] seam) {
        if (seam.length != height || !isValidSeam(seam)) {
            throw new IllegalArgumentException();
        }
        SeamRemover.removeVerticalSeam(this.pic, seam);
    }

    private boolean isValidSeam(int[] seam) {
        for (int i = 0, j = 1; j < seam.length; i++, j++) {
            if (Math.abs(seam[i] - seam[j]) > 1) {
                return false;
            }
        }
        return true;
    }
}
