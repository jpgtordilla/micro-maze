import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.*;
public class Balls extends MazeGUI {

    public Balls () {
        super();
        super.add(new BallPanel());
    }
    public static class BallPanel extends JPanel implements ActionListener {
        Timer timer;
        int x = 100;
        int xVel = 10;
        int y = 100;
        int yVel = 10;

        public BallPanel () {
            this.setPreferredSize(new Dimension(MazeGUI.SCREENWIDTH, MazeGUI.SCREENHEIGHT));
            timer = new Timer(10, this);
            timer.start();
        }

        @Override
        public void paint(Graphics g) {
            // generate random circles, then generate one random green circle
            // player has 2 seconds to click the green circle
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.RED);
            g2.fillOval(x, y, 50, 50);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (x >= MazeGUI.SCREENWIDTH - 50 || x < 0) {
                xVel = -xVel;
            }
            if (y >= MazeGUI.SCREENWIDTH - 50 || y < 0) {
                yVel = -yVel;
            }
            x = x + xVel;
            y = y + yVel;
            repaint();
        }
    }
}