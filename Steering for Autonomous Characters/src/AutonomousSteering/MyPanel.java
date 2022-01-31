package AutonomousSteering;

import java.awt.*;

import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.*;

public class MyPanel extends JPanel implements ActionListener{

    final int PANEL_WIDTH = 1000;
    final int PANEL_HEIGHT = 1000;
    Timer timer;

    int startX = 50;
    int startY = 50;
    int predictDistance = 15;
    int predictPointIndex = 0;
    int targetOffset = 5;

    AI ai = new AI(Constants.SPAWN_X, Constants.SPAWN_Y, Constants.NPC_SIZE);

    Ellipse2D.Double npc = new Ellipse2D.Double(startX, startY, 50, 50);
    Ellipse2D.Double futurePos = new Ellipse2D.Double(0, 0, 5, 5);
    Path2D.Double followPath = new Path2D.Double();
    Ellipse2D.Double onLine = new Ellipse2D.Double(0, 0, 5, 5);
    Ellipse2D.Double target = new Ellipse2D.Double(0, 0, 5, 5);
    ArrayList<Integer[]> pathCorners = new ArrayList<>();

    double[] velocity = Constants.VELOCITY;
    double[] acceleration = Constants.ACCELERATION;
    static int maxSpeed = Constants.MAX_SPEED;
    static double maxForce = Constants.MAX_FORCE;


    LinkedList<double[]> points = new LinkedList<>();

    int radius = 25;


    MyPanel(){
        this.setPreferredSize(new Dimension(PANEL_WIDTH,PANEL_HEIGHT));
        this.setBackground(Color.black);
        timer = new Timer(Constants.REFRESH_RATE, this);
        timer.start();

        Track track = new Track();
        followPath = track.getTrackPath();
        points = track.getTrackCoordinates();
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

        // Draw NPC and target
        drawCustomCircle(npc, Color.orange, g2D);
        drawCustomCircle(futurePos, Color.green, g2D);
        drawCustomCircle(onLine, Color.MAGENTA, g2D);
        drawCustomCircle(target, Color.RED, g2D);

        // Draw path
        g2D.setColor(Color.WHITE);
        g2D.draw(followPath);


        g2D.fill(ai);



        g2D.dispose();
    }

    public void applyForce(double[] force) {
        acceleration = force;
    }

    public void seek(double targetX, double targetY, Ellipse2D.Double vehicle){
        // Calculate desired velocity and limit by max speed
        double[] desiredVector = {targetX - vehicle.x, targetY - vehicle.y};
        VectorMath.limitVector(desiredVector, maxSpeed);

        // Calculate steering force and limit by max force
        double[] steeringVec = {desiredVector[0] - velocity[0], desiredVector[1] - velocity[1]};
        VectorMath.limitVector(steeringVec, maxForce);
        applyForce(steeringVec);
    }

//    public void updateForces() {
//
//        // Apply acceleration
//        velocity[0] += acceleration[0];
//        velocity[1] += acceleration[1];
//
//        double velocityLength = VectorMath.vectorLength(0, velocity[0], 0, velocity[1]);
//        if (velocityLength > maxSpeed) {
//            VectorMath.limitVector(velocity, maxSpeed);
//        }
//
//        // Apply velocity
//        npc.x += velocity[0];
//        npc.y += velocity[1];
//
//        // Reset acceleration
//        acceleration[0] = 0;
//        acceleration[1] = 0;
//    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Predict future position
        futurePos.x = npc.x + velocity[0] * predictDistance;
        futurePos.y = npc.y + velocity[1] * predictDistance;

        // Find the closest point on the path
        double smallestDistance = 1000000;
        int i = 0;
        for (double[] coord: points) {
            double distance = VectorMath.vectorLength(futurePos.x, coord[0], futurePos.y, coord[1]);
            if (distance < smallestDistance) {
                smallestDistance = distance;
                onLine.x = coord[0];
                onLine.y = coord[1];
                predictPointIndex = i;
            }
            i ++;
        }
        double[] targetCoord = points.get((predictPointIndex + targetOffset) % points.size());
        target.x = targetCoord[0];
        target.y = targetCoord[1];

        // Seek or accelerate
        if (smallestDistance > radius) {
//            AIBehavior.seek(npc, target.x, target.y, velocity, acceleration);
            seek(target.x, target.y, npc);
        } else {
            double speed = VectorMath.vectorLength(0, velocity[0], 0, velocity[1]);
            acceleration[0] += velocity[0] / speed * 0.01;
            acceleration[1] += velocity[1] / speed * 0.01;
        }

        AIBehavior.updateForces(npc, velocity, acceleration);

//        updateForces();
        repaint();
    }
}