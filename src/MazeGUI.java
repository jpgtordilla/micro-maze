import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class MazeGUI extends JFrame implements KeyListener {
    public MazeGUI(String title) {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 500);
        this.addKeyListener(this);
        this.setVisible(true);
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

        // update player position based on input
        switch (getDirection(e.getKeyCode())) {
            case "RIGHT":
                Maze.handleMovement("RIGHT");
                break;
            case "LEFT":
                Maze.handleMovement("LEFT");
                break;
            case "UP":
                Maze.handleMovement("UP");
                break;
            case "DOWN":
                Maze.handleMovement("DOWN");
                break;
            default:
                break;
        }
    }
}
