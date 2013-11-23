package pl.edu.pw.elka.pszt.models;

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
   
 
   
   public Bulldozer getBulldozer(){
	   for(int x=0;x< this.width;x++){
		   for(int y=0;y< this.height;y++){
			   if(movables[x][y] != null) {
				   if(movables[x][y].getClass()==Bulldozer.class){
					   System.out.println("Buldozer " + x + ' ' + y
							   );
					   return (Bulldozer) movables[x][y];
				   }
			   }
		   }			
	   }
	   return null;
   }   
   
   public int getBulldozerX(){
	   for(int x=0;x< this.width;x++){
		   for(int y=0;y< this.height;y++){
			   if(movables[x][y] != null) {
				   if(movables[x][y].getClass()==Bulldozer.class){
					   return x;
				   }
			   }
		   }			
	   }
	   return 0;
   }   
   
   public int getBulldozerY(){
	   for(int x=0;x< this.width;x++){
		   for(int y=0;y< this.height;y++){
			   if(movables[x][y] != null) {
				   if(movables[x][y].getClass()==Bulldozer.class){
					   return y;
				   }
			   }
		   }			
	   }
	   return 0;
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
