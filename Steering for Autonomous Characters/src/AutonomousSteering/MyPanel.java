package AutonomousSteering;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.LinkedList;
import javax.swing.*;

public class MyPanel extends JPanel implements ActionListener{

    Timer timer;
    static AI[] listAI = new AI[Constants.AI_COUNT];
    Path2D.Double followPath;
    LinkedList<double[]> points = new LinkedList<>();

    Rectangle2D.Double testRect1 = new Rectangle2D.Double(25, 0, 50, 50);
    double speed = 0.5;

    static Wall myWall;

    private static AI testAI = new AI(500, 100);


    MyPanel(){
        this.setPreferredSize(new Dimension(Constants.PANEL_WIDTH,Constants.PANEL_HEIGHT));
        this.setBackground(Constants.C_BG);
        timer = new Timer(Constants.REFRESH_RATE, this);
        timer.start();

        // Add AI to the track
        for (int i = 0; i < Constants.AI_COUNT; i++) {
            listAI[i] = new AI(Constants.SPAWN_X, Constants.SPAWN_Y);
        }



        myWall = new Wall(500, 50, 50, 100);

        testAI.body.x = 500;
        testAI.body.y = 100;

        new Track();
        getPath();
    }

    private void getPath() {
        followPath = Track.getTrackPath();
        points = Track.getTrackCoordinates();
    }

    public static AI[] getAI() {
        return listAI;
    }

    public static AI getTestAI() {
        return testAI;
    }

//    public static Wall getWall(){
//        return myWall;
//    }

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

        // Draw road
        g2D.setColor(Constants.C_TRACK);
        g2D.setStroke(new BasicStroke(Constants.TRACK_THICKNESS * 2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2D.draw(followPath);

        // Draw path tools
        if (Constants.SHOW_PATH) {
            showPath(followPath, g2D);
        }

        // Draw AI
        for (AI x: listAI) {
            drawCustomCircle(x.body, Color.orange, g2D);
        }

        // Draw debug
        if (Constants.DEBUG) {
            drawDebug(g2D, listAI);
        }

        g2D.setStroke(new BasicStroke(2));
//        g2D.draw(myWall.body);

        drawCustomCircle(testAI.body,Color.orange, g2D);

        g2D.dispose();
    }

    private static void drawDebug(Graphics2D g2D, AI[] listAI) {
        // AI targets
        for (AI x: listAI) {
            g2D.setStroke(new BasicStroke(2));
            g2D.setColor(Color.green);
            g2D.draw(x.desiredVel);

            g2D.setColor(Color.red);
            g2D.draw(x.steeringVel);

            drawCustomCircle(x.futurePos, Color.green, g2D);
            drawCustomCircle(x.onLine, Color.MAGENTA, g2D);
            drawCustomCircle(x.target, Color.red, g2D);

            g2D.setColor(Color.WHITE);
            g2D.draw(x.detection1);
            g2D.draw(x.detection2);
            g2D.draw(x.detection3);

        }
    }

    private static void showPath(Path2D followPath, Graphics2D g2D) {
        g2D.setColor(Constants.C_PATH_RADIUS);
        g2D.setStroke(new BasicStroke(Constants.TRACK_RADIUS * 2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2D.draw(followPath);

        g2D.setStroke(new BasicStroke(2));
        g2D.setColor(Constants.C_PATH);
        g2D.draw(followPath);

        // Track corner-points
        for (Ellipse2D.Double corner: Track.getCornerShapes()) {
            Ellipse2D.Double eggs = new Ellipse2D.Double(corner.getX(), corner.getY(), Constants.CORNER_SIZE+5, Constants.CORNER_SIZE+5);
            drawCustomCircle(eggs, Constants.C_CORNER, g2D);
            drawCustomCircle(corner, Constants.C_EDGE, g2D);
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
            Constants.RESET = !Constants.RESET;
        }

        repaint();
    }
}