/** Main class
 * Initialize how many levels and set level counter
 * Loop which contains the level handler
 * Calls an updateCurrentLevel() method in the Maze class which handles player position on Keyup
 */

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

public class Main {
    public int totalLevels;
    public int levelCounter;
    public static boolean isGameOver;
    public static boolean running;
    public static Maze maze;

    /** Initialize a Swing window and start the game */
    public Main() {
        JFrame gameWindow = new JFrame("Maze Game");
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameWindow.setVisible(true);
        startGame();
        isGameOver = false;
        running = true;
    }

    /** Set number of levels and the current level */
    public void startGame() {
        // input handler for text game
        // initialize levels
        Scanner sc = new Scanner(System.in);
        System.out.println("How many levels?");
        totalLevels = Integer.parseInt(sc.nextLine()); // convert next line number to integer
        System.out.println("You have selected " + totalLevels + " levels.");
        levelCounter = 0;
        maze = new Maze(); // initialize a new maze
    }

    /** Update the maze through the main class and break if closed, continue after game over */
    public static void gameLoop() {
        maze.updateCurrentLevel(); // signals Maze class to update the parameters of its maze instance
        // if the game is over, start a new game
        if (isGameOver) {
            isGameOver = false;
            System.out.println("Starting a new game...");
            Main newGame = new Main(); // initialize a new game
        }
        // set running = false if closed
        if (JFrame.EXIT_ON_CLOSE != 3) {
            running = false;
        }
    }

    public static void main(String[] args) {
        Main newGame = new Main(); // start a game

        // run the game loop 60 times per second while the application is open
        while (running) {
            ActionListener gameLoopAction = new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    gameLoop();
                }
            };
            Timer gameTimer = new Timer(1000 / 60, gameLoopAction);
            gameTimer.setInitialDelay(0);
            gameTimer.start();
        }
    }
}