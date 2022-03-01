package AutonomousSteering;

import java.awt.geom.Line2D;
import java.util.ArrayList;

public class AIObstacleAvoidance {

    public static void detectObjects(AI ai) {
        updateVision(ai);
        visionObstructed(ai);
    }

    private static void visionObstructed(AI ai) {
        boolean avoidObject = false;
        ArrayList<Double> impactDistance = new ArrayList<>();
        ArrayList<double[]> impactCoord = new ArrayList<>();
        int index = 0;

        for (RayTrace ray: ai.getVisionRays()) {
            if (!Double.isNaN(ray.getImpactPointDistance()) && ray.isObstacleDetected()) {
                avoidObject = true;
                impactDistance.add(ray.getImpactPointDistance());
                impactCoord.add(ray.getImpactPointCoord());
            }
        }

        // Find index of longest distance
        if (avoidObject) {
            for (int i = 0; i < impactDistance.size(); i++) {
                index = impactDistance.get(i) < impactDistance.get(index) ? i : index;
            }
        }

        // Find direction only one time
        if (avoidObject != ai.isAvoidObject()) {
            ai.setAvoidObject(avoidObject);
            if (avoidObject) {
                findDirection(ai, impactCoord.get(index));
            }
        }

        if (avoidObject){
            avoidObstacle(ai, impactCoord.get(index));
        }
    }

    private static void findDirection(AI ai, double[] coord) {
        double[] velocity = ai.getVelocity();
        double aX = ai.body.x;
        double aY = ai.body.y;
        double bX = ai.body.x + velocity[0];
        double bY = ai.body.y + velocity[1];
        double mX = coord[0];
        double mY = coord[1];
        ai.setTurnRight(((bX - aX) * (mY - aY) - (bY - aY) * (mX - aX)) > 0);
    }

    private static void avoidObstacle(AI ai, double[] obstacleCoord) {

        double[] velocity = ai.getVelocity();
        double magnitude = VectorMath.vectorLength(velocity) * 150;
        double magnitudeX = velocity[1] / magnitude;
        double magnitudeY = velocity[0] / magnitude;

        int a = 1;
        if (ai.isTurnRight()) {
            a = -1 * a;
        }

        double targetX = ai.body.x + (-a * magnitudeX);
        double targetY = ai.body.y + (a * magnitudeY);

        double distance = VectorMath.vectorLength(ai.body.x, obstacleCoord[0], ai.body.y, obstacleCoord[1]);
        ai.setForce(VectorMath.forceFalloff(distance, Constants.DETECTION_RANGE, Constants.DETECTION_FALLOFF));
        ai.setSteerTarget(new double[] {targetX, targetY});
    }

    private static void updateVision(AI ai) {
        RayTrace[] rayTraces = ai.getVisionRays();

        // Distribute the vision rods evenly across the AI body
        for (int i = 0; i < rayTraces.length; i++) {
            double position = 1.0 / (rayTraces.length - 1.0) * i;
            moveVisionRod(rayTraces[i], ai.body.x, ai.body.y, ai.getVelocity(), position);
        }
    }

    private static void moveVisionRod(RayTrace rayTrace, double bodyX, double bodyY, double[] velocity, double position) {

        double speed = VectorMath.vectorLength(0, velocity[0], 0, velocity[1]);
        double[] velocityUnitVector = {0, 0};
        if (speed > 0.0) {
            velocityUnitVector = VectorMath.unitVector(velocity);
        }

        double x = bodyX + (velocity[1] * Constants.NPC_SIZE * (1 - 2 * position)) / (2 * speed);
        double y = bodyY + (velocity[0] * Constants.NPC_SIZE * (-1 + 2 * position)) / (2 * speed);

        rayTrace.trace.x1 = x;
        rayTrace.trace.x2 = x + velocityUnitVector[0] * Constants.DETECTION_RANGE;
        rayTrace.trace.y1 = y;
        rayTrace.trace.y2 = y + velocityUnitVector[1] * Constants.DETECTION_RANGE;
    }
}
