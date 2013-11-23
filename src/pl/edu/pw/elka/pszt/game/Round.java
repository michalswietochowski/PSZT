package pl.edu.pw.elka.pszt.game;

import java.util.ArrayList;

import pl.edu.pw.elka.pszt.models.Level;

public class Round {

	private Level level;
	private ArrayList<Move> availableMoves;
	private ArrayList<Move> forbidenMoves;
	private Move lastMove;
	
	public Round(Level level, Move initialMove){
		this.level = level;
		this.lastMove = initialMove;
	}
	
	public Round(Level level){
		this.level = level;
	}

	public void checkMoves(){
		checkMove('F');
	}
	
	public boolean checkMove(char to){
		Move nextMove;
		if(to=='F'){
			nextMove = lastMove.calcNextMove('F');
			System.out.println(level.canMove(nextMove));
			return level.canMove(nextMove);
		}else 
		if(to=='L'){
			nextMove = lastMove.calcNextMove('L');
			System.out.println(level.canMove(nextMove));
			return level.canMove(nextMove);
		}else 
		if(to=='R'){
			nextMove = lastMove.calcNextMove('R');
			System.out.println(level.canMove(nextMove));
			return level.canMove(nextMove);
		}
		return false;
	}
	
	
	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public Move getLastMove() {
		return lastMove;
	}

	public void setLastMove(Move initialMove) {
		this.lastMove = initialMove;
	}
	

}
