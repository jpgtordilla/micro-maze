/** Main class
 * Initialize how many levels and set level counter
 * Loop which contains the level handler
 * Calls an updateCurrentLevel() method in the Maze class which handles player position on Keyup
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static int totalLevels;
    public static int levelCounter;
    public static boolean isGameOver;
    public static boolean running;
    public static Maze maze;
    public static MazeGUI mazeGraphics;
    public static boolean miniRunning = false;

    /** Initialize a Swing window and start the game */
    public Main() {
        startGame();
        mazeGraphics = new MazeGUI(); // GUI instance and input handler
        isGameOver = false;
        running = true;
        totalLevels = 3; // convert next line number to integer
        levelCounter = 0;
    }

    /** Set number of levels and the current level */
    public void startGame() {
        // initialize levels
        maze = new Maze(); // initialize a new maze
    }

    /** Update the maze through the main class and break if closed, continue after game over */
    public void gameLoop() {
        maze.updateCurrentLevel(); // signals Maze class to update the parameters of its maze instance

        // use static variables to mirror MazeGUI static references
        if (Maze.numSteps > generateSteps()) {
            Maze.runningMaze = false;
            miniRunning = true;
            // create a MiniGame instance
            System.out.println("MINI GAME");
            Maze.runningMaze = true;
            Maze.numSteps = 0;
        }

        if (levelCounter >= totalLevels) {
            Maze.runningMaze = false;
            isGameOver = true;
//            System.exit(0); // kill the program
        }
    }

    private int generateSteps() {
        Random r = new Random();
        return r.nextInt(10, 20);
    }

    public static void main(String[] args) {
        // Balls ball = new Balls();

        Main newGame = new Main(); // start a game
        // run the game loop 60 times per second while the application is open
        long interval = 16670000; // 1/60 of a second in nanoseconds
        // initialize current and previous time at 0
        long currentTime = System.nanoTime();
        long previousTime = currentTime;
        long elapsedTime;
        while (running) {
            // constantly get the difference between current and previous time to get an elapsed time
            currentTime = System.nanoTime();
            elapsedTime = currentTime - previousTime;
            if (elapsedTime >= interval) {
                newGame.gameLoop();
                previousTime = currentTime; // reset update time to be equal to current time, and clock starts again
            }
        }
    }
}