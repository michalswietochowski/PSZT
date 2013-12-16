package pl.edu.pw.elka.pszt.game;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javafx.concurrent.Task;
import pl.edu.pw.elka.pszt.models.Barrel;
import pl.edu.pw.elka.pszt.models.BarrelSpotPair;
import pl.edu.pw.elka.pszt.models.Bulldozer;
import pl.edu.pw.elka.pszt.models.Level;
import pl.edu.pw.elka.pszt.models.Spot;

public class AStar extends Task {

	private Level level;
	private MoveTree moveTree;
	private Move lastMove;
	
	private ArrayList<BarrelSpotPair<Barrel, Spot>> barrelSpotPairs;
	private BarrelSpotPair<Barrel, Spot> executingPair;

	private int discardedMoves=0;
	private int maxNumberOfSteps=50;
	
	private Date startDate, endDate;
	private static DateFormat DATEFORMAT =  new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SS");

    private String threadId;
	
	public AStar(Level level, Move initialMove){
		this.level = level;		
		moveTree = new MoveTree();
		moveTree.setRoot(initialMove);
		
	}
	
	public AStar(Level level){
		this.level = level;
		moveTree = new MoveTree();
	}

	@Override
	public void run(){
		update("A* thread started");
		Date startdate = new Date();
		Move minMove = generateNewMovesPop();
		
		for(int i=2 ;i<maxNumberOfSteps; i++){
			minMove = generateNewMovesPop();
			moveFromRoot(minMove);
            update(getLevel().getMovablesMap().toString());
			
			if(getNumberOfBarrelsAtSpot()==barrelSpotPairs.size()){
                update("route found");
				endDate  = new Date();
				lastMove=minMove;
				break;
			}
			goBackToRoot(minMove);
			
		}
        update(getLevel().getMovablesMap().toString());
        update("start time" + DATEFORMAT.format(startdate));
	}
	
	
	public void checkMoves(){
		checkMoves(moveTree.getCurrentNode());
	}
	
	public void checkMoves(Move move){
		ArrayList<Move> children = new ArrayList<Move>();
		if(checkMove(move, 'F')) {
			Move child = move.calcNextMove('F');
			child.setParent(move);
			moveFromRoot(child);
			child.setF(g() + h2());
			children.add(child);
			goBackToRoot(child);
		}
		if(checkMove(move, 'L')) {
			Move child = move.calcNextMove('L');
			child.setParent(move);
			moveFromRoot(child);
			child.setF(g() + h2());
			children.add(child);
			goBackToRoot(child);
			
		}
		if(checkMove(move, 'R')) {
			Move child = move.calcNextMove('R');
			child.setParent(move);
			moveFromRoot(child);
			child.setF(g() + h2());
			children.add(child);
			goBackToRoot(child);
		}
		move.setChildren(children);
		if(children.size()==0 || move.areAllChildrenDeadEnd()){
			move.setDeadEnd(true);
			discardedMoves++;
		}else if(move.getSize()>6){
			if(move.equals(move.getParent().getParent().getParent().getParent()) && !move.isMovedBarrel()){
				move.setDeadEnd(true);
				discardedMoves++;
			}
		}
		
		if(move.getSize()>8){
			if(!move.isMovedBarrel() && 
					move.equals(move.getParent().getParent().getParent().getParent().
					getParent().getParent())){
				move.setDeadEnd(true);
				discardedMoves++;
			}
		}
		/*
		if(barrelAtCorner()){
			move.setDeadEnd(true);
			discardedMovesbarrels++;
		}*/
		
		if(move.getSize()>10){
			if(!move.isMovedBarrel() && 
					move.equals(move.getParent().getParent().getParent().getParent().
					getParent().getParent().getParent().getParent())){
				move.setDeadEnd(true);
				discardedMoves++;
			}
		}

		if(move.getSize()>12){
			if(!move.isMovedBarrel() && 
					move.equals(move.getParent().getParent().getParent().getParent()
					.getParent().getParent().getParent().getParent()
					.getParent().getParent())){
				move.setDeadEnd(true);
				discardedMoves++;
			}
		}

		if(move.getSize()>14){
			if(!move.isMovedBarrel() && 
					move.equals(move.getParent().getParent().getParent().getParent().
					getParent().getParent().getParent().getParent().
					getParent().getParent().getParent().getParent())){
				move.setDeadEnd(true);
				discardedMoves++;
			}
		}
		
		if(move.getSize()>16){
			if(!move.isMovedBarrel() && 
					move.equals(move.getParent().getParent().getParent().getParent().
					getParent().getParent().getParent().getParent().
					getParent().getParent().getParent().getParent()
					.getParent().getParent())){
				move.setDeadEnd(true);
				discardedMoves++;
			}
		}
		/*
		if(move.getSize()>16){
			if(!move.isMovedBarrel() && 
					move.equals(move.getParent().getParent().getParent().getParent().
					getParent().getParent().getParent().getParent().
					getParent().getParent().getParent().getParent()
					.getParent().getParent().getParent().getParent())){
				move.setDeadEnd(true);
				discardedMoves++;
			}
		}

	*/
		
		if(move.getSize()>16){
			Move parent = move.getParent();
			int count =0;
			for(int i =0;i<11;i++){
				if(!parent.isMovedBarrel()){
					count++;
				}
				parent =parent.getParent();
			}
			if(count>10){
				move.setDeadEnd(true);
				discardedMoves++;
			}
		}
		
		
	}
	
	public boolean checkMove(Move move, char to){

		Move nextMove = move.calcNextMove(to);
		if(move!=moveTree.getRoot()){
			//!!!
			moveFromRoot(move);
			moveTree.setCurrentNode(move);
			move.setF(g() + h(executingPair));
		}
		boolean canMove = level.canMove(nextMove);
		/*if(nextMove.areAllChildrenDeadEnd()){
			update("znalazlem martwe dziecko");
			return false;
			nigdy nie znajduje
		}*/
		if(move!=moveTree.getRoot()){
			goBackToRoot(move);
		}
		
		
		return canMove;
	}
	
	
	public Move generateNewMovesPop(){
		ArrayList<Move> parents = moveTree.getYoungest(); //findYoungest();
		
		int size = parents.size();
		update("size beore" + size);
		int i=0;
		for (Iterator<Move> iterator = parents.iterator(); iterator.hasNext(); ) {
		    Move move = iterator.next();
		    checkMoves(move);
		    if (move.isDeadEnd()) {
		    	i++;
		    	iterator.remove();
		    }
		}
		update("usunalem " + i);
		ArrayList<Move> youngest = new ArrayList<Move>();
		for (Move parent : parents) {
			youngest.addAll(parent.getChildren());
			
		}
		update("Discarder moves  " + discardedMoves);
		moveTree.setYoungest(youngest);
		
		return findMinMoveFrom(youngest);
	}
	
	
	/*
	private ArrayList<Move> findYoungest(Move move){
		ArrayList<Move> moves = new ArrayList<Move>();
		if(move.getChildren()==null){
			if(!move.isDeadEnd()){
				moves.add(move);
			}else {
				return null;
			}
			
		}else {
			for (Move child : move.getChildren()) {
				if(!child.isDeadEnd()){
					moves.addAll(findYoungest(child));
				}
			}
		}
		return moves;
	}
	
	*/
		
	
	public void moveFromRoot(Move move){
		ArrayList<Move> moves = new ArrayList<Move>();
		moves.add(move);
		while(move.getParent()!=moveTree.getRoot()){
			
			move = move.getParent();
			moves.add(move);
		}
		Collections.reverse(moves);
		for (Move move2 : moves) {
			level.move(move2);
		}
	}
	
	public void goBackToRoot(Move move){
		ArrayList<Move> moves = new ArrayList<Move>();
		moves.add(move);
		while(move.getParent()!=moveTree.getRoot()){
			
			move = move.getParent();
			moves.add(move);
		}
		for (Move move2 : moves) {
			goBack(move2);
		}
	}
	
	public void goBack(Move lastMove){
	    
	    Move reverseMove = new Move(
	                    lastMove.getXo(),
	                    lastMove.getYo(),
	                    lastMove.getXi(),
	                    lastMove.getYi()
	                    );
	    level.move(reverseMove);
	    
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
	    }
	}	
	
	
	private Move findMinMoveFrom(ArrayList<Move> moves){
		
		Move minMove=null;
		for (Move move : moves) {
			if(minMove==null){
				minMove = move;
				
			}else {
				if((move.getBarellsAtSpots()<minMove.getBarellsAtSpots()) || (move.getF()<minMove.getF())){
					minMove = move;
				}
			}
		}
		return minMove;
	}
	
	public int getNumberOfBarrelsAtSpot(){
		int count =0;
		for (BarrelSpotPair<Barrel, Spot> pair : barrelSpotPairs) {
			if(isBarrelAtSpot(pair)){
				count++;
			}
		}
		return count;
	}
	
	public boolean isBarrelAtSpot(BarrelSpotPair<Barrel, Spot> pair){
		if(h2(pair)==0){
			return true;
		}
		return false;
	}
	
	
	/*
	 * @Dariusz
	 * @Comment used only to display data fo tests
	 */
	public void moveFromRootWitDisp(Move move){
		ArrayList<Move> moves = new ArrayList<Move>();
		moves.add(move);
		while(move.getParent()!=moveTree.getRoot()){
			
			move = move.getParent();
			moves.add(move);
		}
		Collections.reverse(moves);
		for (Move move2 : moves) {
			level.move(move2);
			update("moves from root ");
			update(level.getMovablesMap().toString());
			
		}
	}
	
	
	public int g(){
		return moveTree.getCurrentNode().getSize();
	}
	
	public int h(BarrelSpotPair<Barrel, Spot> pair){
		/*int[] barrel = level.getMovablesMap().findBarrel(pair.getLeft());
		int[] spot = level.getMap().findSpot(pair.getRight());
		int[] bulldozer = level.getMovablesMap().findBulldozer();
		int h1 = Math.abs(barrel[0] -bulldozer[0]) + Math.abs(barrel[1] -bulldozer[1]);
		int h2 = Math.abs(barrel[0] -spot[0]) + Math.abs(barrel[1] -spot[1]);
		*/
		return h2() ; //+ h1();
		
	}
	
	public int h1(//BarrelSpotPair<Barrel, Spot> pair
			){
		int h1 =0;
		for (BarrelSpotPair<Barrel, Spot> pair : barrelSpotPairs) {
			int[] barrel = level.getMovablesMap().findBarrel(pair.getLeft());
			int[] bulldozer = level.getMovablesMap().findBulldozer();
			if(barrel==null){
				update("bef " + level.getMovablesMap());
			}
			h1 += Math.abs(barrel[0] -bulldozer[0]) + Math.abs(barrel[1] -bulldozer[1]);
		}
		return h1; //Math.abs(barrel[0] -bulldozer[0]) + Math.abs(barrel[1] -bulldozer[1]);
	}
	
	public int h2(//BarrelSpotPair<Barrel, Spot> pair
			){
		int h2 =0;
		for (BarrelSpotPair<Barrel, Spot> pair : barrelSpotPairs) {
			h2 += h2(pair);
		}
		return h2;
	}
	
	public int h2(BarrelSpotPair<Barrel, Spot> pair){
		int[] barrel = level.getMovablesMap().findBarrel(pair.getLeft());
		int[] spot = level.getMap().findSpot(pair.getRight());
		return  Math.abs(barrel[0] -spot[0]) + Math.abs(barrel[1] -spot[1]);
	}
	
	
	public boolean barrelAtCorner() {
		for (BarrelSpotPair<Barrel, Spot> pair  : barrelSpotPairs) {
			if(h2(pair)==0){
				return false;
			}
			if(level.barellAtCorner(pair.getLeft())){
				discardedMoves++;
				return true;
			}
		}
		return false;
	}
	
	
	
	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = (level);
	}

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

	public int getMaxNumberOfSteps() {
		return maxNumberOfSteps;
	}

	public void setMaxNumberOfSteps(int maxNumberOfSteps) {
		this.maxNumberOfSteps = maxNumberOfSteps;
	}

	public Move getLastMove() {
		return lastMove;
	}

	public int getPathSize(){
		return lastMove.getSize();
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    /**
     * Shorthand notification method
     * @param message argument to send to observer or console
     */
    private void update(String message)
    {
        if (threadId != null) {
            message = String.format("[%s][Thread %s] %s", DATEFORMAT.format(new Date()), threadId, message);
            updateMessage(message);
        } else {
            System.out.println(message);
        }
    }

    @Override
    protected Object call() throws Exception {
        run();
        return this;
    }
}
