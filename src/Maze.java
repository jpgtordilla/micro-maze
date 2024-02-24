import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.*;

/** Maze class
 * Generate a 2D maze array parameters of walls, spaces, player, and exit
 * Key presses update player position in O(1)
 * Count number of steps
 * If steps is equal to a number between 10-20, start a mini-game compilation instance (static method from MiniGame class)
 * If the player is at the exit position, regenerate the maze, reset the player, and reset the steps
 */

public class Maze {
    private String[][] mazeArr;
    private int[] playerPos;
    private int[] endPos;

    // constants
    private int MAZEDIM = 20;
    private int MAXLENGTH = 10;
    int EMPTYSPACES = 400;
    int SPACETHRESHOLD = 100;

    public Maze() {
        mazeArr = new String[MAZEDIM][MAZEDIM];
        for (int i = 0; i < MAZEDIM; i++) {
            for (int j = 0; j < MAZEDIM; j++) {
                mazeArr[i][j] = "-";
            }
        }
        playerPos = new int[]{0, 0}; // player starts in top left
        endPos = new int[]{MAZEDIM - 1, MAZEDIM - 1}; // end is at bottom right
    }
    /** generates a maze that can navigated by the player */
    private void createNewLevel() {
        // add player and goal
        mazeArr[0][0] = "P";
        mazeArr[MAZEDIM - 1][MAZEDIM - 1] = "G";

        // naive maze algorithm rules
        // - never have lines touch each other
        // - never generate lines longer than a certain length
        // - never generate a line that intersects with (0, 0) or (19, 19)

        // maze algorithm O(N^2)
        // - store a set of coordinates that a line cannot intersect
        // - generate a line length
        // - generate a line direction
        // - generate a starting point
        //      - recursive method, re-run if generated point or projected line is already is in set of coordinates
        //      - on base case, update the set of coordinates (any point at/adjacent to line, except in the direction)
        // - create the line
        //      - depending on the direction, update the array and the set of coordinates
        // - repeat until empty spaces < some threshold

        Set<List<Integer>> usedCoordinates = new HashSet<>();
        // TODO:
        // - fill in generateLineLength()
        // - fill in generateLineDirection()
        // - fill in

        // draw a new line on each iteration until the maze is sufficiently full
        while (EMPTYSPACES > SPACETHRESHOLD) {
            int lineLength = generateLineLength();
            String lineDirection = generateLineDirection();
            ArrayList<Integer> lineStart = generateLineStart();

            // update 2D array with start point and add to usedCoordinates
            int startCol = lineStart.getLast(); // col value of starting position
            int startRow = lineStart.getFirst(); // row value of starting position
            mazeArr[startRow][startCol] = "X";
            usedCoordinates.add(lineStart);
            EMPTYSPACES--;
            // determine which way to draw the line
            int xDirection; // how much to change X position after each draw to the 2D array
            int yDirection; // how much to change Y position after each draw to the 2D array
            switch (lineDirection) {
                case "up":
                    yDirection = -1;
                    xDirection = 0;
                    break;
                case "down":
                    yDirection = 1;
                    xDirection = 0;
                    break;
                case "left":
                    yDirection = 0;
                    xDirection = -1;
                    break;
                case "right":
                    yDirection = 0;
                    xDirection = 1;
                    break;
                default:
                    yDirection = 0;
                    xDirection = 0;
                    break;
            }
            // initialize at the start of the line
            int prevCol = startCol;
            int prevRow = startRow;
            int lineCounter = 1;
            // for the rest of the line
            while (lineCounter < lineLength) {
                int currentRow = prevRow + yDirection;
                int currentCol = prevCol + xDirection;
                mazeArr[prevRow + yDirection][prevCol + xDirection] = "X"; // draw a wall object
                prevCol = currentCol; // update X position
                prevRow = currentRow; // update Y position
                // update the used coordinates
                ArrayList<Integer> currentCoordinates = new ArrayList<>(List.of(currentRow, currentCol));
                usedCoordinates.add(currentCoordinates);
                EMPTYSPACES--;
                lineCounter++; // move to the next part of the line
            }
        }
    }

    private int generateLineLength() {
        return 5;
    }
    private String generateLineDirection() {
        return "up";
    }

    private ArrayList<Integer> generateLineStart() {
        return new ArrayList<>(List.of(10, 10));
    }
    public void updateCurrentLevel() {
        System.out.println("Updating...");
    }

    /** TESTING */
    @Test
    public void testCreateNewLevelDrawLine() {
        createNewLevel();
        for (int i = 0; i < MAZEDIM; i++) {
            for (int j = 0; j < MAZEDIM; j++) {
                System.out.print("|" + mazeArr[i][j]);
                if (j == MAZEDIM - 1) {
                    System.out.println("|");
                }
            }
        }
    }
}
