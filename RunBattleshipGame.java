package org.cis1200.battleship;

/**
 * CIS 120 HW09 - Battleship Project
 * (c) University of Pennsylvania
 * Created by Alicia Sun.
 */

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * This class sets up the top-level frame and widgets for the GUI.
 * 
 * This game adheres to a Model-View-Controller design framework. This
 * framework is very effective for turn-based games.
 *
 * In a Model-View-Controller framework, Game initializes the view,
 * implements a bit of controller functionality through the reset
 * button, and then instantiates a GameBoard. The GameBoard will
 * handle the rest of the game's view and controller functionality, and
 * it will instantiate a Battleship object to serve as the game's model.
 */
public class RunBattleshipGame implements Runnable {
    public void run() {
        // NOTE: the 'final' keyword denotes immutability even for local variables.

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("Battleship");
        frame.setLocation(300, 300);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Setting up...");
        status_panel.add(status);

        // Game board
        final GameBoard board;
        try {
            board = new GameBoard(status);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        frame.add(board, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Note here that when we add an action listener to the reset, resume, and undo button, we
        // define it as an anonymous inner class that is an instance of
        // ActionListener with its actionPerformed() method overridden. When the
        // button is pressed, actionPerformed() will be called.
        final JButton undo = new JButton("Undo");
        undo.addActionListener(e -> {
            board.undo();
        });
        control_panel.add(undo);

        final JButton reset = new JButton("New Game");
        reset.addActionListener(e -> {
            try {
                board.reset();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        control_panel.add(reset);

        final JButton resume = new JButton("Resume Prev");
        resume.addActionListener(e -> {
            try {
                board.resume();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        control_panel.add(resume);

        final JLabel title = new JLabel("    WELCOME TO BATTLESHIP");
        control_panel.add(title);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start the game
        try {
            board.reset();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}