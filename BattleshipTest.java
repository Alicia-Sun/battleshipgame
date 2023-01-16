package org.cis1200.battleship;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * You can use this file (and others) to test your
 * implementation.
 */

public class BattleshipTest {
    String[][] sampleboard = {  {"0","0","0","0","4","4","4","4"},
                                {"0","0","0","0","0","0","0","0"},
                                {"0","0","3","3","3","0","0","0"},
                                {"0","0","0","0","0","0","5","0"},
                                {"0","0","0","0","0","0","5","0"},
                                {"0","0","0","0","0","0","5","0"},
                                {"2","0","0","0","1","0","5","0"},
                                {"2","0","0","0","0","0","5","0"}};

    @Test
    public void testPlayTurnHit() throws IOException {
        //player is not allowed to attack the same space. After, it is still
        //their turn since attack failed.
        Battleship dummy = new Battleship();
        dummy.setShootBoard(sampleboard);
        dummy.playTurn(0,6);
        dummy.playTurn(0,6);

        assertEquals("X",dummy.getShootCell(0,6));
    }

    @Test
    public void testPlayTurnMiss() throws IOException {
        //player is not allowed to attack the same space. After, it is still
        //their turn since attack failed.
        Battleship dummy = new Battleship();
        dummy.setShootBoard(sampleboard);
        dummy.playTurn(0,5);
        dummy.playTurn(0,5);

        assertEquals("M",dummy.getShootCell(0,5));
    }

    @Test
    public void testPlayTurnPlayerComputerSameSquare() throws IOException {
        //player and computer are allowed to attack same square since they use
        //different grids
        Battleship dummy = new Battleship();
        dummy.playTurn(1,1);

        assertTrue(dummy.playTurn(1,1));
        assertTrue(dummy.getCurrentPlayer());
    }

    @Test
    public void testPlayTurnPlayerShootsSameSquare() throws IOException {
        //edge case if player attacks the same space. After, it is still
        //their turn since attack failed.
        Battleship dummy = new Battleship();
        dummy.playTurn(1,1);
        dummy.playTurn(1,1);

        assertFalse(dummy.playTurn(1,1));
        assertTrue(dummy.getCurrentPlayer());
    }

    @Test
    public void testSinkShip() throws IOException {
        //board contains no more of a certain ship after it is sunk
        Battleship dummy = new Battleship();
        dummy.setShootBoard(sampleboard);
        dummy.playTurn(0,6);
        dummy.playTurn(0,6);

        Set<String> beforesinkshot = dummy.shipSet(dummy.getshootboard());
        dummy.playTurn(0,7); // 2 ship is sunk
        Set<String> aftersinkshot = dummy.shipSet(dummy.getshootboard());

        assertEquals("2",dummy.sunkship(beforesinkshot, aftersinkshot));
    }

    @Test
    public void testReset() throws IOException {
        Battleship dummy = new Battleship();
        dummy.setShootBoard(sampleboard);
        dummy.playTurn(0,6);
        dummy.playTurn(0,6);
        dummy.reset();
        Boolean equals = (dummy.getshootboard()[6][0].equals("2") &&
                        dummy.getshootboard()[7][0].equals("2") &&

                        dummy.getshootboard()[6][4].equals("1") &&

                        dummy.getshootboard()[2][2].equals("3") &&
                        dummy.getshootboard()[2][3].equals("3") &&
                        dummy.getshootboard()[2][4].equals("3") &&

                        dummy.getshootboard()[0][4].equals("4") &&
                        dummy.getshootboard()[0][5].equals("4") &&
                        dummy.getshootboard()[0][6].equals("4") &&
                        dummy.getshootboard()[0][7].equals("4") &&

                        dummy.getshootboard()[3][6].equals("5") &&
                        dummy.getshootboard()[4][6].equals("5") &&
                        dummy.getshootboard()[5][6].equals("5") &&
                        dummy.getshootboard()[6][6].equals("5") &&
                        dummy.getshootboard()[7][6].equals("5"));

        assertFalse(equals);
        assertEquals(0,dummy.numRounds());
    }

    @Test
    public void testResetContainsAllShips() throws IOException {
        Battleship dummy = new Battleship();
        dummy.setShootBoard(sampleboard);
        dummy.playTurn(0,6);
        dummy.playTurn(0,6);
        dummy.reset();

        assertEquals(5, dummy.shipSet(dummy.getshootboard()).size());
    }

    @Test
    public void testResume() throws IOException {
        Battleship dummy = new Battleship();
        //resume is coded to only activate after reset is called
        //twice since running the game calls it twice.
        dummy.reset();
        dummy.setShootBoard(sampleboard);
        dummy.playTurn(0,6);
        dummy.playTurn(0,6);
        //hit one ship

        dummy.reset();
        dummy.resume();
        Boolean equals = (dummy.getshootboard()[6][0].equals("X") &&
                dummy.getshootboard()[7][0].equals("2") &&
                dummy.getshootboard()[6][4].equals("1") &&
                dummy.getshootboard()[2][2].equals("3") &&
                dummy.getshootboard()[2][3].equals("3") &&
                dummy.getshootboard()[2][4].equals("3") &&
                dummy.getshootboard()[0][4].equals("4") &&
                dummy.getshootboard()[0][5].equals("4") &&
                dummy.getshootboard()[0][6].equals("4") &&
                dummy.getshootboard()[0][7].equals("4") &&
                dummy.getshootboard()[3][6].equals("5") &&
                dummy.getshootboard()[4][6].equals("5") &&
                dummy.getshootboard()[5][6].equals("5") &&
                dummy.getshootboard()[6][6].equals("5") &&
                dummy.getshootboard()[7][6].equals("5"));

        assertTrue(equals);
        assertEquals(1,dummy.numRounds());
    }

    @Test
    public void testCheckWinner() throws IOException {
        Battleship dummy = new Battleship();
        dummy.setShootBoard(sampleboard);
        dummy.playTurn(0,6);
        dummy.playTurn(0,6);
        dummy.playTurn(0,7);
        dummy.playTurn(0,7);

        dummy.playTurn(4,6);

        dummy.playTurn(2,2);
        dummy.playTurn(2,2);
        dummy.playTurn(3,2);
        dummy.playTurn(3,2);
        dummy.playTurn(4,2);
        dummy.playTurn(4,2);

        dummy.playTurn(4,0);
        dummy.playTurn(4,0);
        dummy.playTurn(5,0);
        dummy.playTurn(5,0);
        dummy.playTurn(6,0);
        dummy.playTurn(6,0);
        dummy.playTurn(7,0);
        dummy.playTurn(7,0);

        dummy.playTurn(6,3);
        dummy.playTurn(6,3);
        dummy.playTurn(6,4);
        dummy.playTurn(6,4);
        dummy.playTurn(6,5);
        dummy.playTurn(6,5);
        dummy.playTurn(6,6);
        dummy.playTurn(6,6);
        dummy.playTurn(6,7);
        dummy.playTurn(6,7);

        assertEquals(1,dummy.checkWinner());
        assertTrue(dummy.shipSet(dummy.getshootboard()).isEmpty());
    }

    @Test
    public void testUndo() throws IOException {
        Battleship dummy = new Battleship();
        dummy.setShootBoard(sampleboard);
        dummy.playTurn(0,6);
        dummy.playTurn(0,6);
        dummy.undo();

        assertEquals("2",dummy.getShootCell(0,6));
        assertEquals(0,dummy.numRounds());
    }

    @Test
    public void testUndoAfterResuming() throws IOException {
        Battleship dummy = new Battleship();
        //edge case test for undo button after resuming a prev game.
        //resume is coded to only activate after reset is called
        //twice since running the game calls it twice.
        dummy.reset();
        dummy.setShootBoard(sampleboard);
        dummy.playTurn(0,6);
        dummy.playTurn(0,6);
        //hit one ship

        dummy.reset();
        dummy.resume();
        dummy.undo();
        Boolean equals = (dummy.getshootboard()[6][0].equals("2") &&
                dummy.getshootboard()[7][0].equals("2") &&
                dummy.getshootboard()[6][4].equals("1") &&
                dummy.getshootboard()[2][2].equals("3") &&
                dummy.getshootboard()[2][3].equals("3") &&
                dummy.getshootboard()[2][4].equals("3") &&
                dummy.getshootboard()[0][4].equals("4") &&
                dummy.getshootboard()[0][5].equals("4") &&
                dummy.getshootboard()[0][6].equals("4") &&
                dummy.getshootboard()[0][7].equals("4") &&
                dummy.getshootboard()[3][6].equals("5") &&
                dummy.getshootboard()[4][6].equals("5") &&
                dummy.getshootboard()[5][6].equals("5") &&
                dummy.getshootboard()[6][6].equals("5") &&
                dummy.getshootboard()[7][6].equals("5"));

        assertTrue(equals);
        assertEquals(0,dummy.numRounds());
    }
}
