package pl.edu.pw.elka.pszt.models;

import java.util.ArrayList;

import pl.edu.pw.elka.pszt.game.Move;

/**
 * PSZT
 * Created: 11.11.2013 00:44
 */
public class Level {

    protected int number;
    protected Map map;
    protected MovablesMap movablesMap;
    

    public Level(int number) {
        this.number = number;
    }

    public boolean canMove(Move nextMove){
    	int x,y;
    	x= nextMove.getXo();
    	y = nextMove.getYo();
    	
    	if(map.canMove(x,y)){
    		if(movablesMap.isBarrel(x,y)){
    			nextMove = nextMove.calcNextMove('F');
    			x= nextMove.getXo();
    	    	y = nextMove.getYo();
    	    	if(map.canMove(x,y)){
    	    		if(movablesMap.isBarrel(x,y)){
    	    			return false;
    	    		}
    	    		return true;
    	    	}
    		}else {
    			return true;
    		}
    	}
    	return false;
    }
    
    public void move(Move nextMove){
    	if(movablesMap.isBarrel(nextMove.getXo(), nextMove.getYo())){
    		Move barrelMove = nextMove.calcNextMove('F');
    		movablesMap.execute(barrelMove);
    		nextMove.setMovedBarrel(true);
    	}
    	movablesMap.execute(nextMove);
    	
    }
    
    public boolean barellAtCorner(Barrel barrel) {
	
		int walls = barrelWalls(barrel);
		
		ArrayList<Barrel> barrels = getMovablesMap().getBarrels();
		/*for (Barrel barrel2 : barrels) {
			//if(!barrel2.equals(barrel)){
				if(areNeighbours(barrel, barrel2)){
					walls+= barrelWalls(barrel2);
					if(walls>1){
						return true;
					}
				}
			//}
		}*/
		
		
		
		if(walls>1){
			return true;
		}else 
			return false;
	} 
    
    public int barrelWalls(Barrel barrel){
    	int[] coords = getMovablesMap().findBarrel(barrel);
    	/*System.out.println("coords " + coords[0] + coords[1]);
    	System.out.println("coords " + !map.canMove(coords[0]-1, coords[1]));
    	System.out.println("coords " + !map.canMove(coords[0]+1, coords[1]));
    	System.out.println("coords " + !map.canMove(coords[0], coords[1]-1));
    	System.out.println("coords " + !map.canMove(coords[0], coords[1]+1));
    	*/
		int walls =0;
		if(!map.canMove(coords[0]-1, coords[1])){
			walls++;
		}
		if(!map.canMove(coords[0]+1, coords[1])){
			walls++;
		}
		if(!map.canMove(coords[0], coords[1]-1)){
			walls++;
		}
		if(!map.canMove(coords[0], coords[1]+1)){
			walls++;
		}
		if(coords[0]==4 && coords[1]==4){
			return walls;
		}
		if(walls>1)
			return walls;
		return walls;
    }
    
    private boolean areNeighbours(Barrel barrel, Barrel barrel2){
    	int[] coords = getMovablesMap().findBarrel(barrel);
    	int[] coords2 = getMovablesMap().findBarrel(barrel2);
		
		if((Math.abs(coords[0] -coords2[0]) + Math.abs(coords[1] -coords2[1]))==1){
			return true;
		}
		return false;
			
    }
    
    public int getNumber() {
        return number;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public String toString() {
        return String.format("Level %d", this.number);
    }

    /**
     * For testing purposes
     *
     * @return debug string
     */
    public String dump() {
        return this.toString() + this.map + this.movablesMap;
    }

	public MovablesMap getMovablesMap() {
		return movablesMap;
	}

	public void setMovablesMap(MovablesMap movablesMap) {
		this.movablesMap = movablesMap;
	}
	
	public Bulldozer getBulldozer(){
		return movablesMap.getBulldozer();
	}

	
}
