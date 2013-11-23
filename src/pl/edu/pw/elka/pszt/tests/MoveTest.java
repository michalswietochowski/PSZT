package pl.edu.pw.elka.pszt.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import pl.edu.pw.elka.pszt.game.Move;
import pl.edu.pw.elka.pszt.game.Round;
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
	Round round1;
	Move initMoveN, initMoveE, initMoveS, initMoveW;
	ArrayList<Barrel> barrels;
	ArrayList<Spot> spots;
	MovablesMap movableMap;
	Map map;
	ArrayList<BarrelSpotPair<Barrel, Spot>> barrelSpotPairs;
	
	public MoveTest(){
		try {
			
            level1 = LevelFactory.createFromProperties("level1");
            round1 = new Round(level1);
            initMoves();
            //System.out.println(level1);
            movableMap= round1.getLevel().getMovablesMap();
        	map = round1.getLevel().getMap();
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
		int [] bulldozerCoord = round1.getLevel().getMovablesMap().findBulldozer();
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
		Level level1 = round1.getLevel();
		assertEquals("class pl.edu.pw.elka.pszt.models.Floor", level1.getMap().getMapObjects()[2][2].getClass().toString());
		assertEquals("class pl.edu.pw.elka.pszt.models.Wall", level1.getMap().getMapObjects()[1][1].getClass().toString());
		assertEquals("class pl.edu.pw.elka.pszt.models.Spot", level1.getMap().getMapObjects()[2][3].getClass().toString());
		assertEquals("class pl.edu.pw.elka.pszt.models.Barrel", level1.getMovablesMap().getMovables()[2][2].getClass().toString());
		assertEquals("class pl.edu.pw.elka.pszt.models.Bulldozer", level1.getMovablesMap().getMovables()[4][3].getClass().toString());
		
	}
	
	@Test
	public void moveTest() {
		canMoveForwardTest();
		canMoveLeftTest();
		canMoveRightTest();
		moveBulldozerTest();
	}
	
	@Test
	public void distanceFunctionsTest(){
		System.out.println("\n distanceFunctionsTest \n");
		// h - distance from bulldozer to barrel
		assertEquals(round1.h(barrelSpotPairs.get(0)), 4);
		assertEquals(round1.h(barrelSpotPairs.get(1)), 3);
		assertEquals(round1.h(barrelSpotPairs.get(2)), 3);
		
		// g - moves count
		assertTrue(round1.g()==0);
		Move nextMove = initMoveN.calcNextMove('F');
		round1.move(nextMove);
		assertTrue(round1.g()==1);
		nextMove = nextMove.calcNextMove('L');
		round1.move(nextMove);
		assertTrue(round1.g()==2);
		nextMove = nextMove.calcNextMove('F');
		round1.move(nextMove);
		assertTrue(round1.g()==3);
		
		assertEquals(round1.h(barrelSpotPairs.get(0)), 3);
		assertEquals(round1.h(barrelSpotPairs.get(1)), 4);
		assertEquals(round1.h(barrelSpotPairs.get(2)), 4);		
		
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
	public void minMoveTest(){
		
		round1.addLastMove(initMoveW);
		round1.setBarrelSpotPairs(barrelSpotPairs);
		System.out.println("\n minMoveTest \n");
		System.out.println(round1.getLevel().getMovablesMap().toString());
		
		round1.checkMoves();
		Move minMove = round1.findMinMove();
		round1.move(minMove);
		System.out.println(round1.getLevel().getMovablesMap().toString());
		
		round1.checkMoves();
		minMove = round1.findMinMove();
		round1.move(minMove);
		System.out.println( round1.getLevel().getMovablesMap().toString());
		
		round1.checkMoves();
		minMove = round1.findMinMove();
		round1.move(minMove);
		System.out.println(round1.getLevel().getMovablesMap().toString());
		
		round1.checkMoves();
		minMove = round1.findMinMove();
		round1.move(minMove);
		System.out.println(round1.getLevel().getMovablesMap().toString());
		
		round1.checkMoves();
		minMove = round1.findMinMove();
		round1.move(minMove);
		System.out.println(round1.getLevel().getMovablesMap().toString());
		
	}

	
	private void canMoveForwardTest(){
		System.out.println("\n canMoveForwardTest \n");
		 
		round1.addLastMove(initMoveN);
		assertTrue(round1.checkMove('F'));
		round1.addLastMove(initMoveE);
		assertFalse(round1.checkMove('F'));
		round1.addLastMove(initMoveS);
		assertTrue(round1.checkMove('F'));
		round1.addLastMove(initMoveW);
		assertTrue(round1.checkMove('F'));
		
	}	
	
	private void canMoveLeftTest(){
		System.out.println("\n canMoveLeftTest \n");
		
		round1.addLastMove(initMoveN);
		assertTrue(round1.checkMove('L'));
		round1.addLastMove(initMoveE);
		assertTrue(round1.checkMove('L'));
		round1.addLastMove(initMoveS);
		assertFalse(round1.checkMove('L'));
		round1.addLastMove(initMoveW);
		assertTrue(round1.checkMove('L'));
		
	}
	
	private void canMoveRightTest(){
		System.out.println("\n canMoveRightTest \n");
		
		round1.addLastMove(initMoveN);
		assertFalse(round1.checkMove('R'));
		round1.addLastMove(initMoveE);
		assertTrue(round1.checkMove('R'));
		round1.addLastMove(initMoveS);
		assertTrue(round1.checkMove('R'));
		round1.addLastMove(initMoveW);
		assertTrue(round1.checkMove('R'));
		
	}
	
	private void moveBulldozerTest(){
		System.out.println("\n moveBulldozerTest \n");
		System.out.println(round1.getLevel().getMovablesMap().toString());
		
		Move nextMove = initMoveN.calcNextMove('F');
		round1.move(nextMove);
		int [] bulldozerCoord = round1.getLevel().getMovablesMap().findBulldozer();
		
		
		assertTrue(bulldozerCoord[0]==nextMove.getXo());
		assertTrue(bulldozerCoord[1]==nextMove.getYo());
		nextMove = nextMove.calcNextMove('L');
		round1.move(nextMove);
		//
		System.out.println(round1.getLevel().getMovablesMap().toString());
		//
		bulldozerCoord = round1.getLevel().getMovablesMap().findBulldozer();
		assertTrue(bulldozerCoord[0]==nextMove.getXo());
		assertTrue(bulldozerCoord[1]==nextMove.getYo());
		
		nextMove = nextMove.calcNextMove('F');
		round1.move(nextMove);
		bulldozerCoord = round1.getLevel().getMovablesMap().findBulldozer();
		assertTrue(bulldozerCoord[0]==nextMove.getXo());
		assertTrue(bulldozerCoord[1]==nextMove.getYo());
		assertTrue(round1.getLevel().getMovablesMap().getMovables()[1][2].getClass()==Barrel.class);
		
		System.out.println(round1.getLevel().getMovablesMap().toString());
	}
	
	
}
