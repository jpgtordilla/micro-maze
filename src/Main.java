import java.util.Random;
/** Main class
 * Initialize how many levels and set level counter
 * Loop which contains the level handler
 * Calls an updateCurrentLevel() method in the Maze class which handles player position on Keyup
 */
public class Main {
    public static int totalStages;
    public static int stageCounter;
    public static boolean running;
    public static Maze maze;
    public static MazeGUI mazeGraphics;
    public static int currentLevel; // maze = 0, mini-game = 1, game over = 2
    /** Initialize a Swing window and start the game */
    public Main() {
        maze = new Maze(); // initialize a new maze
        mazeGraphics = new MazeGUI(); // GUI instance and input handler
        currentLevel = 0;
        totalStages = 5; // convert next line number to integer
        stageCounter = 0;
        running = true;
    }
    /** Update the maze through the main class and break if closed, continue after game over */
    public void gameLoop() {
        maze.updateCurrentStage(); // signals Maze class to update the parameters of its maze instance
        // use static variables to mirror MazeGUI static references
        if (Maze.numSteps > generateSteps()) {
            currentLevel = 1;
            Maze.numSteps = 0;
        }
        if (stageCounter >= totalStages) {
            currentLevel = 2;
            running = false;
        }
    }
    /** Generates how many steps you can take before running into an alien */
    private int generateSteps() {
        Random r = new Random();
        return r.nextInt(10, 20);
    }
    /** The Main method creates an instance of the game and handles the loop logic with a System timer */
    public static void main(String[] args) {
        Main newGame = new Main();
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