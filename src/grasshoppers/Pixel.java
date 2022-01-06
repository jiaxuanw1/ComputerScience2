package grasshoppers;

import java.util.Objects;

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
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Pixel)) {
            return false;
        }
        Pixel p = (Pixel) o;
        return x == p.getX() && y == p.getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Pixel{" + "x=" + x + ", y=" + y + ", grasshoppers=" + numGrasshoppers + "}";
    }

}
