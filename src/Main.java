/** Main class
 * Initialize how many levels and set level counter
 * Loop which contains the level handler
 * Calls an updateCurrentLevel() method in the Maze class which handles player position on Keyup
 */

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Scanner;

public class Main {
    public static int totalLevels;
    public static int levelCounter;
    public static boolean isGameOver;
    public static boolean running;
    public static Maze maze;

    /** Initialize a Swing window and start the game */
    public Main() {
        MazeGUI mazeGraphics = new MazeGUI("Maze Game"); // GUI instance and input handler
        startGame();
        isGameOver = false;
        running = true;
    }

    /** Set number of levels and the current level */
    public void startGame() {
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
        if (levelCounter >= totalLevels) {
            isGameOver = true;
        }
        // if the game is over or if closed, end the game
        if (isGameOver || JFrame.EXIT_ON_CLOSE != 3) {
            running = false;
        }
    }

    public static void main(String[] args) {
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
                gameLoop();
                previousTime = currentTime; // reset update time to be equal to current time, and clock starts again
            }
        }
    }
}