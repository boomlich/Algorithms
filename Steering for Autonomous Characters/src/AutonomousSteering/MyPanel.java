package AutonomousSteering;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.LinkedList;
import javax.swing.*;

public class MyPanel extends JPanel implements ActionListener{

    Timer timer;
    AI[] listAI = new AI[Constants.AI_COUNT];
    Path2D.Double followPath;
    LinkedList<double[]> points = new LinkedList<>();

    MyPanel(){
        this.setPreferredSize(new Dimension(Constants.PANEL_WIDTH,Constants.PANEL_HEIGHT));
        this.setBackground(Color.black);
        timer = new Timer(Constants.REFRESH_RATE, this);
        timer.start();

        // Add AI to the track
        for (int i = 0; i < Constants.AI_COUNT; i++) {
            listAI[i] = new AI(0, 0);
        }

        new Track();
        getPath();
    }

    private void getPath() {
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
        if (Constants.SHOW_PATH) {
            g2D.setColor(new Color(40, 40, 40));
            g2D.setStroke(new BasicStroke(Constants.TRACK_RADIUS * 2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2D.draw(followPath);

            g2D.setStroke(new BasicStroke(2));
            g2D.setColor(Color.WHITE);
            g2D.draw(followPath);
        }

        // Draw AI
        for (AI x: listAI) {
            drawCustomCircle(x.body, Color.orange, g2D);
        }

        if (Constants.DEBUG) {
            drawDebug(g2D, listAI);
        }

        g2D.dispose();
    }

    private static void drawDebug(Graphics2D g2D, AI[] listAI) {
        // AI targets
        for (AI x: listAI) {
            drawCustomCircle(x.futurePos, Color.green, g2D);
            drawCustomCircle(x.onLine, Color.MAGENTA, g2D);
            drawCustomCircle(x.target, Color.red, g2D);
        }

        // Track corner-points
        for (Ellipse2D.Double corner: Track.getCornerShapes()) {
            drawCustomCircle(corner, Color.CYAN, g2D);
        }
    }



    @Override
    public void actionPerformed(ActionEvent e) {

        // Update all AI
        for (AI x: listAI) {
            x.update();
        }

        if (MouseInput.isMouseActivated()) {
            Track.update();
            getPath();
        }

        if (Constants.RESET) {
            getPath();
        }

        repaint();
    }
}