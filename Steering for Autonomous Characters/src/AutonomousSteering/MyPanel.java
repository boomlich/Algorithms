package AutonomousSteering;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.LinkedList;
import javax.swing.*;

public class MyPanel extends JPanel implements ActionListener{


    Timer timer;


//    AI ai = new AI(Constants.SPAWN_X, Constants.SPAWN_Y);
//    AI ai1 = new AI(500, 700);
    AI[] listAI = new AI[Constants.AI_COUNT];

    Path2D.Double followPath;
    LinkedList<double[]> points = new LinkedList<>();




    MyPanel(){
        this.setPreferredSize(new Dimension(Constants.PANEL_WIDTH,Constants.PANEL_HEIGHT));
        this.setBackground(Color.black);
        timer = new Timer(Constants.REFRESH_RATE, this);
        timer.start();


        for (int i = 0; i < Constants.AI_COUNT; i++) {
            listAI[i] = new AI(0, 0);
        }

        Track track = new Track();
        followPath = Track.getTrackPath();
        points = Track.getTrackCoordinates();

    }


    public static void drawCustomCircle(Ellipse2D.Double shape, Color color, Graphics2D g2D) {
        AffineTransform reset = g2D.getTransform();
        g2D.transform(AffineTransform.getTranslateInstance(-shape.getWidth()/2, -shape.getWidth()/2));
        g2D.setColor(color);
        g2D.fill(shape);
        g2D.setTransform(reset);
    }

    public void paint(Graphics g) {

        super.paint(g);
        Graphics2D g2D = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setRenderingHints(rh);

        // Draw path
        g2D.setColor(new Color(40, 40, 40));
        g2D.setStroke(new BasicStroke(Constants.TRACK_RADIUS * 2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2D.draw(followPath);

        g2D.setStroke(new BasicStroke(2));
        g2D.setColor(Color.WHITE);
        g2D.draw(followPath);


        // Draw AI
        for (AI x: listAI) {
            drawCustomCircle(x.body, Color.orange, g2D);
            if (Constants.DEBUG) {
                drawCustomCircle(x.futurePos, Color.green, g2D);
                drawCustomCircle(x.onLine, Color.MAGENTA, g2D);
                drawCustomCircle(x.target, Color.red, g2D);
            }
        }

        g2D.dispose();
    }



    @Override
    public void actionPerformed(ActionEvent e) {

        // Update all AI
        for (AI x: listAI) {
            x.update();
        }

        repaint();
    }
}