package pl.edu.pw.elka.pszt.tests;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import org.junit.Test;

import pl.edu.pw.elka.pszt.game.Move;
import pl.edu.pw.elka.pszt.game.AStar;
import pl.edu.pw.elka.pszt.models.Barrel;
import pl.edu.pw.elka.pszt.models.BarrelSpotPair;
import pl.edu.pw.elka.pszt.models.Bulldozer;
import pl.edu.pw.elka.pszt.models.Level;
import pl.edu.pw.elka.pszt.models.Map;
import pl.edu.pw.elka.pszt.models.MovablesMap;
import pl.edu.pw.elka.pszt.models.Spot;
import pl.edu.pw.elka.pszt.utils.LevelFactory;

public class MoveTest {

	 Level level1;
	 Level level2;
	 Level level3;
	AStar astar;
	Move initMoveN, initMoveE, initMoveS, initMoveW;
	ArrayList<Barrel> barrels;
	ArrayList<Spot> spots;
	MovablesMap movableMap;
	Map map;
	ArrayList<BarrelSpotPair<Barrel, Spot>> barrelSpotPairs;
	
	public MoveTest(){
		try {
			
            level1 = LevelFactory.createFromProperties("level1");
            level2 = LevelFactory.createFromProperties("level2");
            level3 = LevelFactory.createFromProperties("level3");
            astar = new AStar(level1);
            initMoves();
            //System.out.println(level1);
            movableMap= level1.getMovablesMap();
        	map = level1.getMap();
            barrels = movableMap.getBarrels();
        	spots = map.getSpots();
        	
        	BarrelSpotPair<Barrel, Spot> pair ;
    		barrelSpotPairs = new ArrayList<BarrelSpotPair<Barrel,Spot>>();
    		for (int i =0; i<3;i++) {
    			pair = new BarrelSpotPair<Barrel, Spot>(barrels.get(i), spots.get(i));
    			barrelSpotPairs.add(pair);
    		}
    		
        	
        	
        } catch (Exception e) {
            e.printStackTrace();
        }
			
	}
	
	private void initMoves(){
		int [] bulldozerCoord = level1.getMovablesMap().findBulldozer();
		initMoveE = new Move(
				bulldozerCoord[0]-1,
				bulldozerCoord[1],
				bulldozerCoord[0],
				bulldozerCoord[1]
				);
		
		
		initMoveW = new Move(
				bulldozerCoord[0]+1,
				bulldozerCoord[1],
				bulldozerCoord[0],
				bulldozerCoord[1]
				);
		
		initMoveS = new Move(
				bulldozerCoord[0],
				bulldozerCoord[1]-1,
				bulldozerCoord[0],
				bulldozerCoord[1]);


		initMoveN = new Move(
				bulldozerCoord[0],
				bulldozerCoord[1]+1,
				bulldozerCoord[0],
				bulldozerCoord[1]);
		
	}
	
	@Test
	public void levelInitTest() {
		//Level level1 = astar.getLevel();
		assertEquals("class pl.edu.pw.elka.pszt.models.Floor", level1.getMap().getMapObjects()[2][2].getClass().toString());
		assertEquals("class pl.edu.pw.elka.pszt.models.Wall", level1.getMap().getMapObjects()[1][1].getClass().toString());
		assertEquals("class pl.edu.pw.elka.pszt.models.Spot", level1.getMap().getMapObjects()[2][3].getClass().toString());
		assertEquals("class pl.edu.pw.elka.pszt.models.Barrel", level1.getMovablesMap().getMovables()[2][2].getClass().toString());
		assertEquals("class pl.edu.pw.elka.pszt.models.Bulldozer", level1.getMovablesMap().getMovables()[4][3].getClass().toString());
		
	}
	
	@Test
	public void moveTest() {
		 Move current = astar.getMoveTree().getCurrentNode();
		 astar.setBarrelSpotPairs(barrelSpotPairs);
		canMoveForwardTest();
		canMoveLeftTest();
		canMoveRightTest();
		canMoveBackTest();
		moveBulldozerTest();
		
	}
	
	@Test
	public void distanceFunctionsTest(){
		System.out.println("\n distanceFunctionsTest \n");
		
		BarrelSpotPair<Barrel, Spot> pair ;
		barrelSpotPairs = new ArrayList<BarrelSpotPair<Barrel,Spot>>();
		for (int i =0; i<3;i++) {
			pair = new BarrelSpotPair<Barrel, Spot>(barrels.get(i), spots.get(i));
			barrelSpotPairs.add(pair);
		}
		
		astar.setBarrelSpotPairs(barrelSpotPairs);
		
		// h - (h2 only) distance  barrel to spot
		assertEquals(3, astar.h(barrelSpotPairs.get(0)) );
		assertEquals(3, astar.h(barrelSpotPairs.get(1)));
		assertEquals(3, astar.h(barrelSpotPairs.get(2)));
		
		/**
		 *  h - (h2 +h1) distance  barrel to spot
		assertEquals(10, astar.h(barrelSpotPairs.get(0)) );
		assertEquals(10, astar.h(barrelSpotPairs.get(1)));
		assertEquals(10, astar.h(barrelSpotPairs.get(2)));
		
		*/
		astar.setInitialMove(initMoveN);
		// g - moves count
		assertTrue(astar.g()==0);
		
		Move nextMove =null;
		for(int i=1 ;i<4; i++){
			nextMove = astar.generateNewMovesPop();
			System.out.println("minmove # "+ i + " size=" +nextMove.getSize() + nextMove);
			assertEquals(nextMove.getSize(), i);
		}
				
	}
	
	@Test
	public void barrelsAndSpotsTest(){
		
		System.out.println("\n barrelsAndSpotsTest \n");
		int[][] barrelscoord = {{2,2}, {3,3} , {3,4}};
		int[][] spotscoord = {{2,3}, {2,4}, {4,4}};
		
		assertEquals(barrels.size(), 3);
		assertEquals(spots.size(), 3);
		
		for (Barrel barrel : barrels) {
			int[] coordinates = movableMap.findBarrel(barrel);
			System.out.println(barrel + " " + coordinates[0] + " " + coordinates[1]);
			assertTrue(
					((coordinates[0] == barrelscoord[0][0]) && (coordinates[1] == barrelscoord[0][1])) ||
					((coordinates[0] == barrelscoord[1][0]) && (coordinates[1] == barrelscoord[1][1])) ||
					((coordinates[0] == barrelscoord[2][0]) && (coordinates[1] == barrelscoord[2][1])) 
					);
			
		}
		for (Spot spot : spots) {
			int[] coordinates = map.findSpot(spot);
			System.out.println(spot + " " + coordinates[0] + " " + coordinates[1]);
			assertTrue(
					((coordinates[0] == spotscoord[0][0]) && (coordinates[1] == spotscoord[0][1])) ||
					((coordinates[0] == spotscoord[1][0]) && (coordinates[1] == spotscoord[1][1])) ||
					((coordinates[0] == spotscoord[2][0]) && (coordinates[1] == spotscoord[2][1])) 
					);
		}
		
	}
	
	
	@Test
	public void AstarTest(){
		
		System.out.println("\n AstarTest \n");
		
		
		astar = new AStar(level3);
		astar.setLoopRemoval(8);
		astar.setStepsPerBatSRate(0);
		astar.setMaxNumberOfSteps(100000);
		//*/
		/*
		astar = new AStar(level2);
		astar.setLoopRemoval(8);
		astar.setStepsPerBatSRate(11);
		*/
		///*
		/*astar = new AStar(level3);
		astar.setLoopRemoval(5);
		astar.setStepsPerBatSRate(8);
		//*/
		
		astar.run();
		//System.out.println("path size = " + astar.getPathSize());
		assertEquals(33, astar.getPathSize());
		
	}
	
	
	@Test
	public void barrelAtCornerTest(){
		
		System.out.println("\n barrelAtCornerTest \n");
		astar = new AStar(level3);
		int [] bulldozerCoord = level3.getMovablesMap().findBulldozer();
		initMoveN = new Move(
				bulldozerCoord[0],
				bulldozerCoord[1]+1,
				bulldozerCoord[0],
				bulldozerCoord[1]);
		System.out.println(astar.getLevel().getMovablesMap().toString());
		assertFalse(astar.isAnyBarrelAtCorner());
		Move nextMove = initMoveN.calcNextMove('R');
		//astar.checkMoves();
		astar.getLevel().move(nextMove);
		System.out.println(astar.getLevel().getMovablesMap().toString());
		
		/////////////////////
		nextMove = nextMove.calcNextMove('F');
		//astar.checkMoves();
		astar.getLevel().move(nextMove);
		System.out.println(astar.getLevel().getMovablesMap().toString());
		assertTrue(astar.isAnyBarrelAtCorner());
		//////////////////
		
		
		
	}
	
	@Test
	public void getAllNodesTest(){
		
		
		
		System.out.println("\n getAllNodesTest \n");
		System.out.println(astar.getLevel().getMovablesMap().toString());
		
		System.out.println(astar.getMoveTree().getAllNodes());
		astar = new AStar(level1);
		astar.setLoopRemoval(5);
		astar.setStepsPerBatSRate(11);
		astar.setMaxNumberOfSteps(4);
		astar.run();
		System.out.println(astar.getMoveTree().getAllNodes());
		System.out.println("\n BEST: \n" + astar.getMoveTree().getBest());
		System.out.println(astar.getLevel().getMovablesMap().toString());
		//System.out.println(astar.getLevel().getMovablesMap().toString());
	}
	
	private void canMoveForwardTest(){
		System.out.println("\n canMoveForwardTest \n");
		
		astar.setInitialMove(initMoveN);
		Move current = astar.getMoveTree().getCurrentNode();
		assertTrue(astar.checkMove(current, 'F'));
		
		astar.setInitialMove(initMoveE);
		current = astar.getMoveTree().getCurrentNode();
		assertFalse(astar.checkMove(current,'F'));
		
		astar.setInitialMove(initMoveS);
		current = astar.getMoveTree().getCurrentNode();
		assertTrue(astar.checkMove(current,'F'));
		
		astar.setInitialMove(initMoveW);
		current = astar.getMoveTree().getCurrentNode();
		assertTrue(astar.checkMove(current,'F'));
		
	}	
	
	private void canMoveLeftTest(){
		System.out.println("\n canMoveLeftTest \n");
		
		astar.setInitialMove(initMoveN);
		Move current = astar.getMoveTree().getCurrentNode();
		assertTrue(astar.checkMove(current,'L'));
		
		astar.setInitialMove(initMoveE);
		current = astar.getMoveTree().getCurrentNode();
		assertTrue(astar.checkMove(current,'L'));
		
		astar.setInitialMove(initMoveS);
		current = astar.getMoveTree().getCurrentNode();
		assertFalse(astar.checkMove(current,'L'));
		
		astar.setInitialMove(initMoveW);
		current = astar.getMoveTree().getCurrentNode();
		assertTrue(astar.checkMove(current,'L'));
		
		
	}
	
	private void canMoveRightTest(){
		System.out.println("\n canMoveRightTest \n");
		
		astar.setInitialMove(initMoveN);
		Move current = astar.getMoveTree().getCurrentNode();
		assertFalse(astar.checkMove(current,'R'));
		
		astar.setInitialMove(initMoveE);
		current = astar.getMoveTree().getCurrentNode();
		assertTrue(astar.checkMove(current,'R'));
		
		astar.setInitialMove(initMoveS);
		current = astar.getMoveTree().getCurrentNode();
		assertTrue(astar.checkMove(current,'R'));
		
		astar.setInitialMove(initMoveW);
		current = astar.getMoveTree().getCurrentNode();
		assertTrue(astar.checkMove(current,'R'));
		
	}
	
	private void canMoveBackTest(){
		System.out.println("\n canMoveBacktTest \n");
		
		astar.setInitialMove(initMoveN);
		Move current = astar.getMoveTree().getCurrentNode();
		assertTrue(astar.checkMove(current,'B'));
		
		astar.setInitialMove(initMoveE);
		current = astar.getMoveTree().getCurrentNode();
		assertTrue(astar.checkMove(current,'B'));
		
		astar.setInitialMove(initMoveS);
		current = astar.getMoveTree().getCurrentNode();
		assertTrue(astar.checkMove(current,'B'));
		
		astar.setInitialMove(initMoveW);
		current = astar.getMoveTree().getCurrentNode();
		assertFalse(astar.checkMove(current,'B'));
		
	}
	
	private AStar moveBulldozerTest(){
		
		System.out.println("\n moveBulldozerTest \n");
		System.out.println(astar.getLevel().getMovablesMap().toString());
		assertFalse(astar.isAnyBarrelAtCorner());
		Move nextMove = initMoveN.calcNextMove('F');
		//astar.checkMoves();
		astar.getLevel().move(nextMove);
		int [] bulldozerCoord = astar.getLevel().getMovablesMap().findBulldozer();
		
		
		assertTrue(bulldozerCoord[0]==nextMove.getXo());
		assertTrue(bulldozerCoord[1]==nextMove.getYo());
		nextMove = nextMove.calcNextMove('L');
		//astar.checkMoves();
		astar.getLevel().move(nextMove);
		//
		System.out.println(astar.getLevel().getMovablesMap().toString());
		//
		assertFalse(astar.isAnyBarrelAtCorner());
		bulldozerCoord = astar.getLevel().getMovablesMap().findBulldozer();
		assertTrue(bulldozerCoord[0]==nextMove.getXo());
		assertTrue(bulldozerCoord[1]==nextMove.getYo());
		
		nextMove = nextMove.calcNextMove('F');
		//astar.checkMoves();
		astar.getLevel().move(nextMove);
		bulldozerCoord = astar.getLevel().getMovablesMap().findBulldozer();
		assertTrue(bulldozerCoord[0]==nextMove.getXo());
		assertTrue(bulldozerCoord[1]==nextMove.getYo());
		assertTrue(astar.getLevel().getMovablesMap().getMovables()[1][2].getClass()==Barrel.class);
		
		System.out.println(astar.getLevel().getMovablesMap().toString());
		assertTrue(astar.isAnyBarrelAtCorner());
		/////////////////////
		nextMove = nextMove.calcNextMove('L');
		//astar.checkMoves();
		astar.getLevel().move(nextMove);
		System.out.println(astar.getLevel().getMovablesMap().toString());
		assertTrue(astar.isAnyBarrelAtCorner());
		//////////////////
		
		return astar;
		
	}
	
	
	
	public void InitLevel2(){
		
			try {
				
	            level1 = LevelFactory.createFromProperties("level2");
	            astar = new AStar(level1);
	            initMoves();
	            System.out.println(level1);
	            movableMap= level1.getMovablesMap();
	        	map = level1.getMap();
	            barrels = movableMap.getBarrels();
	        	spots = map.getSpots();
	        	
	        	BarrelSpotPair<Barrel, Spot> pair ;
	    		barrelSpotPairs = new ArrayList<BarrelSpotPair<Barrel,Spot>>();
	    		for (int i =0; i<4;i++) {
	    			pair = new BarrelSpotPair<Barrel, Spot>(barrels.get(i), spots.get(i));
	    			barrelSpotPairs.add(pair);
	    		}
	    		
	        	
	        	
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
				
	}
	
}
