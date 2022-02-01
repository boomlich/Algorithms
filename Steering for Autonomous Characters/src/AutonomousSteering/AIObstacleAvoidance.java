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
        Rectangle2D.Double object_obsticle = new Rectangle2D.Double(x, y, size, size);
        boolean avoidObject = false;

        boolean a = ai.detection1.intersects(object_obsticle);
        boolean b = ai.detection2.intersects(object_obsticle);
        boolean c = ai.detection3.intersects(object_obsticle);
        avoidObject = a || b || c;


        if (avoidObject != ai.isAvoidObject()) {
            ai.setAvoidObject(avoidObject);
        }

        if (avoidObject){
            findSteeringDirection(ai, object_obsticle);
        }
    }

    private static void findSteeringDirection (AI ai, Rectangle2D.Double object) {
        // Find steering direction
        double[] velocity = ai.getVelocity();
        double speed = VectorMath.vectorLength(velocity);
        double aX = ai.body.x;
        double aY = ai.body.y;
        double bX = ai.body.x + velocity[0];
        double bY = ai.body.y + velocity[1];
        double mX = object.getCenterX();
        double mY = object.getCenterY();
        boolean turnRight = ((bX - aX) * (mY - aY) - (bY - aY) * (mX - aX)) > 0;
//        System.out.println(turnRight);

        double targetX = ai.body.x + (-velocity[1] / speed) * 150;
        double targetY = ai.body.y + (velocity[0] / speed) * 150;

        double distance = VectorMath.vectorLength(aX, mX, aY, mY);
        ai.setForce(VectorMath.forceFalloff(distance, 200, 50));

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
        ai.detection1.x2 = ai.body.x + (velocity[0] / length) * Constants.DETECTION_RANGE + (offsetX * 3);
        ai.detection1.y1 = ai.body.y + offsetY;
        ai.detection1.y2 = ai.body.y + (velocity[1] / length) * Constants.DETECTION_RANGE + (offsetY * 3);

        ai.detection2.x1 = ai.body.x;
        ai.detection2.x2 = ai.body.x + (velocity[0] / length) * Constants.DETECTION_RANGE;
        ai.detection2.y1 = ai.body.y;
        ai.detection2.y2 = ai.body.y + (velocity[1] / length) * Constants.DETECTION_RANGE;

        ai.detection3.x1 = ai.body.x - offsetX;
        ai.detection3.x2 = ai.body.x + (velocity[0] / length) * Constants.DETECTION_RANGE - (offsetX * 3);
        ai.detection3.y1 = ai.body.y - offsetY;
        ai.detection3.y2 = ai.body.y + (velocity[1] / length) * Constants.DETECTION_RANGE - (offsetY * 3);
    }
}
