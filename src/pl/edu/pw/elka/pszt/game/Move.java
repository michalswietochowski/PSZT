package pl.edu.pw.elka.pszt.game;

public class Move {
	private int xi, yi, xo, yo;

	public Move(int xi, int yi, int xo, int yo){
		this.xi =xi;
		this.yi=yi;
		this.xo = xo;
		this.yo= yo;
		
	}
	
	public Move calcNextMove(char to){
		int diffX = xo-xi;
		int diffY = yo-yi;
		Move nextMove=null;
		
		if(to=='F'){
			nextMove = new Move(xo, yo, xo+diffX, yo+diffY);
		}else if(to=='L'){
			nextMove = new Move(xo, yo, xo+diffY, yo-diffX);
		}else if(to=='R'){
			nextMove = new Move(xo, yo, xo-diffY, yo+diffX);
		}
		
		System.out.println("Next move " + nextMove.toString());
		return nextMove;
	}
	
	
	
	
	public int getYo() {
		return yo;
	}

	public void setYo(int yo) {
		this.yo = yo;
	}

	public int getXi() {
		return xi;
	}

	public void setXi(int xi) {
		this.xi = xi;
	}

	public int getYi() {
		return yi;
	}

	public void setYi(int yi) {
		this.yi = yi;
	}

	public int getXo() {
		return xo;
	}

	public void setXo(int xo) {
		this.xo = xo;
	}
	
	 @Override
	   public String toString() {
		 return String.format("Move from %d,%d to %d,%d", this.xi, this.yi, this.xo, this.yo);
		 
	 }
	
}
