package pl.edu.pw.elka.pszt.game;

import java.util.ArrayList;

import pl.edu.pw.elka.pszt.models.Level;

public class MoveTree {
	private Move root;
	private Move currentNode;
	private ArrayList<Move> youngest;
	private ArrayList<Move> deadEnds;
	private ArrayList<Move> checkedMoves;
	
	private int loopRemove;	
	
	public MoveTree(){
		deadEnds = new ArrayList<Move>();
		checkedMoves = new ArrayList<Move>();
		//checkedMoves.add(root);
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
		this.root.setF(-1);
		//checkedMoves.add(root);
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
	
	public ArrayList<Move> getBest() {
	   return findBest(getAllNodes());
	}

	public ArrayList<Move> getAllNodes() {
		//System.out.println("ROOT IS" + root);
		//System.out.println("ALL CH" + root.getChildren());
		return removeDuplicates(getChildren(root));
	}
	
	public ArrayList<Move> getChildren(Move parent){
		ArrayList<Move> children= new ArrayList<Move>();
		if(parent.getChildren() == null || parent.getChildren().size()==0){
			if(parent.isDeadEnd()==false){
				children.add(parent); 
			}
		}else{
			
			for (Move child : parent.getChildren()) {
				ArrayList<Move> ch = getChildren(child);
				if(ch != null) {
				children.addAll(ch);
				}
			}
			if(parent.isDeadEnd()==false){
				children.add(parent); 
			}
		}
		
		return children;
	}
	
	public ArrayList<Move> removeDuplicates(ArrayList<Move> moves){
		ArrayList<Move> noduplicates= new ArrayList<Move>();
		boolean isdupl=false;
		for (Move move : moves) {
			for (Move nodupl : noduplicates) {
				if(nodupl.equals(move) && nodupl.getF()==move.getF()){
					isdupl= true;
					break;
				}
			}
			if(!isdupl){
				noduplicates.add(move);
			}
			isdupl =false;
		}
		return removeChecked(noduplicates);
	}
	
	public ArrayList<Move> removeChecked(ArrayList<Move> moves){
		ArrayList<Move> nochecked= new ArrayList<Move>();
		for (Move move : moves) {
			if(move.wasChecked()==false){
				Move iterMove = move;
				boolean moved = false;
				boolean eq = false;
				int count =0;
				while(iterMove.getParent()!=getRoot() &&  iterMove.getParent()!=null){
					iterMove =iterMove.getParent();
					if(iterMove.isMovedBarrel()){
						moved =true;
						break;
					}
					if(iterMove.equals(move)){
						eq= true;
						break;
					}
					count++;
					if(count>loopRemove){
						eq= true;
						break;
					}
					
				}
				if(!moved && 
						eq){
					move.setDeadEnd(true);
					move.setChecked(true);
				}else{
					nochecked.add(move);
				}
			}
		}
		
		
		
		return nochecked;
	}
	
	public ArrayList<Move> findBest(ArrayList<Move> moves){
		ArrayList<Move> bestlist =  new ArrayList<Move>();;
		Move best = null;
		int fmin = 100000;
		for (Move move : moves) {
			if(move.wasChecked()==false && move.isDeadEnd()==false){
				if(move.getF()<fmin){
					bestlist.clear();
					bestlist.add(move);
					fmin = move.getF();
					
				}else if(move.getF()==fmin){
					bestlist.add(move);
				}
			}
		}
		/*
		if(best != null){
			bestlist.add(best);
		}*/
		return bestlist;
		
		/*int fmin = 1000;
		for (Move move : moves) {
			if(move.getF()<fmin){
				bestlist =  new ArrayList<Move>();
				bestlist.add(move);
				fmin = move.getF();
			}else if(move.getF()==fmin){
				bestlist.add(move);
			}
		}
		return bestlist;
		*/
	}
	
	
	
	public void setYoungest(ArrayList<Move> youngest) {
		this.youngest = youngest;
	}

	public void setcheckedMoves(ArrayList<Move> youngest) {
		this.checkedMoves.addAll(youngest);
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

	public int getLoopRemove() {
		return loopRemove;
	}

	public void setLoopRemove(int loopRemove) {
		this.loopRemove = loopRemove;
	}
}
