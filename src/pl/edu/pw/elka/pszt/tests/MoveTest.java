package pl.edu.pw.elka.pszt.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;

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
		//Level level1 = round1.getLevel();
		assertEquals("class pl.edu.pw.elka.pszt.models.Floor", level1.getMap().getMapObjects()[2][2].getClass().toString());
		assertEquals("class pl.edu.pw.elka.pszt.models.Wall", level1.getMap().getMapObjects()[1][1].getClass().toString());
		assertEquals("class pl.edu.pw.elka.pszt.models.Spot", level1.getMap().getMapObjects()[2][3].getClass().toString());
		assertEquals("class pl.edu.pw.elka.pszt.models.Barrel", level1.getMovablesMap().getMovables()[2][2].getClass().toString());
		assertEquals("class pl.edu.pw.elka.pszt.models.Bulldozer", level1.getMovablesMap().getMovables()[4][3].getClass().toString());
		
	}
	
	@Test
	public void moveTest() {
		 Move current = round1.getMoveTree().getCurrentNode();
		 round1.setBarrelSpotPairs(barrelSpotPairs);
		canMoveForwardTest();
		canMoveLeftTest();
		canMoveRightTest();
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
		
		round1.setBarrelSpotPairs(barrelSpotPairs);
		
		// h - (h2 only) distance  barrel to spot
		assertEquals(4, round1.h(barrelSpotPairs.get(0)) );
		assertEquals(4, round1.h(barrelSpotPairs.get(1)));
		assertEquals(4, round1.h(barrelSpotPairs.get(2)));
		
		/**
		 *  h - (h2 +h1) distance  barrel to spot
		assertEquals(10, round1.h(barrelSpotPairs.get(0)) );
		assertEquals(10, round1.h(barrelSpotPairs.get(1)));
		assertEquals(10, round1.h(barrelSpotPairs.get(2)));
		
		*/
		round1.setInitialMove(initMoveN);
		// g - moves count
		assertTrue(round1.g()==0);
		
		Move nextMove =null;
		for(int i=1 ;i<4; i++){
			nextMove = round1.generateNewMovesPop();
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
	public void findMinMoveFromTreeTest(){
		
		System.out.println("\n findMinMoveFromTreeTest \n");
		
		
		ArrayList<BarrelSpotPair<Barrel, Spot>> barrelSpotPairsT = barrelSpotPairs;
		Collections.reverse(barrelSpotPairsT);
		round1.setBarrelSpotPairs(barrelSpotPairsT);
		round1.setInitialMove(initMoveS);
		int h2 =0;
		Move minMove = round1.generateNewMovesPop();
		for(int i=2 ;i<37; i++){
			minMove = round1.generateNewMovesPop();
			System.out.println("minmove # "+ i + " f=" +minMove.getF() + "  move: "+  minMove);
			round1.moveFromRoot(minMove);
			System.out.println(round1.getLevel().getMovablesMap());
			h2 =  round1.h2();
			System.out.println("FMIN =" + minMove.getF() + "=" + round1.g() + " + " + round1.h2() );
			round1.goBackToRoot(minMove);
			if(h2==0){
				assertTrue(true);
				System.out.println("route found");
				break;
			}
		}
		
		//round1.moveFromRootWitDisp(minMove);
		
		//assertTrue(round1.isBarrelAtSpot(round1.getBarrelSpotPairs().get(0)));
		//assertEquals(minMove.getSize(), 6);
		//round1.goBackToRoot(minMove);
	}
	
	
	
	
	
	
	@Test
	public void minMoveTest(){
		
		//round1.setInitialMove(initMoveN);
		//round1.setBarrelSpotPairs(barrelSpotPairs);
		//round1.setInitialMove(initMoveW);
		//round1.setBarrelSpotPairs(barrelSpotPairs);
		System.out.println("\n minMoveTest \n");
		
		ArrayList<BarrelSpotPair<Barrel, Spot>> barrelSpotPairsT = barrelSpotPairs;
		Collections.reverse(barrelSpotPairsT);
		round1.setBarrelSpotPairs(barrelSpotPairsT);
		round1.setInitialMove(initMoveN);
		
		// System.out.println(round1.getLevel().getMovablesMap().toString());
		
		//round1.start(barrelSpotPairs, initMoveN);
	}

	
	private void canMoveForwardTest(){
		System.out.println("\n canMoveForwardTest \n");
		
		round1.setInitialMove(initMoveN);
		Move current = round1.getMoveTree().getCurrentNode();
		assertTrue(round1.checkMove(current, 'F'));
		
		round1.setInitialMove(initMoveE);
		current = round1.getMoveTree().getCurrentNode();
		assertFalse(round1.checkMove(current,'F'));
		
		round1.setInitialMove(initMoveS);
		current = round1.getMoveTree().getCurrentNode();
		assertTrue(round1.checkMove(current,'F'));
		
		round1.setInitialMove(initMoveW);
		current = round1.getMoveTree().getCurrentNode();
		assertTrue(round1.checkMove(current,'F'));
		
	}	
	
	private void canMoveLeftTest(){
		System.out.println("\n canMoveLeftTest \n");
		
		round1.setInitialMove(initMoveN);
		Move current = round1.getMoveTree().getCurrentNode();
		assertTrue(round1.checkMove(current,'L'));
		
		round1.setInitialMove(initMoveE);
		current = round1.getMoveTree().getCurrentNode();
		assertTrue(round1.checkMove(current,'L'));
		
		round1.setInitialMove(initMoveS);
		current = round1.getMoveTree().getCurrentNode();
		assertFalse(round1.checkMove(current,'L'));
		
		round1.setInitialMove(initMoveW);
		current = round1.getMoveTree().getCurrentNode();
		assertTrue(round1.checkMove(current,'L'));
		
		
	}
	
	private void canMoveRightTest(){
		System.out.println("\n canMoveRightTest \n");
		
		round1.setInitialMove(initMoveN);
		Move current = round1.getMoveTree().getCurrentNode();
		assertFalse(round1.checkMove(current,'R'));
		
		round1.setInitialMove(initMoveE);
		current = round1.getMoveTree().getCurrentNode();
		assertTrue(round1.checkMove(current,'R'));
		
		round1.setInitialMove(initMoveS);
		current = round1.getMoveTree().getCurrentNode();
		assertTrue(round1.checkMove(current,'R'));
		
		round1.setInitialMove(initMoveW);
		current = round1.getMoveTree().getCurrentNode();
		assertTrue(round1.checkMove(current,'R'));
		
	}
	
	private void moveBulldozerTest(){
		
		System.out.println("\n moveBulldozerTest \n");
		System.out.println(round1.getLevel().getMovablesMap().toString());
		
		Move nextMove = initMoveN.calcNextMove('F');
		//round1.checkMoves();
		round1.getLevel().move(nextMove);
		int [] bulldozerCoord = round1.getLevel().getMovablesMap().findBulldozer();
		
		
		assertTrue(bulldozerCoord[0]==nextMove.getXo());
		assertTrue(bulldozerCoord[1]==nextMove.getYo());
		nextMove = nextMove.calcNextMove('L');
		//round1.checkMoves();
		round1.getLevel().move(nextMove);
		//
		System.out.println(round1.getLevel().getMovablesMap().toString());
		//
		bulldozerCoord = round1.getLevel().getMovablesMap().findBulldozer();
		assertTrue(bulldozerCoord[0]==nextMove.getXo());
		assertTrue(bulldozerCoord[1]==nextMove.getYo());
		
		nextMove = nextMove.calcNextMove('F');
		//round1.checkMoves();
		round1.getLevel().move(nextMove);
		bulldozerCoord = round1.getLevel().getMovablesMap().findBulldozer();
		assertTrue(bulldozerCoord[0]==nextMove.getXo());
		assertTrue(bulldozerCoord[1]==nextMove.getYo());
		assertTrue(round1.getLevel().getMovablesMap().getMovables()[1][2].getClass()==Barrel.class);
		
		System.out.println(round1.getLevel().getMovablesMap().toString());
		
	}
	
	
}
