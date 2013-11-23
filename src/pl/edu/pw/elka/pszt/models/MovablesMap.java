package pl.edu.pw.elka.pszt.models;

import java.util.ArrayList;

import pl.edu.pw.elka.pszt.game.Move;

public class MovablesMap {

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
   protected MovableObject[][] movables;

   public MovablesMap(int width, int height) {
       this.width = width;
       this.height = height;
       this.movables = new MovableObject[width][height];
   }

   public boolean isBarrel(int x, int y){
	   if(movables[x][y] != null) {
		   if(movables[x][y].getClass()==Barrel.class){
			   return true;
		   }
	   }
	   return false;
	   
   }
   
   public void execute(Move move){
	   MovableObject movable = movables[move.getXi()][move.getYi()];
	   movables[move.getXi()][move.getYi()] = null;
	   movables[move.getXo()][move.getYo()] = movable;
   }
 
   
   public Bulldozer getBulldozer(){
	   MovableObject movable = getMovable();
	   		if(movable.getClass()==Bulldozer.class){
			   return (Bulldozer) movable;
		   }
   	   return null;
   }   
   
   private MovableObject getMovable(){
	   for(int x=0;x< this.width;x++){
		   for(int y=0;y< this.height;y++){
			   if(movables[x][y] != null) {
				  return movables[x][y];
			   }
		   }			
	   }
	   return null;
	   
   }
   
   public int[] findBulldozer(){
	   for(int x=0;x< this.width;x++){
		   for(int y=0;y< this.height;y++){
			   if(movables[x][y] != null) {
				   if(movables[x][y].getClass() == Bulldozer.class){
					   int[] i = {x,y};
					   return i;
				   }
			   }
		   }			
	   }
	   return null;
   }
   
   public ArrayList<Barrel> getBarrels(){
	   ArrayList<Barrel> barrels= new ArrayList<Barrel>();
	   for(int x=0;x< this.width;x++){
		   for(int y=0;y< this.height;y++){
			   if(movables[x][y] != null) {
				   if(movables[x][y].getClass()==Barrel.class){
					   barrels.add((Barrel)movables[x][y]);
				   }
			   }
		   }			
	   }
	   return barrels;
   }
   
   public int[] findBarrel(Barrel barrel){
	   for(int x=0;x< this.width;x++){
		   for(int y=0;y< this.height;y++){
			   if(movables[x][y] != null) {
				   if(movables[x][y] == barrel){
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

   public MovableObject[][] getMovables() {
       return movables;
   }

   public void setMovables(MovableObject[][] mapMovables) {
       this.movables = mapMovables;
   }

   public void setMovableObjectAt(int x, int y, MovableObject movableObject) {
       this.movables[x][y] = movableObject;
   }

   @Override
   public String toString() {
       String string = String.format("Map %dx%d:\n", this.width, this.height);

       for (int y = 0; y < this.height; y++) {
    	   string += y + " ";
           for (int x = 0; x < this.width; x++) {
        	   if(this.movables[x][y] != null){
        		   string +="|" + this.movables[x][y].toString();
        	   }else {
        		   string += "|" +' ';
        	   }
           }
           string += "\n";
       }

       return string;
   }
}
