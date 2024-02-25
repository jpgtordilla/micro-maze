import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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

    public static class MazeGUIPanel extends JPanel implements KeyListener, ActionListener {
        public MazeGUIPanel() {
            this.setPreferredSize(new Dimension(SCREENWIDTH, SCREENHEIGHT));
            this.setBackground(Color.GREEN);
        }
        /** take in details from Maze class and render to the Swing Panel */
        public void paint(Graphics g) {
            super.paint(g);
            Graphics2D g2 = (Graphics2D) g;
            renderArray(g2);
        }
        public void renderArray(Graphics2D g2) {
            // render player
            g2.setColor(Color.RED);
            g2.fillRect(playerX, playerY, OBJECTDIM, OBJECTDIM);
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
        public void actionPerformed(ActionEvent e) {
            repaint();
        }
    }
}
