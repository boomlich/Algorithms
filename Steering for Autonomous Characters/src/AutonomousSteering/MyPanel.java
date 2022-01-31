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
    int predictDistance = 50;

    Ellipse2D.Double npc = new Ellipse2D.Double(startX, startY, 50, 50);
    Rectangle2D.Double target = new Rectangle2D.Double(50, 50, 4, 4);
    Ellipse2D.Double futurePos = new Ellipse2D.Double(0, 0, 5, 5);
    Path2D.Double followPath = new Path2D.Double();
    Ellipse2D.Double onLine = new Ellipse2D.Double(0, 0, 5, 5);
    ArrayList<Integer[]> pathCorners = new ArrayList<>();

    double closestX;
    double closestY;

    double[] velocity = {0, 2};
    double[] acceleration = {0, 0};
    static int maxSpeed = 5;
    static double maxForce = 0.1;
    int refreshRate = 10;
    LinkedList<double[]> points = new LinkedList<>();

    int radius = 50;


    MyPanel(){
        this.setPreferredSize(new Dimension(PANEL_WIDTH,PANEL_HEIGHT));
        this.setBackground(Color.black);
        timer = new Timer(refreshRate, this);
        timer.start();


        pathCorners.add(new Integer[]{100, 100});
        pathCorners.add(new Integer[]{100, 900});
//        pathCorners.add(new Integer[]{500, 500});
        pathCorners.add(new Integer[]{900, 900});
        pathCorners.add(new Integer[]{900, 100});

        // Construct path from points
        followPath.moveTo(pathCorners.get(0)[0], pathCorners.get(0)[1]);
        for (int i = 1; i < pathCorners.size(); i++) {
            followPath.lineTo(pathCorners.get(i)[0], pathCorners.get(i)[1]);
        }
        followPath.closePath();

        // Calculate all points on the follow path
        double totalPathLength = 0;
        for (int i = 0; i < pathCorners.size(); i++) {
            int x1 = pathCorners.get(i)[0];
            int y1 = pathCorners.get(i)[1];
            int x2 = pathCorners.get((i + 1) % pathCorners.size())[0];
            int y2 = pathCorners.get((i + 1) % pathCorners.size())[1];

            double segmentLength = Math.sqrt(Math.pow(x2 - x1 , 2) + Math.pow(y2 - y1, 2));
            totalPathLength += segmentLength;

            for (int j = 0; j < (int) segmentLength; j+=5) {
                points.add(new double[]{x1 + (x2 - x1)/segmentLength * j, y1 + (y2-y1)/segmentLength * j});
            }
        }
        System.out.println("Path total length: " + totalPathLength);
        System.out.println(points.size());

    }


    public static void drawCustomCircle(Ellipse2D.Double shape, Color color, Graphics2D g2D) {
        AffineTransform reset = g2D.getTransform();
        g2D.transform(AffineTransform.getTranslateInstance(-shape.getWidth()/2, -shape.getWidth()/2));
        g2D.setColor(color);
        g2D.fill(shape);
        g2D.draw(shape.getBounds2D());
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

        // Draw target
        g2D.setColor(Color.WHITE);
        g2D.fill(target);

        // Draw path
        g2D.draw(followPath);

        g2D.dispose();
    }

    public void applyForce(double[] force) {
        acceleration = force;
        System.out.println(acceleration[0] + " ::::: " + acceleration[1]);
    }

    public void seek(double targetX, double targetY, Ellipse2D.Double vehicle){
        double desiredDistance = vectorLength(vehicle.x, targetX, vehicle.y, targetY);
        double[] desiredVec = {(targetX - vehicle.x) / desiredDistance * maxSpeed, (targetY - vehicle.y) / desiredDistance * maxSpeed};
        double[] steeringVec = {desiredVec[0] - velocity[0], desiredVec[1] - velocity[1]};

        // Limit steering force
        double steeringDistance = vectorLength(0, steeringVec[0], 0, steeringVec[1]);
        steeringVec[0] = steeringVec[0] / steeringDistance * maxForce;
        steeringVec[1] = steeringVec[1] / steeringDistance * maxForce;
        applyForce(steeringVec);
    }

    public double vectorLength(double x1, double x2, double y1, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1,2));
    }

    public void updateForces() {

        // Apply acceleration
        velocity[0] += acceleration[0];
        velocity[1] += acceleration[1];

        double velocityLength = vectorLength(0, velocity[0], 0, velocity[1]);
        System.out.println(velocityLength);
        if (velocityLength > maxSpeed) {
            System.out.println("adjusted");
            velocity[0] = velocity[0] / velocityLength * maxSpeed;
            velocity[1] = velocity[1] / velocityLength * maxSpeed;
        }

        // Apply velocity
        npc.x += velocity[0];
        npc.y += velocity[1];

        // Reset acceleration
        acceleration[0] = 0;
        acceleration[1] = 0;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Predict future position
        futurePos.x = npc.x + velocity[0] * predictDistance;
        futurePos.y = npc.y + velocity[1] * predictDistance;

        // Target follows mouse coordinates
//        Point p = MouseInfo.getPointerInfo().getLocation();
//        target.x = p.x;
//        target.y = p.y;

        // Find the closest point on the path
        closestX = 0;
        closestY = 0;
        double smallestDistance = 1000000;
        for (double[] i: points) {
            double distance = Math.sqrt(Math.pow(i[0] - futurePos.x, 2) + Math.pow(i[1] - futurePos.y, 2));
            if (distance < smallestDistance) {
                smallestDistance = distance;
                closestX = i[0];
                closestY = i[1];
            }
        }
        onLine.x = closestX;
        onLine.y = closestY;

//        System.out.println("D: " + smallestDistance + " :::: " + "radius: " + radius);
        if (smallestDistance > radius) {
            seek(closestX, closestY, npc);
        }

        updateForces();

        repaint();
    }
}