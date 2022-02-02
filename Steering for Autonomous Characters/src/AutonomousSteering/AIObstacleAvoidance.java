package AutonomousSteering;

import java.awt.geom.Rectangle2D;

public class AIObstacleAvoidance {
    public static void detectObjects(AI ai) {
        updateVision(ai);
        visionObstructed(ai);
    }

    private static void visionObstructed(AI ai) {
        AI object = MyPanel.getTestAI();
        double x = object.body.x - object.body.getWidth() / 2;
        double y = object.body.y - object.body.getWidth() / 2;
        double size = object.body.getWidth();
        Rectangle2D.Double object_obstacle = new Rectangle2D.Double(x, y, size, size);

        boolean a = ai.detection1.intersects(object_obstacle);
        boolean b = ai.detection2.intersects(object_obstacle);
        boolean c = ai.detection3.intersects(object_obstacle);
        boolean avoidObject = a || b || c;

        if (avoidObject != ai.isAvoidObject()) {
            ai.setAvoidObject(avoidObject);

            if (avoidObject) {
                findDirection(ai, object_obstacle);
            }
        }

        if (avoidObject){
            avoidObstacle(ai, object_obstacle);
        }
    }

    private static void findDirection(AI ai, Rectangle2D.Double object) {
        double[] velocity = ai.getVelocity();
        double aX = ai.body.x;
        double aY = ai.body.y;
        double bX = ai.body.x + velocity[0];
        double bY = ai.body.y + velocity[1];
        double mX = object.getCenterX();
        double mY = object.getCenterY();
        ai.setTurnRight(((bX - aX) * (mY - aY) - (bY - aY) * (mX - aX)) > 0);
    }

    private static void avoidObstacle(AI ai, Rectangle2D.Double object) {

        double[] velocity = ai.getVelocity();
        double speed = VectorMath.vectorLength(velocity);
        double targetX;
        double targetY;

        if (ai.isTurnRight()) {
            targetX = ai.body.x + (velocity[1] / speed) * 150;
            targetY = ai.body.y + (-velocity[0] / speed) * 150;
        } else {
            targetX = ai.body.x + (-velocity[1] / speed) * 150;
            targetY = ai.body.y + (velocity[0] / speed) * 150;
        }

        double distance = VectorMath.vectorLength(ai.body.x, object.getCenterX(), ai.body.y, object.getCenterY());
        ai.setForce(VectorMath.forceFalloff(distance, Constants.DETECTION_RANGE, Constants.DETECTION_FALLOFF));

        ai.setSteerTarget(new double[] {targetX, targetY});
    }

    private static void updateVision(AI ai) {
        double[] velocity = ai.getVelocity();
        double length = VectorMath.vectorLength(0, velocity[0], 0, velocity[1]);
        double[] normalVelocity = {-velocity[1], velocity[0]};
        if (length == 0.0) {
            length = 0.001;
        }
        double offsetX = (normalVelocity[0] / length) * Constants.NPC_SIZE / 2.0;
        double offsetY = (normalVelocity[1] / length) * Constants.NPC_SIZE / 2.0;
        ai.detection1.x1 = ai.body.x + offsetX;
        ai.detection1.x2 = ai.body.x + (velocity[0] / length) * Constants.DETECTION_RANGE + (offsetX * Constants.DETECTION_OFFSET);
        ai.detection1.y1 = ai.body.y + offsetY;
        ai.detection1.y2 = ai.body.y + (velocity[1] / length) * Constants.DETECTION_RANGE + (offsetY * Constants.DETECTION_OFFSET);

        ai.detection2.x1 = ai.body.x;
        ai.detection2.x2 = ai.body.x + (velocity[0] / length) * Constants.DETECTION_RANGE;
        ai.detection2.y1 = ai.body.y;
        ai.detection2.y2 = ai.body.y + (velocity[1] / length) * Constants.DETECTION_RANGE;

        ai.detection3.x1 = ai.body.x - offsetX;
        ai.detection3.x2 = ai.body.x + (velocity[0] / length) * Constants.DETECTION_RANGE - (offsetX * Constants.DETECTION_OFFSET);
        ai.detection3.y1 = ai.body.y - offsetY;
        ai.detection3.y2 = ai.body.y + (velocity[1] / length) * Constants.DETECTION_RANGE - (offsetY * Constants.DETECTION_OFFSET);
    }
}
