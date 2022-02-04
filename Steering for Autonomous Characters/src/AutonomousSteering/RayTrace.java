package AutonomousSteering;

import java.awt.geom.*;

public class RayTrace {

    Line2D.Double trace;
    Line2D.Double traceSeek;
    Ellipse2D.Double impactPoint;
    private boolean obstacleDetected;
    private IObstacle obstacleIdentified;
    private double[] impactPointCoord = new double[2];
    private double impactPointDistance;
    IObstacle owner;

    public RayTrace(double x1, double y1, double x2, double y2) {
        trace = new Line2D.Double(x1, y1, x2, y2);
    }

    public void update(IObstacle owner) {
        this.owner = owner;
        update();
    }

    public void update() {

        obstacleDetected = false;
        traceSeek = new Line2D.Double(0,0, 0, 0);
        impactPoint = new Ellipse2D.Double(0, 0, 0, 0);

        // Check intersection with all objects
        for (IObstacle obstacle: MyPanel.getAllObstacles()) {
            if (owner == null || !owner.equals(obstacle)) {
                if (trace.intersects(obstacle.getBoundingBox())) {
                    findIntersectionPoint(obstacle);
                }
            }
        }
    }

    private void findIntersectionPoint(IObstacle obstacle) {
        // Find the directional unit vector of the raytrace
        double[] direction = {trace.x2 - trace.x1, trace.y2 - trace.y1};
        double[] unitVector = VectorMath.unitVector(direction);
        double traceStep = VectorMath.vectorLength(direction) / Constants.RAYTRACE_QUALITY;

        // Try different raytrace lengths until it intersects with the bounding lines of the object
        outerloop:
        for (int i = 0; i < Constants.RAYTRACE_QUALITY+1; i++) {
            for (Line2D.Double line: obstacle.getBodyLines()) {
                double length = traceStep * i;
                double[] stepVector = {unitVector[0] * length, unitVector[1] * length};

                double x2 = trace.getX1() + stepVector[0];
                double y2 = trace.getY1() + stepVector[1];

                traceSeek = new Line2D.Double(trace.getX1(), trace.getY1(), x2, y2);

                if (traceSeek.intersectsLine(line)) {
                    impactPoint = new Ellipse2D.Double(x2, y2, Constants.DEBUG_SIZE, Constants.DEBUG_SIZE);
                    obstacleDetected = true;
                    impactPointCoord = new double[]{x2, y2};
                    obstacleIdentified = obstacle;
                    impactPointDistance = length;
                    break outerloop;
                }
            }
        }
    }

    public double[] getImpactPointCoord() {
        return impactPointCoord;
    }

    public IObstacle getObstacleIdentified() {
        return obstacleIdentified;
    }

    public boolean isObstacleDetected() {
        return obstacleDetected;
    }

    public double getImpactPointDistance() {
        return impactPointDistance;
    }
}
