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
}
