import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.*;
public class Balls extends MazeGUI implements ActionListener {
    Timer timer;
    int x = 100;
    int y = 100;

    public Balls () {
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
}