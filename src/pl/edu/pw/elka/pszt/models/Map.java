package pl.edu.pw.elka.pszt.models;

import java.util.ArrayList;

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
    
    public boolean isSpot(int x, int y){
    	if(mapObjects[x][y].getClass() == Spot.class){
    		return true;
    	}
    	return false;
    }
    
    public ArrayList<Spot> getSpots(){
 	   ArrayList<Spot> spots= new ArrayList<Spot>();
 	   for(int x=0;x< this.width;x++){
 		   for(int y=0;y< this.height;y++){
 			   if(mapObjects[x][y] != null) {
 				   if(mapObjects[x][y].getClass()==Spot.class){
 					   spots.add((Spot)mapObjects[x][y]);
 				   }
 			   }
 		   }			
 	   }
 	   return spots;
    }
    
    public int[] findSpot(Spot spot){
 	   for(int x=0;x< this.width;x++){
 		   for(int y=0;y< this.height;y++){
 			   if(mapObjects[x][y] != null) {
 				   if(mapObjects[x][y] == spot){
 					   int[] i = {x,y};
 					   return i;
 				   }
 			   }
 		   }			
 	   }
 	   return null;
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

    public MapObject getMapObject(int x, int y) {
        return this.mapObjects[x][y];
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
