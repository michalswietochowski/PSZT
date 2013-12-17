package pl.edu.pw.elka.pszt.game;

import java.util.ArrayList;

import pl.edu.pw.elka.pszt.models.Level;

public class MoveTree {
	private Move root;
	private Move currentNode;
	private ArrayList<Move> youngest;
	private ArrayList<Move> deadEnds;
	
	public MoveTree(){
		deadEnds = new ArrayList<Move>();
	}
	
	public Move getMin(){
		return null;
		
	}
	
	
	
	public Move getRoot() {
		return root;
	}
	public void setRoot(Move root) {
		this.root = root;
		this.currentNode = this.root;
		this.youngest = new ArrayList<Move>();
		youngest.add(root);
	}
	public Move getCurrentNode() {
		return currentNode;
	}
	public void setCurrentNode(Move currentNode) {
		this.currentNode = currentNode;
	}
	
	
	public ArrayList<Move> getYoungest() {
		return youngest;
	}



	public void setYoungest(ArrayList<Move> youngest) {
		this.youngest = youngest;
	}



	public ArrayList<Move> getDeadEnds() {
		return deadEnds;
	}

	public void addDeadEnd(Move move){
		for (Move deadEnd : getDeadEnds()) {
			if(move.equals(deadEnd)){
				return;
			}
		}
		deadEnds.add(move);
	}

	public void setDeadEnds(ArrayList<Move> deadEnds) {
		this.deadEnds = deadEnds;
	}
}
