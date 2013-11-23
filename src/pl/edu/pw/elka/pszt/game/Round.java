package pl.edu.pw.elka.pszt.game;

import java.util.ArrayList;

import pl.edu.pw.elka.pszt.models.Barrel;
import pl.edu.pw.elka.pszt.models.BarrelSpotPair;
import pl.edu.pw.elka.pszt.models.Level;
import pl.edu.pw.elka.pszt.models.Spot;

public class Round {

	private Level level;
	private ArrayList<Move> availableMoves;
	private ArrayList<Move> forbidenMoves;
	private ArrayList<Move> executedMoves;
	
	private ArrayList<BarrelSpotPair<Barrel, Spot>> barrelSpotPairs;
	private BarrelSpotPair<Barrel, Spot> executingPair;
	
	//private Move lastMove;
	
	public Round(Level level, Move initialMove){
		this.level = level;
		availableMoves = new ArrayList<Move>();
		forbidenMoves = new ArrayList<Move>();
		executedMoves = new ArrayList<Move>();
		this.executedMoves.add(initialMove);
		
	}
	
	public Round(Level level){
		this.level = level;
		availableMoves = new ArrayList<Move>();
		forbidenMoves = new ArrayList<Move>();
		executedMoves = new ArrayList<Move>();
	}

	public void start(ArrayList<BarrelSpotPair<Barrel, Spot>> barrelSpotPairs, Move initMove){
		this.barrelSpotPairs =barrelSpotPairs;
		this.executingPair = this.barrelSpotPairs.get(0);
		this.executedMoves.add(initMove);
		for (BarrelSpotPair<Barrel, Spot> barrelSpotPair : barrelSpotPairs) {
			while(h(barrelSpotPair)!=1){//jaki dodatkowy warunek, zêby sie nie krêci³ do usranej œmierci?
				checkMoves();
				
				
			}
		}
	}
	
	public void checkMoves(){
		availableMoves = new ArrayList<Move>();
		if(checkMove('F')) availableMoves.add(executedMoves.get(executedMoves.size()-1).calcNextMove('F'));
		if(checkMove('L')) availableMoves.add(executedMoves.get(executedMoves.size()-1).calcNextMove('L'));
		if(checkMove('R')) availableMoves.add(executedMoves.get(executedMoves.size()-1).calcNextMove('R'));
		
	
	}
	
	public boolean checkMove(char to){
		Move nextMove;
		nextMove = executedMoves.get(executedMoves.size()-1).calcNextMove(to);
		for (Move forbidenMove : forbidenMoves) {
			if(forbidenMove.equals(nextMove)){
				return false;
			}
		}
		return level.canMove(nextMove);
	}
	
	public void move(Move nextMove){
		moveMin(nextMove);
		availableMoves.remove(nextMove);
		
	}
	
	public void moveMin(Move nextMove){
		level.move(nextMove);
		executedMoves.add(nextMove);
	}
	
	public Move findMinMove(){
		Move minMove = availableMoves.get(0);
		moveMin(minMove);
		int fmin = g() + h(executingPair);
		goBackMin(availableMoves.get(0).isMovedBarrel());
		
		for (int i =0;i<availableMoves.size();i++){
			if(!minMove.equals(availableMoves.get(i))){
				moveMin(availableMoves.get(i));
				if( g() + h(executingPair) < fmin) {
					fmin = g() + h(executingPair);
					minMove = availableMoves.get(i);
					}
				goBackMin(availableMoves.get(i).isMovedBarrel());
			}
		}
		return minMove;
	}
	
	public int g(){
		return executedMoves.size();
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
		Move lastMove = executedMoves.get(executedMoves.size()-1);
		Move reverseMove = new Move(
				lastMove.getXo(),
				lastMove.getYo(),
				lastMove.getXi(),
				lastMove.getYi()
				);
		move(reverseMove);
		availableMoves.add(lastMove);
		executedMoves.remove(lastMove);
		
		
		
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
			
		}
	}
	
	public void goBackMin(boolean movedBarrel){
		
		Move lastMove = executedMoves.get(executedMoves.size()-1);
		Move reverseMove = new Move(
				lastMove.getXo(),
				lastMove.getYo(),
				lastMove.getXi(),
				lastMove.getYi()
				);
		level.move(reverseMove);		
		executedMoves.remove(lastMove);
		
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
			//availableMoves.add(lastMove);
		}
	}
	
	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public Move getLastMove() {
		return executedMoves.get(executedMoves.size()-1);
	}

	public void addLastMove(Move move) {
		this.executedMoves.add(move);
	}

	public ArrayList<BarrelSpotPair<Barrel, Spot>> getBarrelSpotPairs() {
		return barrelSpotPairs;
	}

	public void setBarrelSpotPairs(ArrayList<BarrelSpotPair<Barrel, Spot>> barrelSpotPairs) {
		this.barrelSpotPairs = barrelSpotPairs;
		this.executingPair = barrelSpotPairs.get(0);
	}
	

}
