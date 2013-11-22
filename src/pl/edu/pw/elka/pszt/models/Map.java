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
    protected MapObject[][] mapObjects;

    public Map(int width, int height) {
        this.width = width;
        this.height = height;
        this.mapObjects = new MapObject[width][height];
    }

    public boolean canMove(int x, int y){
    	return isFloor(x,y) || isSpot(x,y);
    }
    
    private boolean isFloor(int x, int y){
    	if(mapObjects[x][y].getClass() == Floor.class){
    		return true;
    	}
    	return false;
    }
    
    private boolean isSpot(int x, int y){
    	if(mapObjects[x][y].getClass() == Spot.class){
    		return true;
    	}
    	return false;
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

    public MapObject[][] getMapObjects() {
        return mapObjects;
    }

    public void setMapObjects(MapObject[][] mapObjects) {
        this.mapObjects = mapObjects;
    }

    public void setMapObjectAt(int x, int y, MapObject mapObject) {
        this.mapObjects[x][y] = mapObject;
    }

    @Override
    public String toString() {
        String string = String.format("Map %dx%d:\n", this.width, this.height);
        
        for (int y = 0; y < this.height; y++) {
        	string += y + " ";
            for (int x = 0; x < this.width; x++) {
                string += "|" + this.mapObjects[x][y].toString();
            }
            string += "\n";
        }

        return string;
    }
}
