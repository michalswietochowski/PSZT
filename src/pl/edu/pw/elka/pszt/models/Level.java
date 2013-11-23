package pl.edu.pw.elka.pszt.models;

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
    
    
    public int getNumber() {
        return number;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    /**
     * For testing purposes
     *
     * @return debug string
     */
    public String toString() {
    	String maps = String.format("Level %d\n", this.number) + this.map.toString();
    	maps += this.movablesMap.toString();
        return maps;
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
