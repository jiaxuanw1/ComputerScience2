package grasshoppers;

public class Pixel {

    private final int x;
    private final int y;
    private int numGrasshoppers;

    public Pixel(int x, int y) {
        this.x = x;
        this.y = y;
        numGrasshoppers = 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getNumGrasshoppers() {
        return numGrasshoppers;
    }

    public void incrementGrasshopper() {
        numGrasshoppers++;
    }

    public void resetGrasshoppers() {
        numGrasshoppers = 0;
    }

    @Override
    public String toString() {
        return "Pixel{" + "x=" + x + ", y=" + y + ", grasshoppers=" + numGrasshoppers + "}";
    }

}
