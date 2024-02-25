import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;
import java.util.Random;

public class MazeGUI extends JFrame {
    public static int playerX = 5;
    public static int playerY = 29;
    // constants
    public static final int OBJECTDIM = 40;
    public static final int goalX = 750;
    public static final int goalY = 789;
    // constants
    public static final int SCREENWIDTH = 800;
    public static final int SCREENHEIGHT = 829;
    MazeGUIPanel panel;
    public MazeGUI() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(SCREENWIDTH, SCREENHEIGHT);
        panel = new MazeGUIPanel(); // create the panel
        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.addKeyListener(panel);
        this.setVisible(true);
    }
    public static class MazeGUIPanel extends JPanel implements KeyListener, MouseListener, ActionListener {
        private Timer timer;
        private int minLoc = 10;
        private int maxLoc = 750;
        private int minVel = 2;
        private int maxVel = 7;
        private int[] xvals;
        private int[] yvals;
        private int[] xvelocities;
        private int[] yvelocities;
        private int x = 10;
        private int y = 10;
        private int xvel = 3;
        private int yvel = 3;
        private Random r = new Random();
        private static int numClicks = 0; // keeps track of ammo during mini-game stage
        // assets
        private Image astro;
        private Image door;
        private Image alien;
        public MazeGUIPanel() {
            this.setPreferredSize(new Dimension(SCREENWIDTH, SCREENHEIGHT));
            this.setBackground(generateColor());
            addMouseListener(this);
            // set the frame rate for the mini-game loop
            timer = new Timer(10, this);
            timer.start();
            // create and populate arrays of random positions and velocities for the spheres
            xvals = new int[100];
            yvals = new int[100];
            xvelocities = new int[100];
            yvelocities = new int[100];
            populateVals();
            // create the sprites
            astro = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images/astro.png"))).getImage();
            door = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images/door.png"))).getImage();
            alien = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images/alien.png"))).getImage();
        }
        /** Random functions that generate the spheres */
        public int generateVals(int low, int high) {
            return r.nextInt(low, high);
        }
        public void populateVals() {
            for (int i = 0; i < xvals.length; i++) {
                xvals[i] = generateVals(minLoc, maxLoc);
                yvals[i] = generateVals(minLoc, maxLoc);
                xvelocities[i] = generateVals(minVel, maxVel);
                yvelocities[i] = generateVals(minVel, maxVel);
            }
        }
        public Color generateColor() {
            int red = r.nextInt(0, 255);
            int green = r.nextInt(0, 255);
            int blue = r.nextInt(0, 255);
            return new Color(red, green, blue);
        }
        /** Take in details from the Main class and render to the Swing Panel */
        public void paint(Graphics g) {
            super.paint(g);
            Graphics2D g2 = (Graphics2D) g;
            renderGraphics(g2);
        }
        public void renderGraphics(Graphics2D g2) {
            switch (Main.currentLevel) {
                case 0:
                    // MAZE STAGE
                    // render player
                    g2.drawImage(astro, playerX, playerY, null);
                    g2.drawImage(door, goalX, goalY, null);
                    // render maze
                    g2.setColor(Color.BLACK);
                    int xPos = 0;
                    int yPos = 29;
                    // screen dims:
                    // * 0-800
                    // * 29-839
                    for (int i = 0; i < Maze.MAZEDIM; i++) {
                        for (int j = 0; j < Maze.MAZEDIM; j++) {
                            if (Maze.mazeArr[i][j].equals("X")) {
                                g2.fillRect(xPos, yPos, OBJECTDIM, OBJECTDIM);
                            }
                            xPos += 40;
                        }
                        xPos = 0;
                        yPos += 40;
                    }
                    break;
                case 1:
                    // MINI-GAME STAGE
                    this.removeAll();
                    revalidate();
                    g2.drawImage(alien, x, y, null);
                    for (int i = 0; i < 50; i++) {
                        g2.setColor(generateColor());
                        g2.fillOval(xvals[i], yvals[i], 50, 50);
                    }
                    break;
                case 2:
                    // GAME OVER
                    this.removeAll();
                    revalidate();
                    g2.setColor(generateColor());
                    g2.drawString("CONGRATULATIONS!", 20, 20);
                    g2.drawImage(astro, 300, 350, null);
                    for (int i = 0; i < 100; i++) {
                        g2.setColor(generateColor());
                        g2.fillOval(xvals[i], yvals[i], generateVals(5, 50), generateVals(5, 50));
                    }
                    break;
            }
        }
        /** returns the direction in a String given a keycode */
        private String getDirection(int code) {
            return switch (code) {
                case 68, 39 -> "RIGHT";
                case 37, 65 -> "LEFT";
                case 87, 38 -> "UP";
                case 83, 40 -> "DOWN";
                default -> null;
            };
        }
        /** The main movement handler for the Maze portion of the game */
        @Override
        public void keyReleased(KeyEvent e) {
            if (Main.currentLevel == 0) {
                // update player position based on input
                switch (getDirection(e.getKeyCode())) {
                    case "RIGHT":
                        Maze.handleMovement("RIGHT");
                        repaint();
                        break;
                    case "LEFT":
                        Maze.handleMovement("LEFT");
                        repaint();
                        break;
                    case "UP":
                        Maze.handleMovement("UP");
                        repaint();
                        break;
                    case "DOWN":
                        Maze.handleMovement("DOWN");
                        repaint();
                        break;
                    default:
                        break;
                }
            }
        }
        /** Mouse handler that enables shooting or losing when fighting the alien */
        @Override
        public void mousePressed(MouseEvent e) {
            // if the alien is clicked, go back to the maze where you left off
            if (e.getX() > x && e.getX() < x + 50 && e.getY() > y && e.getY() < y + 50) {
                Main.currentLevel = 0;
                numClicks = 0;
            }
            // if in the mini-game, and you run out of ammo, generate a new maze and start over
            // note that the stage number does not decrease
            if (Main.currentLevel == 1) {
                numClicks++;
                if (numClicks >= 3) {
                    Main.currentLevel = 0;
                    Maze maze = new Maze();
                    numClicks = 0;
                }
            }
        }
        /** The mini-game game loop that runs every 10 milliseconds */
        @Override
        public void actionPerformed(ActionEvent e) {
            for (int i = 0; i < 50; i++) {
                if (xvals[i] > SCREENWIDTH - 50 || xvals[i] <= 0) {
                    xvelocities[i] = -xvelocities[i];
                }
                if (yvals[i] > SCREENHEIGHT - 50 || yvals[i] <= 0) {
                    yvelocities[i] = -yvelocities[i];
                }
                xvals[i] = xvals[i] + xvelocities[i];
                yvals[i] = yvals[i] + yvelocities[i];
            }
            if (x > SCREENWIDTH - 50 || x <= 0) {
                xvel = -xvel;
            }
            if (y > SCREENHEIGHT - 50 || y <= 0) {
                yvel = -yvel;
            }
            x = x + xvel;
            y = y + yvel;
            repaint();
        }
        /** Unused inherited methods */
        @Override
        public void keyTyped(KeyEvent e) {}
        @Override
        public void keyPressed(KeyEvent e) {}
        @Override
        public void mouseClicked(MouseEvent e) {}
        @Override
        public void mouseReleased(MouseEvent e) {}
        @Override
        public void mouseEntered(MouseEvent e) {}
        @Override
        public void mouseExited(MouseEvent e) {}
    }
}
