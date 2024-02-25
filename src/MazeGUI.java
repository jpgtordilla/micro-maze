import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class MazeGUI extends JFrame {
    public static int playerX = 0;
    public static int playerY = 29;
    public static final int OBJECTDIM = 40;
    public static final int goalX = 760;
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
        private int maxVel = 5;
        private int[] xvals;
        private int[] yvals;
        private int[] xvelocities;
        private int[] yvelocities;
        private int x = 10;
        private int y = 10;
        private int xvel = 3;
        private int yvel = 3;
        private Random r = new Random();
        private int numClicks = 0;
        public MazeGUIPanel() {
            this.setPreferredSize(new Dimension(SCREENWIDTH, SCREENHEIGHT));
            this.setBackground(generateColor());
            addMouseListener(this);
            timer = new Timer(10, this);
            timer.start();

            xvals = new int[50];
            yvals = new int[50];
            xvelocities = new int[50];
            yvelocities = new int[50];
            populateVals();
        }

        public int generateVals(int low, int high) {
            return r.nextInt(low, high);
        }
        public void populateVals() {
            for (int i = 0; i < 50; i++) {
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
        /** take in details from Maze class and render to the Swing Panel */
        public void paint(Graphics g) {
            super.paint(g);
            Graphics2D g2 = (Graphics2D) g;
            renderArray(g2);
        }
        public void renderArray(Graphics2D g2) {
            if (!Main.miniRunning) {
                // render player
                g2.setColor(Color.blue);
                g2.fillRect(playerX, playerY, OBJECTDIM, OBJECTDIM);
                g2.setColor(Color.white);
                g2.fillRect(playerX + OBJECTDIM / 4, playerY + OBJECTDIM / 8, OBJECTDIM / 2, OBJECTDIM / 2);
                // render goal
                g2.setColor(Color.ORANGE);
                g2.fillRect(goalX, goalY, OBJECTDIM, OBJECTDIM);
                // render maze
                g2.setColor(Color.BLACK);
                int xPos = 0;
                int yPos = 29;
                for (int i = 0; i < Maze.MAZEDIM; i++) {
                    for (int j = 0; j < Maze.MAZEDIM; j++) {
                        // screen dims:
                        // * 0-800
                        // * 29-839
                        if (Maze.mazeArr[i][j].equals("X")) {
                            g2.fillRect(xPos, yPos, OBJECTDIM, OBJECTDIM);
                        }
                        xPos += 40;
                    }
                    xPos = 0;
                    yPos += 40;
                }
            } else {
                this.removeAll();
                revalidate();
                g2.setColor(Color.blue);
                g2.fillOval(x, y, 50, 50);
                for (int i = 0; i < 50; i++) {
                    g2.setColor(generateColor());
                    g2.fillOval(xvals[i], yvals[i], 50, 50);
                }
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
        @Override
        public void keyTyped(KeyEvent e) {}
        @Override
        public void keyPressed(KeyEvent e) {}
        @Override
        public void keyReleased(KeyEvent e) {
            // System.out.println("Key pressed: " + getDirection(e.getKeyCode()));
            if (Maze.runningMaze) {
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
        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getX() > x && e.getX() < x + 50 && e.getY() > y && e.getY() < y + 50) {
                Main.miniRunning = false;
            }
            numClicks++;
            if (numClicks >= 3) {
                Main.miniRunning = false;

                numClicks = 0;
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
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
    }
}
