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
		assertEquals(4, astar.h(barrelSpotPairs.get(0)) );
		assertEquals(4, astar.h(barrelSpotPairs.get(1)));
		assertEquals(4, astar.h(barrelSpotPairs.get(2)));
		
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
	public void findMinMoveFromTreeTest(){
		
		System.out.println("\n findMinMoveFromTreeTest \n");
	/*
		//
		BarrelSpotPair<Barrel, Spot> pair ;
		barrelSpotPairs = new ArrayList<BarrelSpotPair<Barrel,Spot>>();
	
		pair = new BarrelSpotPair<Barrel, Spot>(barrels.get(0), spots.get(2));
		barrelSpotPairs.add(pair);
		pair = new BarrelSpotPair<Barrel, Spot>(barrels.get(1), spots.get(0));
		barrelSpotPairs.add(pair);
		pair = new BarrelSpotPair<Barrel, Spot>(barrels.get(2), spots.get(1));
		barrelSpotPairs.add(pair);
		*/
		//
		
		ArrayList<BarrelSpotPair<Barrel, Spot>> barrelSpotPairsT = barrelSpotPairs;
		//Collections.reverse(barrelSpotPairsT);
		astar.setBarrelSpotPairs(barrelSpotPairsT);
		astar.setInitialMove(initMoveN);
		int h2 =0;
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SS");
		Date startdate = new Date();
		
		
		Move minMove = astar.generateNewMovesPop();
		for(int i=2 ;i<50; i++){
			minMove = astar.generateNewMovesPop();
			System.out.println("minmove # "+ i + " f=" +minMove.getF() + "  move: "+  minMove);
			astar.moveFromRoot(minMove);
			System.out.println(astar.getLevel().getMovablesMap());
			h2 =  astar.h2();
			System.out.println("FMIN =" + minMove.getF() + "=" + astar.g() + " + " + astar.h2() );
			astar.goBackToRoot(minMove);
			if(h2==0){
				assertTrue(true);
				System.out.println("route found");
				break;
			}
		}
		System.out.println("start time" + dateFormat.format(startdate));
		Date enddate = new Date();
		
		System.out.println("end   time" + dateFormat.format(enddate) );
		
	}
	
	
	
	
	
	
	@Test
	public void minMoveTest(){
		
		//astar.setInitialMove(initMoveN);
		//astar.setBarrelSpotPairs(barrelSpotPairs);
		//astar.setInitialMove(initMoveW);
		//astar.setBarrelSpotPairs(barrelSpotPairs);
		System.out.println("\n minMoveTest \n");
		
		ArrayList<BarrelSpotPair<Barrel, Spot>> barrelSpotPairsT = barrelSpotPairs;
		Collections.reverse(barrelSpotPairsT);
		astar.setBarrelSpotPairs(barrelSpotPairsT);
		astar.setInitialMove(initMoveN);
		
		// System.out.println(astar.getLevel().getMovablesMap().toString());
		
		//astar.start(barrelSpotPairs, initMoveN);
	}
	
	
	@Test
	public void barrelAtCornerTest(){
		
		/* DD rezygnyje nie daje dobrych wyników
		System.out.println("\n barrelAtCornerTest \n");
		
		astar.setBarrelSpotPairs(barrelSpotPairs);
		assertFalse(astar.barrelAtCorner());
		//System.out.println("\n moveBulldozerTest \n");
				System.out.println(astar.getLevel().getMovablesMap().toString());
				
				Move nextMove = initMoveS.calcNextMove('F');
				//astar.checkMoves();
				astar.getLevel().move(nextMove);
				nextMove = nextMove.calcNextMove('R');
				astar.getLevel().move(nextMove);
				System.out.println(astar.getLevel().getMovablesMap().toString());

				nextMove = nextMove.calcNextMove('R');
				astar.getLevel().move(nextMove);
				System.out.println(astar.getLevel().getMovablesMap().toString());
				nextMove = nextMove.calcNextMove('L');
				astar.getLevel().move(nextMove);
				System.out.println(astar.getLevel().getMovablesMap().toString());
				nextMove = nextMove.calcNextMove('F');
				astar.getLevel().move(nextMove);
				System.out.println(astar.getLevel().getMovablesMap().toString());
				nextMove = nextMove.calcNextMove('L');
				astar.getLevel().move(nextMove);
				System.out.println(astar.getLevel().getMovablesMap().toString());

				nextMove = nextMove.calcNextMove('L');
				astar.getLevel().move(nextMove);
				System.out.println(astar.getLevel().getMovablesMap().toString());
				nextMove = nextMove.calcNextMove('F');
				astar.getLevel().move(nextMove);
				System.out.println(astar.getLevel().getMovablesMap().toString());
				nextMove = nextMove.calcNextMove('L');
				astar.getLevel().move(nextMove);
				System.out.println(astar.getLevel().getMovablesMap().toString());
				
			
				System.out.println(astar.getLevel().getMovablesMap().toString());
				nextMove = nextMove.calcNextMove('R');
				astar.getLevel().move(nextMove);
				System.out.println(astar.getLevel().getMovablesMap().toString());
				nextMove = nextMove.calcNextMove('L');
				astar.getLevel().move(nextMove);
				System.out.println(astar.getLevel().getMovablesMap().toString());
				
				
				System.out.println(astar.getLevel().getMovablesMap().toString());
				nextMove = nextMove.calcNextMove('F');
				astar.getLevel().move(nextMove);
				System.out.println(astar.getLevel().getMovablesMap().toString());
				nextMove = nextMove.calcNextMove('L');
				astar.getLevel().move(nextMove);
				System.out.println(astar.getLevel().getMovablesMap().toString());
				
				nextMove = nextMove.calcNextMove('L');
				astar.getLevel().move(nextMove);
				System.out.println(astar.getLevel().getMovablesMap().toString());
				nextMove = nextMove.calcNextMove('F');
				astar.getLevel().move(nextMove);
				
				
				
		 System.out.println(astar.getLevel().getMovablesMap().toString());
		
		 assertTrue(astar.barrelAtCorner());
		*/
		
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
	
	private AStar moveBulldozerTest(){
		
		//System.out.println("\n moveBulldozerTest \n");
		System.out.println(astar.getLevel().getMovablesMap().toString());
		
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
		return astar;
		
	}
	
	
}
