package AutonomousSteering;

import java.awt.*;

import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import javax.swing.*;

public class MyPanel extends JPanel implements ActionListener{

    final int PANEL_WIDTH = 1000;
    final int PANEL_HEIGHT = 1000;
    Timer timer;

    int speed = 5;

    Ellipse2D.Double npc = new Ellipse2D.Double(750, 500, 100, 100);
    Rectangle2D.Double target = new Rectangle2D.Double(50, 50, 4, 4);

    double[] direction = {0, 0};

    Path2D.Double followPath = new Path2D.Double();


    MyPanel(){
        this.setPreferredSize(new Dimension(PANEL_WIDTH,PANEL_HEIGHT));
        this.setBackground(Color.black);
        timer = new Timer(10, this);
        timer.start();
    }

    public void paint(Graphics g) {

        super.paint(g);

        Graphics2D g2D = (Graphics2D) g;

        g2D.setColor(Color.ORANGE);
        g2D.fill(npc);

        g2D.setColor(Color.WHITE);
        g2D.fill(target);


        followPath.moveTo(0, 500);
        followPath.lineTo(1000, 500);
        g2D.draw(followPath);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Calculate a vector pointing towards the target and normalize the length
        double totalDistance = Math.sqrt(Math.pow(npc.x - target.x, 2) + Math.pow(npc.y - target.y, 2));
        double distanceX = target.x - npc.x;
        double distanceY = target.y - npc.y;
        direction[0] =  distanceX / totalDistance;
        direction[1] = distanceY / totalDistance;

        // Apply velocity
        npc.x += direction[0] * speed;
        npc.y += direction[1] * speed;

//        if (target.y < 950) {
//            target.y += 5;
//        } else {
//            target.x += 5;
//        }
//
        Point p = MouseInfo.getPointerInfo().getLocation();
        target.x = p.x;
        target.y = p.y;


        repaint();

    }
}