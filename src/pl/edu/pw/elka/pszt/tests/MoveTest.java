package pl.edu.pw.elka.pszt.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import pl.edu.pw.elka.pszt.game.Move;
import pl.edu.pw.elka.pszt.game.Round;
import pl.edu.pw.elka.pszt.models.Bulldozer;
import pl.edu.pw.elka.pszt.models.Level;
import pl.edu.pw.elka.pszt.utils.LevelFactory;

public class MoveTest {

	Round round1;
	
	public MoveTest(){
		try {
			
            Level level1 = LevelFactory.createFromProperties("level1");
            round1 = new Round(level1);
            
            System.out.println(level1);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
			
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
		round1.getLevel().getBulldozer();
		
		Move initialMove = new Move(
				round1.getLevel().getMovablesMap().getBulldozerX()-1,
				round1.getLevel().getMovablesMap().getBulldozerY(),
				round1.getLevel().getMovablesMap().getBulldozerX(),
				round1.getLevel().getMovablesMap().getBulldozerY()
				);
		round1.setLastMove(initialMove);
		round1.checkMoves();
		initialMove = new Move(
				round1.getLevel().getMovablesMap().getBulldozerX()+1,
				round1.getLevel().getMovablesMap().getBulldozerY(),
				round1.getLevel().getMovablesMap().getBulldozerX(),
				round1.getLevel().getMovablesMap().getBulldozerY()
				);
		round1.setLastMove(initialMove);
		round1.checkMoves();
		initialMove = new Move(
				round1.getLevel().getMovablesMap().getBulldozerX(),
				round1.getLevel().getMovablesMap().getBulldozerY()-1,
				round1.getLevel().getMovablesMap().getBulldozerX(),
				round1.getLevel().getMovablesMap().getBulldozerY());
		round1.setLastMove(initialMove);
		round1.checkMoves();
		initialMove = new Move(
				round1.getLevel().getMovablesMap().getBulldozerX(),
				round1.getLevel().getMovablesMap().getBulldozerY()+1,
				round1.getLevel().getMovablesMap().getBulldozerX(),
				round1.getLevel().getMovablesMap().getBulldozerY());
		round1.setLastMove(initialMove);
		round1.checkMoves();
	}

}
