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
    private static String[][] mazeArr;
    private static int playerPos;
    private int endPos;
    private Set<Integer> usedCoordinates;
    private int EMPTYSPACES;
    public static int numSteps;
    public static boolean runningMaze;

    // constants
    public static final int MAZEDIM = 20;
    private final int MINLENGTH = 2;
    private final int MAXLENGTH = 10;

    public Maze() {
        runningMaze = true;
        mazeArr = new String[MAZEDIM][MAZEDIM];
        for (int i = 0; i < MAZEDIM; i++) {
            for (int j = 0; j < MAZEDIM; j++) {
                mazeArr[i][j] = "-";
            }
        }
        numSteps = 0;
        playerPos = 0; // player starts in top left
        endPos = MAZEDIM * MAZEDIM - 1; // end is at bottom right
        usedCoordinates = new HashSet<>();
        // add player and end blocks and their buffers
        usedCoordinates.add(0);
        usedCoordinates.add(1);
        usedCoordinates.add(MAZEDIM);
        usedCoordinates.add((MAZEDIM * MAZEDIM) - 1);
        EMPTYSPACES = (MAZEDIM * MAZEDIM) - 2;

        // create a new level and stores player position
        createNewLevel();
        drawLevel(); // TODO: comment out for final
    }

    /** converts from grid coordinates to digits
     * 0, 0 --> 0 */
    private int coordsToDigits(ArrayList<Integer> coords) {
        int row = coords.getFirst();
        int col = coords.getLast();
        return (row * MAZEDIM) + col;
    }

    /** converts from digits to grid coordinates
     * 0 --> 0, 0 */
    private static int[] digitsToCoords(int digit) {
        // digit = row * MAZEDIM + col
        // for a grid of 5x5:
        // 23 % 5 = 3 --> col
        // 23 / 5 = 4; --> row
        int col = digit % MAZEDIM;
        int row = digit / MAZEDIM;
        return new int[]{row, col};
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

        // maze algorithm (general pseudocode)
        // - store a set of coordinates that a line cannot intersect
        // - generate a line length
        // - generate a line direction
        // - generate a starting point
        //      - iterative, re-run if generated point or projected line is already is in set of coordinates
        //      - on base case, update the set of coordinates (any point at/adjacent to line, except in the direction)
        // - create the line
        //      - depending on the direction, update the array and the set of coordinates
        // - repeat until empty spaces < some threshold

        // draw a new line on each iteration until the maze is sufficiently full
        while (EMPTYSPACES > 0) {
            int lineLength = generateLineLength();
            String lineDirection = generateLineDirection();
            ArrayList<Integer> lineStart = generateLineStart(lineLength, lineDirection);
            if (lineStart == null) {
                break;
            }
            // update 2D array with start point and add to usedCoordinates
            int startCol = lineStart.getLast(); // col value of starting position
            int startRow = lineStart.getFirst(); // row value of starting position
            mazeArr[startRow][startCol] = "X";
            // determine which way to draw the line
            int xDirection = getDirectionX(lineDirection); // how much to change X position after each draw to the 2D array
            int yDirection = getDirectionY(lineDirection); // how much to change Y position after each draw to the 2D array
            // add to used coordinates, as well as the buffer
            usedCoordinates.add(coordsToDigits(lineStart));
            // Adding a pre-buffer
            if (yDirection == 0) { // if iterating to the left or right
                // flipped checkAndAdd since adding onto the last node
                checkAndAdd(false, true, startRow, startCol);
            } else if (xDirection == 0) { // if iterating to the top or bottom
                checkAndAdd(true, true, startRow, startCol);
            }
            EMPTYSPACES--;
            // initialize at the start of the line
            int prevCol = startCol;
            int prevRow = startRow;
            int lineCounter = 1;
            // for the rest of the line
            while (lineCounter < lineLength) {
                int currentRow = prevRow + yDirection;
                int currentCol = prevCol + xDirection;
                mazeArr[currentRow][currentCol] = "X"; // draw a wall object
                prevCol = currentCol; // update X position
                prevRow = currentRow; // update Y position
                // update the used coordinates
                // also include all surrounding coordinates except in direction of iteration
                ArrayList<Integer> currentCoordinates = new ArrayList<>(List.of(currentRow, currentCol));
                usedCoordinates.add(coordsToDigits(currentCoordinates));
                EMPTYSPACES--;
                // add the buffer surrounding the current coordinates to ensure a clear path
                if (yDirection == 0) { // if iterating to the left or right
                    // add elements above and below
                    checkAndAdd(true, false, currentRow, currentCol);
                } else if (xDirection == 0) { // if iterating to the top or bottom
                    // add elements to the right and left
                    checkAndAdd(false, false, currentRow, currentCol);
                }
                lineCounter++; // move to the next part of the line
                // adding a post-buffer
                if (lineCounter == lineLength) {
                    if (yDirection == 0) { // if iterating to the left or right
                        // flipped checkAndAdd since adding onto the last node
                        checkAndAdd(false, false, currentRow, currentCol);
                    } else if (xDirection == 0) { // if iterating to the top or bottom
                        checkAndAdd(true, false, currentRow, currentCol);
                    }
                }
            }
        }
    }

    private void checkAndAdd(boolean inXDirection, boolean flipped, int currentRow, int currentCol) {
        int inverter = 1;
        if (flipped) {
            inverter = -1;
        }
        if (inXDirection) {
            if (currentRow - 1 >= 0) {
                usedCoordinates.add(coordsToDigits(new ArrayList<>(List.of(currentRow - inverter, currentCol))));
            }
            if (currentRow + 1 < MAZEDIM) {
                usedCoordinates.add(coordsToDigits(new ArrayList<>(List.of(currentRow + inverter, currentCol))));
            }
        } else {
            if (currentCol - 1 >= 0) {
                usedCoordinates.add(coordsToDigits(new ArrayList<>(List.of(currentRow, currentCol - inverter))));
            }
            if (currentCol + 1 < MAZEDIM) {
                usedCoordinates.add(coordsToDigits(new ArrayList<>(List.of(currentRow, currentCol + inverter))));
            }
        }
    }

    private int getDirectionX(String lineDirection) {
        return switch (lineDirection) {
            case "left" -> -1;
            case "right" -> 1;
            default -> 0;
        };
    }

    private int getDirectionY(String lineDirection) {
        return switch (lineDirection) {
            case "up" -> -1;
            case "down" -> 1;
            default -> 0;
        };
    }

    private int generateLineLength() {
        Random r = new Random();
        return r.nextInt(MINLENGTH, MAXLENGTH + 1);
    }
    private String generateLineDirection() {
        Random r = new Random();
        String[] directionArray = new String[]{"up", "down", "left", "right"};
        int index = r.nextInt(0, directionArray.length);
        return directionArray[index];
    }

    private ArrayList<Integer> generateLineStart(int lineLength, String direction) {
        // determine which way to iterate and check for walls
        int xDirection = getDirectionX(direction);
        int yDirection = getDirectionY(direction);
        // generate a start position, break after a maximum of MAZEDIM x MAZEDIM failures
        int count = 0;
        while (count < (MAZEDIM * MAZEDIM)) {
            // generate a random start
            Random r = new Random();
            int randomRow = r.nextInt(0, MAZEDIM);
            int randomCol = r.nextInt(0, MAZEDIM);
            // check if the line intersects with a wall
            // if it doesn't return the list of coordinates
            ArrayList<Integer> startPos = new ArrayList<>();
            startPos.addLast(randomRow);
            startPos.addLast(randomCol);
            // check if used coordinates already contains this start position, as well as surrounding blocks
            if (usedCoordinates.contains(coordsToDigits(startPos))
                    || usedCoordinates.contains(coordsToDigits(startPos) + 1)
                    || usedCoordinates.contains(coordsToDigits(startPos) - 1)
                    || usedCoordinates.contains(coordsToDigits(startPos) + MAZEDIM)
                    || usedCoordinates.contains(coordsToDigits(startPos) - MAZEDIM)) {
                continue;
            }
            int prevCol = randomCol;
            int prevRow = randomRow;
            int lineCounter = 1;
            while (lineCounter < lineLength) {
                int currentRow = prevRow + yDirection;
                int currentCol = prevCol + xDirection;
                // if a wall exists at this point or out of bounds
                ArrayList<Integer> currentCoords = new ArrayList<>(List.of(currentRow, currentCol));
                if (currentRow >= MAZEDIM || currentRow < 0
                        || currentCol >= MAZEDIM || currentCol < 0
                    || usedCoordinates.contains(coordsToDigits(currentCoords))) {
                    break;
                }
                prevCol = currentCol; // update X position
                prevRow = currentRow; // update Y position
                lineCounter++; // move to the next part of the line
                if (lineCounter == lineLength) {
                    return startPos;
                }
            }
            count++;
        }
        return null; // if no potential start point exists
    }
    public void updateCurrentLevel() {
        if (mazeArr[MAZEDIM -1][MAZEDIM -1].equals("P")) {
            playerPos = 0;
            Maze newMaze = new Maze();
            Main.levelCounter++;
        }
    }

    /** prints out the 2D array as a CLI */
    public static void drawLevel() {
        for (int i = 0; i < MAZEDIM; i++) {
            for (int j = 0; j < MAZEDIM; j++) {
                System.out.print("|" + mazeArr[i][j]);
                if (j == MAZEDIM - 1) {
                    System.out.println("|");
                }
            }
        }
    }

    /** move player in the maze based on key input, feeds into MazeGUI handlers */
    public static void handleMovement(String direction) {
        int step = 0;
        switch (direction) {
            case "UP":
                // if not in the top row and not blocked by a wall
                if (playerPos - MAZEDIM >= 0 && !isBlocked("UP")) {
                    step -= MAZEDIM;
                    numSteps++;
                }
                break;
            case "DOWN":
                // if not in the bottom row and not blocked by a wall
                if (playerPos + MAZEDIM < (MAZEDIM * MAZEDIM) && !isBlocked("DOWN")) {
                    step += MAZEDIM;
                    numSteps++;
                }
                break;
            case "RIGHT":
                // in the case of 5x5: (4 + 1) % 5 == 0
                if ((playerPos + 1) % MAZEDIM != 0 && !isBlocked("RIGHT")) { // if not in right column
                    step += 1;
                    numSteps++;
                }
                break;
            case "LEFT":
                // in the case of 5x5: 5 / 5 = 1
                if (playerPos % MAZEDIM != 0 && !isBlocked("LEFT")) { // if not in left column
                    step -= 1;
                    numSteps++;
                }
                break;
            default:
                break;
        }
        int[] playerPosition = digitsToCoords(playerPos);
        mazeArr[playerPosition[0]][playerPosition[1]] = "-"; // erase old player marker
        playerPos = playerPos + step; // update player position
        playerPosition = digitsToCoords(playerPos); // get new position
        mazeArr[playerPosition[0]][playerPosition[1]] = "P"; // draw new player marker
        drawLevel();
    }

    private static boolean isBlocked(String direction) {
        int step = 0;
        switch (direction) {
            case "UP":
                step -= MAZEDIM;
                break;
            case "DOWN":
                step += MAZEDIM;
                break;
            case "RIGHT":
                step += 1;
                break;
            case "LEFT":
                step -= 1;
                break;
            default:
                break;
        }
        int[] coordsToCheck = digitsToCoords(playerPos + step);
        int row = coordsToCheck[0];
        int col = coordsToCheck[1];
        return mazeArr[row][col].equals("X");
    }

    /** TESTING */

    /** Testing maze generation */
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
