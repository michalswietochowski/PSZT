package pl.edu.pw.elka.pszt.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import pl.edu.pw.elka.pszt.models.Barrel;
import pl.edu.pw.elka.pszt.models.BarrelSpotPair;
import pl.edu.pw.elka.pszt.models.Bulldozer;
import pl.edu.pw.elka.pszt.models.Level;
import pl.edu.pw.elka.pszt.models.Spot;

public class Round {

	//private Level currentLevel;
	//private Level originLevel;
	//
	
	//private ArrayList<Move> availableMoves;
	//private ArrayList<Move> forbidenMoves;
	//private ArrayList<Move> executedMoves;
	private MoveTree moveTree;
	
	private ArrayList<BarrelSpotPair<Barrel, Spot>> barrelSpotPairs;
	private BarrelSpotPair<Barrel, Spot> executingPair;
	
	//private Move lastMove;
	
	public Round(Level level, Move initialMove){
		this.currentLevel = level;
		this.originLevel = level;
		moveTree = new MoveTree();
		moveTree.setRoot(initialMove);
		
	}
	
	public Round(Level level){
		this.currentLevel = level;
		this.originLevel = level;
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
				System.out.println(currentLevel.getMovablesMap().toString());
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
			Move child = move.calcNextMove('F');
			child.setF(g() + h(child, executingPair));
			child.setParent(move);
			children.add(child);
		}
		if(checkMove(move, 'L')) {
			Move child = move.calcNextMove('L');
			child.setF(g() + h(child, executingPair));
			child.setParent(move);
			children.add(child);
		}
		if(checkMove(move, 'R')) {
			Move child = move.calcNextMove('R');
			child.setF(g() + h(child, executingPair));
			child.setParent(move);
			children.add(child);
		}
		move.setChildren(children);
		//System.out.println("dla" + move+"znalezione ruchy " + children );
		if(children.size()==0){
			move.setDeadEnd(true);
			moveTree.setCurrentNode(move.getParent());
		}
	}
	
	public boolean checkMove(Move move, char to){

		Move nextMove = move.calcNextMove(to);
		
		boolean canMove = currentLevel.canMove(nextMove);
		if(nextMove.areAllChildrenDeadEnd()){
			return false;
		}
		
	
		return canMove;
	}
	
	public Move generateNewMovesPop(){
		ArrayList<Move> youngest = findYoungest(moveTree.getRoot());
		for (Move move : youngest) {
			//System.out.println("szukam ruchów dla dziecka " + move);
			checkMoves(move);
		}
		System.out.println("youngest " + youngest);
		return findMinMoveFrom(youngest);
	}
	
	private ArrayList<Move> findYoungest(Move move){
		ArrayList<Move> moves = new ArrayList<Move>();
		if(move.getChildren()==null){
			if(!move.isDeadEnd()){
			
				moves.add(move);
			}else {
				return null;
			}
			
			//System.out.println("add move " + move);
		}else {
			for (Move child : move.getChildren()) {
				//System.out.println("child " + child);
				moves.addAll(findYoungest(child));
				//System.out.println("youngest childs " +findYoungest(child));
			}
		}
		return moves;
	}
	
	
	
	private Move findMinMoveFrom(ArrayList<Move> moves){
		int fmin = 0;
		Move minMove=null;
		for (Move move : moves) {
			if(minMove==null){
				minMove = move;
				fmin = minMove.getF();
			}else {
				if(move.getF()<minMove.getF()){
					minMove = move;
					fmin = minMove.getF();
				}
				
			}
			System.out.println(" comparing " + move + move.getF());
		}
		return minMove;
	}
	
	
	public boolean isBarrelAtSpot(BarrelSpotPair<Barrel, Spot> pair){
		/*if(h(pair)==2){
			return true;
		}*/
		return false;
	}
	
	
	
	
	
	
	
	
	
	public void move(Move nextMove){
		currentLevel.move(nextMove);
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
	
	public void moveFromRoot(Move move){
		ArrayList<Move> moves = new ArrayList<Move>();
		moves.add(move);
		System.out.println("parent "+ move.getParent());
		while(move.getParent() != null && move.getParent()!=moveTree.getRoot()){
			moves.add(move);
			move = move.getParent();
			System.out.println("inside the loop "+ move);
		}
		Collections.reverse(moves);
		currentLevel = originLevel;
		for (Move move2 : moves) {
			currentLevel.move(move2);
		}
		System.out.println("lista ruchów " + 	moves);
	}
	
	
	public Move findMinMove(){
		
		return findMove(moveTree.getCurrentNode());
	}
	
	public Move findMove(Move move){
		ArrayList<Move> children = new ArrayList<Move>();
		if(move.getChildren()!=null){
			for (Move m : move.getChildren()) {
				if(!m.areAllChildrenDeadEnd()){
					children.add(findMove(m));
				}else {
					return null;
				}
			}
			Move min = null;
			for (Move child : children) {
				if(min ==null){
					min =child;
				}else {
					if(child.getF()<min.getF()){
						min=child;
					}
					
				}
			}
			return min;
		}
		return move.getParent();
	}
	
	

	
	public int g(){
		return moveTree.getCurrentNode().getSize();
	}
	
	
	public int h(Move move, BarrelSpotPair<Barrel, Spot> pair){
		int[] barrel = currentLevel.getMovablesMap().findBarrel(pair.getLeft());
		int[] spot = currentLevel.getMap().findSpot(pair.getRight());
		int[] bulldozer = currentLevel.getMovablesMap().findBulldozer();
		int h1 = Math.abs(barrel[0] -move.getXo()) + Math.abs(barrel[1] -move.getYo());
		int h2 = Math.abs(barrel[0] -spot[0]) + Math.abs(barrel[1] -spot[1]);
		return h1+h2;
		
	}
	
	public int h(BarrelSpotPair<Barrel, Spot> pair){
		int[] barrel = currentLevel.getMovablesMap().findBarrel(pair.getLeft());
		int[] spot = currentLevel.getMap().findSpot(pair.getRight());
		int[] bulldozer = currentLevel.getMovablesMap().findBulldozer();
		int h1 = Math.abs(barrel[0] -bulldozer[0] + Math.abs(barrel[1] -bulldozer[1]));
		int h2 = Math.abs(barrel[0] -spot[0]) + Math.abs(barrel[1] -spot[1]);
		return h1+h2;
		
	}
	
	
	
	
	public Level getLevel() {
		return currentLevel;
	}

	public void setLevel(Level level) {
		this.originLevel = level;
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
