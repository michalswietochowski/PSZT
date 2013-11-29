package pl.edu.pw.elka.pszt.game;

import java.util.ArrayList;

import pl.edu.pw.elka.pszt.models.Level;

public class Move {
	private int xi, yi, xo, yo;
	private boolean movedBarrel=false;
	private ArrayList<Move> children;
	private Move parent;
	private boolean isDeadEnd;
	private int f;
	//private Level level;
	
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
		
		//System.out.println("Next move " + nextMove.toString());
		return nextMove;
	}
	public boolean areAllChildrenDeadEnd(){
		if(children== null){
			return false;
		}else {
			for (Move child : children) {
				if(!child.isDeadEnd()){
					return false;
				}
			}
		}
		return true;
		
	}
	
	public Move getMinChild(){
		return null;
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
		 return String.format("Move from %d,%d to %d,%d barrel %b", this.xi, this.yi, this.xo, this.yo, this.movedBarrel);
		 
	 }
	 @Override
	 public boolean equals(Object obj) {
	        if (obj == null) return false;
	        if (getClass() != obj.getClass()) return false;
	        final Move other = (Move) obj;
	        if(this.xi==other.xi){
	        	if(this.xo==other.xo){
	        		if(this.yi==other.yi){
	        			if(this.yo==other.yo){
	        				//if(this.movedBarrel==other.movedBarrel){
	        					return true;
	        		        //}
	        	        }
	    	        }
		        }
	        	
	        }return false;
	    }

	public boolean isMovedBarrel() {
		return movedBarrel;
	}

	public void setMovedBarrel(boolean movedBarrel) {
		this.movedBarrel = movedBarrel;
	}

	public Move getParent() {
		return parent;
	}

	public void setParent(Move parent) {
		this.parent = parent;
	}

	public boolean isDeadEnd() {
		return isDeadEnd;
	}

	public void setDeadEnd(boolean isDeadEnd) {
		this.isDeadEnd = isDeadEnd;
	}

	public ArrayList<Move> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<Move> children) {
		this.children = children;
	}
	
	public int getSize(){
		if(parent == null){
			return 0;
		}else 
			return parent.getSize() +1;
	}

	public int getF() {
		return f;
	}

	public void setF(int f) {
		this.f = f;
	}
/*
	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}*/
}
