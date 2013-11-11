package pl.edu.pw.elka.pszt.models;

import java.util.Set;

/**
 * PSZT
 * Created: 10.11.2013 23:59
 */
public class Location {

    protected int x;
    protected int y;
    protected Set<MapObject> objects;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Location(int x, int y, Set<MapObject> objects) {
        this.x = x;
        this.y = y;
        this.objects = objects;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Set<MapObject> getObjects() {
        return objects;
    }

    public void setObjects(Set<MapObject> objects) {
        this.objects = objects;
    }
}
