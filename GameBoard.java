package org.cis1200.battleship;

/**
 * CIS 120 HW09 - Battleship Project
 * (c) University of Pennsylvania
 * Created by Alicia Sun.
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;


/**
 * This class instantiates a Battleship object, which is the model for the game.
 * As the user clicks the game board, the model is updated. Whenever the model
 * is updated, the game board repaints itself and updates its status JLabel to
 * reflect the current state of the model.
 * 
 * This game adheres to a Model-View-Controller design framework. This
 * framework is very effective for turn-based games. We STRONGLY
 * recommend you review these lecture slides, starting at slide 8,
 * for more details on Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec37.pdf
 * 
 * In a Model-View-Controller framework, GameBoard stores the model as a field
 * and acts as both the controller (with a MouseListener) and the view (with
 * its paintComponent method and the status JLabel).
 */
@SuppressWarnings("serial")
public class GameBoard extends JPanel {

    private Battleship bs; // model for the game
    private JLabel status; // current status text

    // Game constants
    public static final int BOARD_WIDTH = 200 + 15 + 410;
    public static final int BOARD_HEIGHT = 425 + 15;

    /**
     * Initializes the game board.
     */
    public GameBoard(JLabel statusInit) throws IOException {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        bs = new Battleship(); // initializes model for the game
        status = statusInit; // initializes the status JLabel

        /*
         * Listens for mouseclicks. Updates the model, then updates the game
         * board based off of the updated model.
         */
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Point p = e.getPoint();

                // updates the model given the coordinates of the mouseclick
                int x = p.x - 15;
                int y = p.y - 15;
                if ((x >= 0) && (y >= 0) && (bs.getCurrentPlayer())) {
                    bs.playTurn(x / 25, y / 25);

                    updateStatus(); // updates the status JLabel
                    repaint(); // repaints the game board
                }

                if (!bs.getCurrentPlayer()) {
                    //computer turn
                    Random random = new Random();
                    int a = random.nextInt(8);
                    int b = random.nextInt(8);
                    while ((bs.getmyboard()[a][b].equals("M")) ||
                            (bs.getmyboard()[a][b].equals("X"))) {
                        a = random.nextInt(8);
                        b = random.nextInt(8);
                    }
                    bs.playTurn(b, a);

                    updateStatus(); // updates the status JLabel
                    repaint(); // repaints the game board
                }

            }
        });


    }

    /**
     * (Re-)sets the game to its initial state.
     */
    public void reset() throws IOException {
        bs.reset();
        status.setText("Click a square to shoot!");
        repaint();
        updateStatus();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    /**
     * Resumes the previously wiped game state
     */
    public void resume() throws IOException {
        bs.resume();
        status.setText("Click a square to shoot!");
        repaint();
        updateStatus();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    /**
     * Undoes the last round in the game state
     */
    public void undo() {
        bs.undo();
        status.setText("Click a square to shoot!");
        repaint();
        updateStatus();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    private void updateStatus() {
        int winner = bs.checkWinner();
        if (winner == 1) {
            status.setText("You are a winner!!");
        } else if (winner == 2) {
            status.setText("You are a loser!!");
        } else if (winner == 3) {
            status.setText("It's a tie.");
        }
    }

    /**
     * Draws the game board.
     *
     * There are many ways to draw a game board. This approach
     * will not be sufficient for most games, because it is not
     * modular. All of the logic for drawing the game board is
     * in this method, and it does not take advantage of helper
     * methods. Consider breaking up your paintComponent logic
     * into multiple methods or classes, like Mushroom of Doom.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawLine(15, 0, 15, 215);
        g.drawLine(15, 240, 15, 440);
        g.drawLine(0, 15, 215, 15);

        // Draws board grid1
        g.drawLine(40, 0, 40, 215);
        g.drawLine(65, 0, 65, 215);
        g.drawLine(90, 0, 90, 215);
        g.drawLine(115, 0, 115, 215);
        g.drawLine(140, 0, 140, 215);
        g.drawLine(165, 0, 165, 215);
        g.drawLine(190, 0, 190, 215);
        g.drawLine(215, 0, 215, 215);

        g.drawLine(0, 40, 215, 40);
        g.drawLine(0, 65, 215, 65);
        g.drawLine(0, 90, 215, 90);
        g.drawLine(0, 115, 215, 115);
        g.drawLine(0, 140, 215, 140);
        g.drawLine(0, 165, 215, 165);
        g.drawLine(0, 190, 215, 190);

        g.drawLine(0, 215, 215, 215);

        // Draws board grid2
        g.drawLine(40, 240, 40, 440);
        g.drawLine(65, 240, 65, 440);
        g.drawLine(90, 240, 90, 440);
        g.drawLine(115, 240, 115, 440);
        g.drawLine(140, 240, 140, 440);
        g.drawLine(165, 240, 165, 440);
        g.drawLine(190, 240, 190, 440);
        g.drawLine(215, 240, 215, 440);

        g.drawLine(0, 240, 215, 240);
        g.drawLine(0, 265, 215, 265);
        g.drawLine(0, 290, 215, 290);
        g.drawLine(0, 315, 215, 315);
        g.drawLine(0, 340, 215, 340);
        g.drawLine(0, 365, 215, 365);
        g.drawLine(0, 390, 215, 390);
        g.drawLine(0, 415, 215, 415);

        g.drawString("1",25,14);
        g.drawString("2",50,14);
        g.drawString("3",75,14);
        g.drawString("4",100,14);
        g.drawString("5",125,14);
        g.drawString("6",150,14);
        g.drawString("7",175,14);
        g.drawString("8",200,14);

        g.drawString("A", 4,31);
        g.drawString("B", 4,56);
        g.drawString("C", 4,81);
        g.drawString("D", 4,106);
        g.drawString("E", 4,131);
        g.drawString("F", 4,156);
        g.drawString("G", 4,181);
        g.drawString("H", 4,206);


        g.drawString("A", 4,256);
        g.drawString("B", 4,281);
        g.drawString("C", 4,306);
        g.drawString("D", 4,331);
        g.drawString("E", 4,356);
        g.drawString("F", 4,381);
        g.drawString("G", 4,406);
        g.drawString("H", 4,431);

        //Instructions
        g.drawString("INSTRUCTIONS", 225, 35);
        g.drawString("You and the computer both have 5 ships of length 1-5", 225, 50);
        g.drawString("Ships have been randomly placed for both of you", 225, 60);
        g.drawString("Ships cannot be directly adjacent to each other by 1 square", 225, 70);
        g.drawString("The top grid represents the grid the player shoots at", 225, 80);
        g.drawString("The bottom grid represents the players field of ships the computer " +
                "shoots at", 225, 90);
        g.drawString("Each round, both the player and computer will shoot one shot", 225, 100);
        g.drawString("The computer will automatically make a shot when the player selects one",
                225, 110);
        g.drawString("To shoot, click a square that hasn't been shot at on the top grid",
                225, 120);
        g.drawString("When the player sinks a ship, they will be notified which ship sunk",
                225, 130);
        g.drawString("A winner is declared once all of the opponent's ships have been sunk",
                225, 140);

        g.drawString("You may undo a move with the Undo button", 225, 170);
        g.drawString("Once the game is over, Undo is no longer available", 225, 180);
        g.drawString("You may start a new game by clicking the New Game button at the top",
                225, 200);
        g.drawString("This also saves the game that was just wiped", 225, 210);
        g.drawString("The previously wiped game can be continued with the Resume Prev button",
                225, 230);

        BufferedImage img = null;
        try {
            if (img == null) {
                img = ImageIO.read(new File("files/shipimage.png"));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
        Image newImage = img.getScaledInstance(300, 300, Image.SCALE_DEFAULT);
        g.drawImage(newImage,270, 225, null);

        // Draws the boards
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                //draw shootboard
                String shootstate = bs.getShootCell(j, i);
                if (shootstate.equals("X")) {
                    g.drawLine(15 + 25 * j, 15 + 25 * i, 40 + 25 * j, 40 + 25 * i);
                    g.drawLine(15 + 25 * j, 40 + 25 * i, 40 + 25 * j, 15 + 25 * i);
                } else if (shootstate.equals("M")) {
                    g.drawOval(15 + 25 * j, 15 + 25 * i, 25, 25);
                }

                //draw myboard
                String mystate = bs.getMyCell(j, i);
                if (mystate.equals("1") || mystate.equals("2") || mystate.equals("3") ||
                        mystate.equals("4") || mystate.equals("5")) {
                    g.drawString(mystate, 25 + 25 * j, 260 + 25 * i);
                } else if (mystate.equals("M")) {
                    g.drawOval(15 + 25 * j, 15 + 225 + 25 * i, 25, 25);
                } else if (mystate.equals("X")) {
                    g.drawLine(15 + 25 * j, 15 + 225 + 25 * i, 40 + 25 * j, 265 + 25 * i);
                    g.drawLine(15 + 25 * j, 40 + 225 + 25 * i, 40 + 25 * j, 240 + 25 * i);
                }
            }
        }
    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}
