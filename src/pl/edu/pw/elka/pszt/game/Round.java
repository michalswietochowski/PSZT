package pl.edu.pw.elka.pszt.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import pl.edu.pw.elka.pszt.models.Barrel;
import pl.edu.pw.elka.pszt.models.BarrelSpotPair;
import pl.edu.pw.elka.pszt.models.Level;
import pl.edu.pw.elka.pszt.models.Spot;

public class Round {

	private Level level;
	//private ArrayList<Move> availableMoves;
	//private ArrayList<Move> forbidenMoves;
	//private ArrayList<Move> executedMoves;
	private MoveTree moveTree;
	
	private ArrayList<BarrelSpotPair<Barrel, Spot>> barrelSpotPairs;
	private BarrelSpotPair<Barrel, Spot> executingPair;
	
	//private Move lastMove;
	
	public Round(Level level, Move initialMove){
		this.level = level;
		/*availableMoves = new ArrayList<Move>();
		forbidenMoves = new ArrayList<Move>();
		executedMoves = new ArrayList<Move>();
		this.executedMoves.add(initialMove);
		*/
		moveTree = new MoveTree();
		moveTree.setRoot(initialMove);
		
	}
	
	public Round(Level level){
		this.level = level;
		/*availableMoves = new ArrayList<Move>();
		forbidenMoves = new ArrayList<Move>();
		executedMoves = new ArrayList<Move>();*/
		moveTree = new MoveTree();
	}

	public void start(ArrayList<BarrelSpotPair<Barrel, Spot>> barrelSpotPairs, Move initMove){
		/*this.barrelSpotPairs =barrelSpotPairs;
		this.executingPair = this.barrelSpotPairs.get(0);
		this.executedMoves.add(initMove);
		int count =0;
		for (BarrelSpotPair<Barrel, Spot> barrelSpotPair : this.barrelSpotPairs) {
			while(h(barrelSpotPair)!=1){//jaki dodatkowy warunek, zêby sie nie krêci³ do usranej œmierci?
				checkMoves();
				Move minMove = findMinMove();
				System.out.println("min move " + minMove
						);
				if(minMove != null){
					move(minMove);
				}else {
					System.out.println("goback");
					goBack(executedMoves.get(executedMoves.size()-1).isMovedBarrel());
				}
				System.out.println("move # " + count++);
				System.out.println(level.getMovablesMap().toString());
				try {
					System.in.read();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}*/
	}
	
	public void checkMoves(){
		checkMoves(moveTree.getCurrentNode());
	}
	
	public void checkMoves(Move move){
		ArrayList<Move> children = new ArrayList<Move>();
		if(checkMove(move, 'F')) {
			children.add(moveTree.getCurrentNode().calcNextMove('F'));
		}
			//availableMoves.add(executedMoves.get(executedMoves.size()-1).calcNextMove('F'));
		if(checkMove(move, 'L')) 
			children.add(moveTree.getCurrentNode().calcNextMove('L'));
			//availableMoves.add(executedMoves.get(executedMoves.size()-1).calcNextMove('L'));
		if(checkMove(move, 'R')) 
			children.add(moveTree.getCurrentNode().calcNextMove('R'));
			//availableMoves.add(executedMoves.get(executedMoves.size()-1).calcNextMove('R'));
		moveTree.getCurrentNode().setChildren(children);
		/*System.out.println("forbiden" + forbidenMoves.size() + " " + forbidenMoves);
		removeDuplicatesFromMoves();
		System.out.println("executed" + executedMoves.size() + " " + executedMoves);
		System.out.println("available " + availableMoves);*/
		if(children.size()==0){
			moveTree.getCurrentNode().setDeadEnd(true);
			moveTree.setCurrentNode(moveTree.getCurrentNode().getParent());
		}
	}
	
	public boolean checkMove(Move move, char to){
		//Move nextMove;
		Move nextMove = move.calcNextMove(to);
		/*for (Move forbidenMove : forbidenMoves) {
			if(forbidenMove.equals(nextMove)){
				return false;
			}
		}
		*/
		boolean canMove = level.canMove(nextMove);
		if(nextMove.areAllChildrenDeadEnd()){
			return false;
		}
		
		
		/*if(!canMove){
			forbidenMoves.add(nextMove);
		}
		for (Move executedMove : executedMoves) {
			if(executedMove.equals(nextMove)){
				return false;
			}
		}
		*/
		
		return canMove;
	}
	
	/*
	private void removeDuplicatesFromMoves(){
		ArrayList<Move> forbidenMovesNew = new ArrayList<Move>();
		/*ArrayList<Move> executedMovesNew = new ArrayList<Move>();
		for (Move executed : executedMoves) {
			
		}*//*
		for (Move forbiden : forbidenMoves) {
			for (Move move : forbidenMovesNew) {
				//System.out.println("porónanie " + move + " " + forbiden);
				if(forbiden.equals(move)){
					//System.out.println("wykryl zawieranie");
				}
			}
			if(contains(forbidenMovesNew, forbiden)){
				forbidenMovesNew = remove(forbidenMovesNew, forbiden);
				System.out.println("wykryl zawieranie");
			}
			//System.out.println("forbiden after cleaning " +  forbidenMovesNew.size());
			//System.out.println("forbiden cleaning " + forbiden);
			forbidenMovesNew.add(forbiden);
			//
			
			
		}
		System.out.println("forbiden after cleaning " +  forbidenMovesNew.size() + " " + forbidenMovesNew);		
		//forbidenMoves.clear();
		forbidenMoves = forbidenMovesNew;
		//executedMoves.clear();
		//executedMoves = executedMovesNew;
	}
	*/
	
	private boolean contains(ArrayList<Move> list, Move move){
		for (Move tmove : list) {
			if(move.equals(tmove)){
				
				return true;
			}
		}
		return false;
	}
	
	private ArrayList<Move>  remove(ArrayList<Move> list, Move move){
		ArrayList<Move> listnew = new ArrayList<Move>(list);
		for (Move tmove : list) {
			if(move.equals(tmove)){
				listnew.remove(tmove);				
			}
		}
		list = listnew;
		//System.out.println("list after removal " + list );
		return listnew;
	}
	
	public void move(Move nextMove){
		level.move(nextMove);
		System.out.println("loking for " + nextMove.toString());
		for (Move child : moveTree.getCurrentNode().getChildren()) {
			System.out.println(child);
			if(child.equals(nextMove)){
				System.out.println("found" + child);
				if(nextMove.isMovedBarrel()) {
					child.setMovedBarrel(true);
				}
				child.setParent(moveTree.getCurrentNode());
				moveTree.setCurrentNode(child);
			}
			int i =0;
			i++;
		}
		//availableMoves = remove(availableMoves, nextMove);
		
	}
	
	
	
	public Move findMinMove(){
		
		Move minMove = null;//= availableMoves.get(0);
		//moveMin(minMove);
		int fmin =0;//= g() + h(executingPair);
		//goBackMin(availableMoves.get(0).isMovedBarrel());
		
		for (Move move: moveTree.getCurrentNode().getChildren()){
			
			
			
			/*if(!forbidenMoves.contains(availableMoves.get(i))){
				if(minMove == null){
					minMove = availableMoves.get(i);
					moveMin(minMove);
					fmin = g() + h(executingPair);
					
				}else 				
				if(!minMove.equals(availableMoves.get(i))){
					moveMin(availableMoves.get(i));
					if( g() + h(executingPair) < fmin) {
						fmin = g() + h(executingPair);
						minMove = availableMoves.get(i);
						}
					}
				goBackMin();
			}*/
		}
		return minMove;
	}
	/*
	public void moveMin(Move nextMove){
		level.move(nextMove);
		//executedMoves.add(nextMove);
		//System.out.println( "moved " + nextMove.toString());
	}*/
	
	public void goBackMin(//boolean movedBarrel
			){
		/*
		Move lastMove = executedMoves.get(executedMoves.size()-1);
		Move reverseMove = new Move(
				lastMove.getXo(),
				lastMove.getYo(),
				lastMove.getXi(),
				lastMove.getYi()
				);
		level.move(reverseMove);
		//System.out.println("want to remove " + lastMove);
		executedMoves = remove(executedMoves, lastMove);
		//System.out.println("want to remove " + reverseMove);
		executedMoves = remove(executedMoves, reverseMove);
		
		if(lastMove.isMovedBarrel()){
			int diffX = lastMove.getXo() - lastMove.getXi();
			int diffY = lastMove.getYo() - lastMove.getYi();
			reverseMove = new Move(
				lastMove.getXo() + diffX,
				lastMove.getYo() + diffY,
				lastMove.getXi() + diffX,
				lastMove.getYi() + diffY
				);
			level.move(reverseMove);
			executedMoves = remove(executedMoves, lastMove);
			executedMoves = remove(executedMoves, reverseMove);
			//availableMoves.add(lastMove);
		}*/
	}
	
	public int g(){
		return moveTree.getCurrentNode().getSize();
	}
	
	
	public int h(BarrelSpotPair<Barrel, Spot> pair){
		int[] barrel = level.getMovablesMap().findBarrel(pair.getLeft());
		int[] spot = level.getMap().findSpot(pair.getRight());
		int[] bulldozer = level.getMovablesMap().findBulldozer();
		int h1 = Math.abs(barrel[0] -bulldozer[0]) + Math.abs(barrel[1] -bulldozer[1]);
		int h2 = Math.abs(barrel[0] -spot[0]) + Math.abs(barrel[1] -spot[1]);
		return h1+h2;
		
	}
	
	public void goBack(boolean movedBarrel){
		/*Move lastMove = executedMoves.get(executedMoves.size()-1);
		Move reverseMove = new Move(
				lastMove.getXo(),
				lastMove.getYo(),
				lastMove.getXi(),
				lastMove.getYi()
				);
		move(reverseMove);
		forbidenMoves.add(lastMove);
		executedMoves = remove(executedMoves, lastMove);
		executedMoves = remove(executedMoves, reverseMove);
		
		
		if(movedBarrel){
			int diffX = lastMove.getXo() - lastMove.getXi();
			int diffY = lastMove.getYo() - lastMove.getYi();
			reverseMove = new Move(
				lastMove.getXo() + diffX,
				lastMove.getYo() + diffY,
				lastMove.getXi() + diffX,
				lastMove.getYi() + diffY
				);
			move(reverseMove);
			executedMoves = remove(executedMoves, lastMove);
			executedMoves = remove(executedMoves, reverseMove);
			
		}*/
	}
	
	
	
	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}
/*
	public Move getLastMove() {
		return executedMoves.get(executedMoves.size()-1);
	}

	public void addLastMove(Move move) {
		this.executedMoves.add(move);
	}*/

	public ArrayList<BarrelSpotPair<Barrel, Spot>> getBarrelSpotPairs() {
		return barrelSpotPairs;
	}

	public void setBarrelSpotPairs(ArrayList<BarrelSpotPair<Barrel, Spot>> barrelSpotPairs) {
		this.barrelSpotPairs = barrelSpotPairs;
		this.executingPair = barrelSpotPairs.get(0);
	}

	public Move getInitialMove() {
		return moveTree.getRoot();
	}

	public void setInitialMove(Move initialMove) {
		moveTree.setRoot(initialMove);
	}
	
	public MoveTree getMoveTree(){
		return moveTree;
	}

}
