package AutonomousSteering;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import javax.swing.*;

public class MyPanel extends JPanel implements ActionListener{

    Timer timer;
    static AI[] listAI = new AI[Constants.AI_COUNT];
    Path2D.Double followPath;
    LinkedList<double[]> points = new LinkedList<>();
    static Wall myWall = new Wall(300, 50, 50, 100);
    static ArrayList<IObstacle> allObstacles = new ArrayList<>();

    MyPanel(){
        this.setPreferredSize(new Dimension(Constants.PANEL_WIDTH,Constants.PANEL_HEIGHT));
        this.setBackground(Constants.C_BG);
        timer = new Timer(Constants.REFRESH_RATE, this);
        timer.start();

        // Add AI to the track
        for (int i = 0; i < Constants.AI_COUNT; i++) {
            listAI[i] = new AI(Constants.SPAWN_X, Constants.SPAWN_Y, i);
        }

        // Mark objects for obstacle avoidance
        allObstacles.add(myWall);
        allObstacles.addAll(Arrays.asList(listAI));

        new Track();
        getPath();

    }

    private void getPath() {
        followPath = Track.getTrackPath();
        points = Track.getTrackCoordinates();
    }


    public static ArrayList<IObstacle> getAllObstacles() {
        return allObstacles;
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
        g2D.draw(myWall.body);

        g2D.dispose();
    }

    private static void drawDebug(Graphics2D g2D, AI[] listAI) {
        for (AI x: listAI) {

            // Desired and steering velocity
            g2D.setStroke(Constants.D_VELOCITY);
            g2D.setColor(Color.green);
            g2D.draw(x.desiredVel);
            g2D.setColor(Color.red);
            g2D.draw(x.steeringVel);

            // Path following targets
            drawCustomCircle(x.futurePos, Color.green, g2D);
            drawCustomCircle(x.onLine, Color.MAGENTA, g2D);
            drawCustomCircle(x.target, Color.red, g2D);

            // Obstacle raytrace

            for (RayTrace ray: x.getVisionRays()) {
                g2D.setStroke(Constants.D_TRACE);
                g2D.setColor(Color.red);
                g2D.draw(ray.trace);
                g2D.setColor(Color.green);
                g2D.draw(ray.traceSeek);
                drawCustomCircle(ray.impactPoint, Color.blue, g2D);
            }
        }
    }

    private static void showPath(Path2D followPath, Graphics2D g2D) {
        g2D.setColor(Constants.C_PATH_RADIUS);
        g2D.setStroke(Constants.TRACK_BRUSH);
        g2D.draw(followPath);

        g2D.setStroke(Constants.TRACK_EDIT);
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