package pl.edu.pw.elka.pszt.models;

/**
 * PSZT
 * Created: 10.11.2013 23:58
 */
public class Map {
    /**
     *
     */
    protected int width;
    /**
     *
     */
    protected int height;
    /**
     *
     */
    protected Location[][] locations;

    public Map(int width, int height) {
        this.width = width;
        this.height = height;
        this.locations = new Location[width][height];
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Location[][] getLocations() {
        return locations;
    }

    public void setLocations(Location[][] locations) {
        this.locations = locations;
    }

    public void setLocationAt(int x, int y, Location location) {
        this.locations[x][y] = location;
    }

    @Override
    public String toString() {
        String string = String.format("Map %dx%d:\n", this.width, this.height);

        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                string += this.locations[x][y].toString();
            }
            string += "\n";
        }

        return string;
    }
}
