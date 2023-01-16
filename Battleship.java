package org.cis1200.battleship;

/**
 * CIS 120 HW09 - Battleship Project
 * (c) University of Pennsylvania
 * Created by Alicia Sun.
 */

import javax.swing.*;
import java.io.*;
import java.util.*;

/**
 * This class is a model for Battleship.
 * 
 * This game adheres to a Model-View-Controller design framework.
 * This framework is very effective for turn-based games.
 *
 * This model is completely independent of the view and controller.
 * This is in keeping with the concept of modularity! We can play
 * the whole game from start to finish without ever drawing anything
 * on a screen or instantiating a Java Swing object.
 * 
 * Run this file to see the main method play a game of Battleship,
 * visualized with Strings printed to the console.
 */
public class Battleship {

    private String[][] shootboard;
    private String[][] myboard;
    private int numShots;
    private boolean gameOver;
    private boolean player1;
    private int firstgame;
    private Map<Integer, String[]> pmoves = new HashMap<Integer, String[]>(); // stores player moves
    private Map<Integer, String[]> cmoves = new HashMap<Integer, String[]>(); // stores comp moves
    File saved = new File("files/savedprev.csv");


    /**
     * Constructor sets up game state.
     */
    public Battleship() throws IOException {
        firstgame = 0;
        reset();
    }


    /**
     * playTurn allows players to play a turn. Returns true if the move is
     * successful and false if a player tries to play in a location that is
     * taken or after the game has ended. If the turn is successful and the game
     * has not ended, the player is changed. If the turn is unsuccessful or the
     * game has ended, the player is not changed.
     *
     * @param c column to play in
     * @param r row to play in
     * @return whether the turn was successful
     */
    public boolean playTurn(int c, int r) {
        if (player1) {
            if ((shootboard[r][c].equals("X")) || (shootboard[r][c].equals("M") || gameOver)) {
                return false;
            }
            //shoot onto shootboard
            String[] shot = {String.valueOf(c), String.valueOf(r),
                    shootboard[r][c]};
            pmoves.put(numRounds() + 1, shot);
            if (shootboard[r][c].equals("0")) {
                shootboard[r][c] = "M";
            } else {
                Set<String> playerbefore = shipSet(shootboard);
                shootboard[r][c] = "X";
                Set<String> playerafter = shipSet(shootboard);
                playerbefore.removeAll(playerafter);
                if (!playerbefore.isEmpty()) {
                    //a ship was sunk
                    Object[] sunkship = playerbefore.toArray();
                    String ship = sunkship[0].toString();
                    JOptionPane.showMessageDialog(null, "You sunk ship " + ship,
                            "Sunk Ship!", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } else {
            //the computer's move. shoot onto myboard
            if ((myboard[r][c].equals("X")) || (myboard[r][c].equals("M") || gameOver)) {
                return false;
            }
            String[] shot = {String.valueOf(c), String.valueOf(r),
                    myboard[r][c]};
            cmoves.put(numRounds() + 1, shot);
            if (myboard[r][c] == "0") {
                myboard[r][c] = "M";
            } else {
                myboard[r][c] = "X";
            }
        }
        numShots++;
        if (checkWinner() == 0) {
            player1 = !player1;
        }
        return true;
    }

    /**
     * getmyboard returns private field myboard
     * @return myboard
     */
    public String[][] getmyboard() {
        return myboard;
    }

    /**
     * getshootboard returns private field shootboard
     * @return shootboard
     */
    public String[][] getshootboard() {
        return shootboard;
    }

    /**
     * numRounds returns the number shots divided by 2
     * @return number of rounds
     */
    public int numRounds() {
        return numShots / 2;
    }

    /**
     * fillField randomly generates ships on the board. Ships are not allowed
     * to be placed directly next to each other or on top of one another.
     *
     * @return a random 2-d array of the 5 ships.
     */
    public String[][] fillField() {
        Random random = new Random();

        //initialize field to empty ocean
        String[][] field = new String[8][8];
        for (int a = 0; a < 8; a++) {
            for (int b = 0; b < 8; b++) {
                field[a][b] = "0";
            }
        }

        for (int i = 5; i > 0; i--) {
            //System.out.println("Placing ship with length: " + i);
            // start point of the ship and direction
            int x = random.nextInt(8);
            int y = random.nextInt(8);
            boolean vertical = random.nextBoolean();

            // correct start point so that the ship could fit in the field
            if (vertical) {
                if (y + i > 8) {
                    y = 8 - i;
                }
            } else if (x + i > 8) {
                x = 8 - i;
            }
            boolean isFree = true;
            // check for free space
            if (vertical) {
                for (int m = y; m < y + i; m++) {
                    if (!field[m][x].equals("0")) {
                        isFree = false;
                        break;
                    }
                }
            } else {
                for (int n = x; n < x + i; n++) {
                    if (!field[y][n].equals("0")) {
                        isFree = false;
                        break;
                    }
                }
            }
            if (!isFree) {  // no free space found, retry
                i++;
                continue;
            }

            // fill in the adjacent cells using "9" as a filler
            if (vertical) {
                for (int m = Math.max(0, x - 1); m < Math.min(8, x + 2); m++) {
                    for (int n = Math.max(0, y - 1); n < Math.min(8, y + i + 1); n++) {
                        field[n][m] = "9";
                    }
                }
            } else {
                for (int m = Math.max(0, y - 1); m < Math.min(8, y + 2); m++) {
                    for (int n = Math.max(0, x - 1); n < Math.min(8, x + i + 1); n++) {
                        field[m][n] = "9";
                    }
                }
            }
            // fill in the ship cells
            for (int j = 0; j < i; j++) {
                field[y][x] = String.valueOf(i);
                if (vertical) {
                    y++;
                } else {
                    x++;
                }
            }
        }

        //replace filler "9"s with "0"s
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (field[r][c].equals("9")) {
                    field[r][c] = "0";
                }
            }
        }
        return field;
    }

    /**
     * shipSet gets a set of the ships on the board
     *
     * @param board 2D string array of board
     * @return a set of the ship numbers left on the board
     */
    public Set<String> shipSet(String[][] board) {
        Set<String> ships = new HashSet<String>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                //checks if the spot has a ship
                if (board[i][j].equals("1") || board[i][j].equals("2") || board[i][j].equals("3")
                        || board[i][j].equals("4") || board[i][j].equals("5")) {
                    ships.add(board[i][j]);
                }
            }
        }
        return ships;
    }

    /**
     * sunkship returns the missing ship between two ship sets
     *
     * @param ss1 Set</String> of ships before a shot
     * @param ss2 Set</String> of ships after a shot
     * @return the string of the sunk ship
     */
    public String sunkship(Set<String> ss1, Set<String> ss2) {
        ss1.removeAll(ss2);
        Object[] sunkship = ss1.toArray();
        return sunkship[0].toString();
    }

    /**
     * checkWinner checks whether the game has reached a win condition.
     * checkWinner only looks for horizontal wins.
     *
     * @return 0 if nobody has won yet, 1 if player 1 has won, and 2 if player 2
     *         has won, 3 if the game hits stalemate
     */
    public int checkWinner() {
        if (shipSet(shootboard).isEmpty()) {
            gameOver = true;
            return 1;
        } else if (shipSet(myboard).isEmpty()) {
            gameOver = true;
            return 2;
        }
        return 0;
    }


    /**
     * printGameState prints the current game state
     * for debugging.
     */
    public void printGameState() {
        System.out.println("\n\nTurn " + numShots / 2 + ":\n");
        for (int i = 0; i < shootboard.length; i++) {
            for (int j = 0; j < shootboard[i].length; j++) {
                System.out.print(shootboard[i][j]);
                if (j < 7) {
                    System.out.print(" | ");
                }
            }
            if (i < 7) {
                System.out.println("\n---------");
            }
        }
    }


    /**
     * reset (re-)sets the game state to start a new game.
     * The previous game state is also saved in a csv file.
     */
    public void reset() throws IOException {
        if (firstgame > 1) {
            //if this is not the first game
            //save previous game state files/savedprev.csv
            StringBuilder builder = new StringBuilder();
            BufferedWriter writer = new BufferedWriter(new FileWriter(saved));
            for (int i = 0; i < 8; i++) { //for each row
                for (int j = 0; j < 8; j++) { //for each column
                    builder.append(shootboard[i][j] + "");//append to the output string
                    if (j < 7) {
                        //if this is not the last row element
                        builder.append(",");//then add comma
                    }
                }
                builder.append("\n");//append new line at the end of the row
            }
            for (int i = 0; i < 8; i++) { //for each row
                for (int j = 0; j < 8; j++) { //for each column
                    builder.append(myboard[i][j] + "");//append to the output string
                    if (j < 7) {
                        //if this is not the last row element
                        builder.append(",");//then add comma
                    }
                }
                builder.append("\n");//append new line at the end of the row
            }

            //add data for player1 variable
            if (player1) {
                builder.append("1"); //1 is player's turn
            } else {
                builder.append("2"); //2 is computer turn
            }
            builder.append("\n");

            //add data for gameOver
            if (!gameOver) {
                builder.append("0"); //game is not over
            } else {
                builder.append("1"); //game is over
            }
            builder.append("\n");

            //add data for numShots
            builder.append(String.valueOf(numShots));
            builder.append("\n");

            writer.write(builder.toString());//save the string representation of the board
            writer.close();

            //reset. generate random shootboard and myboard
            shootboard = fillField();
            myboard = fillField();

            player1 = true;
            gameOver = false;
            numShots = 0;
            firstgame = firstgame + 1;
        } else {
            //this is the first game
            shootboard = fillField();
            myboard = fillField();

            player1 = true;
            gameOver = false;
            numShots = 0;
            firstgame = firstgame + 1;
        }
    }

    /**
     * getCurrentPlayer is a getter for the player
     * whose turn it is in the game. While technically
     * both player and computer shoot at the same round,
     * if both sink the final ship on the same round,
     * the player is prioritized for the win!
     * 
     * @return true if it's Player's turn,
     *         false if it's Computer's turn.
     */
    public boolean getCurrentPlayer() {
        return player1;
    }

    /**
     * getCell is a getter for the contents of the shootboard cell specified by the method
     * arguments.
     *
     * @param c column to retrieve
     * @param r row to retrieve
     * @return a string denoting the contents of the corresponding cell on the
     *         game board.
     */
    public String getShootCell(int c, int r) {
        return shootboard[r][c];
    }

    /**
     * getCell is a getter for the contents of the myboard cell specified by the method
     * arguments.
     *
     * @param c column to retrieve
     * @param r row to retrieve
     * @return a string denoting the contents of the corresponding cell on the
     *         game board.
     */
    public String getMyCell(int c, int r) {
        return myboard[r][c];
    }

    /**
     * resume obtains the saved game state from the csv file and resets the
     * current game state to reflect such. If there is no previous game saved,
     * return a message explaining such
     */
    public void resume() throws IOException {
        if (firstgame > 2) {
            BufferedReader reader = new BufferedReader(new FileReader(saved));
            String line = "";
            int row = 0;
            while ((line = reader.readLine()) != null) {
                String[] cols = line.split(",");
                int col = 0;
                if (row >= 8) {
                    if (row >= 16) {
                        if (row >= 17) {
                            if (row >= 18) {
                                //numShots data
                                numShots = Integer.valueOf(cols[0]);
                            } else {
                                //gameOver data
                                if (cols[0].equals("0")) {
                                    gameOver = false;
                                } else {
                                    gameOver = true;
                                }
                                row++;
                                col = 0;
                            }
                        }
                        //player1 data
                        if (cols[0].equals("2")) {
                            player1 = false;
                        } else {
                            player1 = true;
                        }
                        row++;
                        col = 0;
                    } else {
                        //second 8 rows are myboard data
                        for (String  c : cols) {
                            myboard[row - 8][col] = c;
                            col++;
                        }
                        row++;
                        col = 0;
                    }
                } else {
                    //first 8 rows are shootboard data
                    for (String  c : cols) {
                        shootboard[row][col] = c;
                        col++;
                    }
                    row++;
                    col = 0;
                }
            }
            reader.close();
        }
    }

    /**
     * undo undoes the last round of moves by both the computer
     * and player
     */
    public void undo() {
        if (!gameOver) {
            int lastround = cmoves.size();
            int pcol = Integer.valueOf(pmoves.get(lastround)[0]);
            int prow = Integer.valueOf(pmoves.get(lastround)[1]);
            String psquare = pmoves.get(lastround)[2];
            int ccol = Integer.valueOf(cmoves.get(lastround)[0]);
            int crow = Integer.valueOf(cmoves.get(lastround)[1]);
            String csquare = cmoves.get(lastround)[2];
            shootboard[prow][pcol] = psquare;
            myboard[crow][ccol] = csquare;
            numShots = numShots - 2;
            pmoves.remove(lastround);
            cmoves.remove(lastround);
        }
    }


    /**
     * setShootBoard sets the shootboard state to a game state provided
     *      * by a 2-D String array input. This is useful for debugging as the
     *      * ships are guaranteed to be where we place them.
     * @param board is a 2D string array we feed this method
     */
    public void setShootBoard(String[][] board) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                shootboard[i][j] = board[i][j];
            }
        }
    }

    /**
     * This main method illustrates how the model is completely independent of
     * the view and controller. We can play the game from start to finish
     * without ever creating a Java Swing object.
     *
     * This is modularity in action, and modularity is the bedrock of the
     * Model-View-Controller design framework.
     *
     * Run this file to see the output of this method in your console.
     */
    public static void main(String[] args) throws IOException {
        Battleship t = new Battleship();

        t.playTurn(1, 1);
        t.printGameState();

        t.playTurn(0, 0);
        t.printGameState();

        t.playTurn(0, 2);
        t.printGameState();

        t.playTurn(2, 0);
        t.printGameState();

        t.playTurn(1, 0);
        t.printGameState();

        t.playTurn(1, 2);
        t.printGameState();

        t.playTurn(0, 1);
        t.printGameState();

        t.playTurn(2, 2);
        t.printGameState();

        t.playTurn(2, 1);
        t.printGameState();
        System.out.println();
        System.out.println();
        System.out.println("Winner is: " + t.checkWinner());
    }
}
