package pl.edu.pw.elka.pszt.game;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import pl.edu.pw.elka.pszt.models.Barrel;
import pl.edu.pw.elka.pszt.models.BarrelSpotPair;
import pl.edu.pw.elka.pszt.models.Bulldozer;
import pl.edu.pw.elka.pszt.models.Floor;
import pl.edu.pw.elka.pszt.models.Level;
import pl.edu.pw.elka.pszt.models.Spot;
import sun.org.mozilla.javascript.internal.ast.ArrayLiteral;

public class AStar implements Runnable{

	private Level level;
	private MoveTree moveTree;
	private Move lastMove;
	
	private ArrayList<BarrelSpotPair<Barrel, Spot>> barrelSpotPairs;
	private BarrelSpotPair<Barrel, Spot> executingPair;

	private int discardedMoves=0;
	private int discardedMovesB=0;
	private int maxNumberOfSteps=50;
	private int loopRemoval = 5;
	private int stepsPerBatSRate =8;
	
	private Date startDate, endDate;
	private static DateFormat DATEFORMAT =  new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SS");;
	
	
	
	public AStar(Level level, Move initialMove){
		this.level = level;		
		moveTree = new MoveTree();
		moveTree.setRoot(initialMove);
		
	}
	
	public AStar(Level level){
		this.level = level;
		moveTree = new MoveTree();
		
		//
		
        ArrayList<Barrel> barrels = level.getMovablesMap().getBarrels();
        ArrayList<Spot> spots = level.getMap().getSpots();
    	
    	BarrelSpotPair<Barrel, Spot> pair ;
		barrelSpotPairs = new ArrayList<BarrelSpotPair<Barrel,Spot>>();
		for (int i =0; i<barrels.size();i++) {
			pair = new BarrelSpotPair<Barrel, Spot>(barrels.get(i), spots.get(i));
			barrelSpotPairs.add(pair);
		}
		int [] bulldozerCoord = level.getMovablesMap().findBulldozer();
		Move root = new Move(
				bulldozerCoord[0]-1,
				bulldozerCoord[1],
				bulldozerCoord[0],
				bulldozerCoord[1]
				);
		
		moveTree.setRoot(root);
		//
	}

	@Override
	public void run(){
		
		Date startdate = new Date();
		Move minMove = generateNewMovesPop();
		
		for(int i=2 ;i<maxNumberOfSteps; i++){
			minMove = generateNewMovesPop();
			moveFromRoot(minMove);
			System.out.println(getLevel().getMovablesMap());
			System.out.println("barrels at spots: " + getNumberOfBarrelsAtSpot());
			if(getNumberOfBarrelsAtSpot()==barrelSpotPairs.size()){
				System.out.println("route found");
				endDate  = new Date();
				lastMove=minMove;
				break;
			}
			goBackToRoot(minMove);
		}
		System.out.println(getLevel().getMovablesMap());
		System.out.println("start time" + DATEFORMAT.format(startdate));
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
			child.setF(hh());
			children.add(child);
			goBackToRoot(child);
		}
		if(checkMove(move, 'L')) {
			Move child = move.calcNextMove('L');
			child.setParent(move);
			moveFromRoot(child);
			child.setF(hh());
			children.add(child);
			goBackToRoot(child);
			
		}
		if(checkMove(move, 'R')) {
			Move child = move.calcNextMove('R');
			child.setParent(move);
			moveFromRoot(child);
			child.setF(hh());
			children.add(child);
			goBackToRoot(child);
		}
		if(move.isMovedBarrel() 
				&& checkMove(move, 'B')) {
			Move child = move.calcNextMove('B');
			
			child.setParent(move);
			moveFromRoot(child);
			child.setF(hh());
			children.add(child);
			goBackToRoot(child);
		}
		
		move.setChildren(children);
		if(children.size()==0 || move.areAllChildrenDeadEnd()){
			move.setDeadEnd(true);
			moveTree.addDeadEnd(move);
			discardedMoves++;
			return;
		}
		
				Move iterMove = move;
				boolean moved = false;
				boolean eq = false;
				int count =0;
				while(iterMove.getParent()!=moveTree.getRoot() &&  iterMove.getParent()!=null){
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
					if(count>loopRemoval){
						eq= true;
						break;
					}
					
				}
				if(!moved && 
						eq){
					move.setDeadEnd(true);
					moveTree.addDeadEnd(move);
					discardedMoves++;
					return;
				}
				
				for (Move deadEnd : moveTree.getDeadEnds()) {
					if(move.equals(deadEnd)){
						eq=true;
						discardedMovesB++;
						break;
					}
				}
		
			if(move.getSize() > stepsPerBatSRate){
			int expectedBatS = (int)(move.getSize()/stepsPerBatSRate);
			moveFromRoot(move);
			
			if(
					getNumberOfBarrelsAtSpot()>(expectedBatS-1) &&
					 getNumberOfBarrelsAtSpot()<=(expectedBatS+1)
			){}else {
				move.setDeadEnd(true);
				moveTree.addDeadEnd(move);
				discardedMoves++;
				goBackToRoot(move);
				return;
			}
			goBackToRoot(move);
		}
	}
	
	public boolean checkMove(Move move, char to){

		Move nextMove = move.calcNextMove(to);
		if(move!=moveTree.getRoot()){
			//!!!
			moveFromRoot(move);
			moveTree.setCurrentNode(move);
			move.setF(hh());
		}
		boolean canMove = level.canMove(nextMove);
		/*if(nextMove.areAllChildrenDeadEnd()){
			System.out.println("znalaz�em martwe dziecko");
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
		System.out.println("size beore"  + size);
		System.out.println("move "  + parents.get(0).getSize());
		
		int i=0;
		for (Iterator<Move> iterator = parents.iterator(); iterator.hasNext(); ) {
		    Move move = iterator.next();
		    checkMoves(move);
		    if (move.isDeadEnd()) {
		    	i++;
		    	iterator.remove();
		    }
		}
		System.out.println("usun��em " + i);
		ArrayList<Move> youngest = new ArrayList<Move>();
		for (Move parent : parents) {
			youngest.addAll(parent.getChildren());
			
		}
		System.out.println("Discarder moves  " + discardedMoves);
		System.out.println("Discarder movesB  " + discardedMovesB);
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
		/*int count =0;
		for (BarrelSpotPair<Barrel, Spot> pair : barrelSpotPairs) {
			int[] barrel = level.getMovablesMap().findBarrel(pair.getLeft());
			int[] spot = level.getMap().findSpot(pair.getRight());
			System.out.println("pair" + barrel[0] + barrel[1]+ spot[0] +spot[1]);
			if(isBarrelAtSpot(pair)){
				
				count++;
			}
		}
		return count;*/
		ArrayList<Barrel> barrels = new ArrayList<Barrel>();
		ArrayList<Spot> spots = new ArrayList<Spot>();
		for (BarrelSpotPair<Barrel, Spot> pair : barrelSpotPairs) {
			barrels.add(pair.getLeft());
			spots.add(pair.getRight());
		}
		int count=0;
		for (Iterator<Barrel> biter = barrels.iterator(); biter.hasNext(); ) {
		    Barrel b= biter.next();
		    for (Iterator<Spot> siter = spots.iterator(); siter.hasNext(); ) {
			    Spot s= siter.next();
			    if (h2(new BarrelSpotPair<Barrel, Spot>(b, s))==0) {
			        count++;
			    }
		    
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
	 * @Comment used only to display data for tests
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
			System.out.println("moves from root ");
			System.out.println(level.getMovablesMap());
			
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
		return hh() ; //+ h1();
		
	}
	
	public int h1(//BarrelSpotPair<Barrel, Spot> pair
			){
		int h1 =0;
		for (BarrelSpotPair<Barrel, Spot> pair : barrelSpotPairs) {
			int[] barrel = level.getMovablesMap().findBarrel(pair.getLeft());
			int[] bulldozer = level.getMovablesMap().findBulldozer();
			if(barrel==null){
				System.out.println( "bef " + level.getMovablesMap());
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
	
	
	public int hh(){
		ArrayList<Barrel> barrels = new ArrayList<Barrel>();
		ArrayList<Spot> spots = new ArrayList<Spot>();
		for (BarrelSpotPair<Barrel, Spot> pair : barrelSpotPairs) {
			barrels.add(pair.getLeft());
			spots.add(pair.getRight());
		}
		
		for (Iterator<Barrel> biter = barrels.iterator(); biter.hasNext(); ) {
		    Barrel b= biter.next();
		    for (Iterator<Spot> siter = spots.iterator(); siter.hasNext(); ) {
			    Spot s= siter.next();
			    if (h2(new BarrelSpotPair<Barrel, Spot>(b, s))==0) {
			        biter.remove();
			        siter.remove();
			    }
		    
		    }   
		}
		int dist =0;
		for (Barrel barrel : barrels) {
			int min =0;
			Spot smin=null;
			for (Spot spot : spots) {
				if(smin==null){
					smin = spot;
					min = h2(new BarrelSpotPair<Barrel, Spot>(barrel, spot));
				}else {
					int i = h2(new BarrelSpotPair<Barrel, Spot>(barrel, spot));
					if(i<min){
						smin = spot;
						min = i;
					}
				}
			}
			dist += min;
			
		}
		
		
		return dist;
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

	public int getLoopRemoval() {
		return loopRemoval;
	}

	public void setLoopRemoval(int loopRemoval) {
		this.loopRemoval = loopRemoval;
	}

	public int getStepsPerBatSRate() {
		return stepsPerBatSRate;
	}

	public void setStepsPerBatSRate(int stepsPerBatS) {
		this.stepsPerBatSRate = stepsPerBatS;
	}

	
}
