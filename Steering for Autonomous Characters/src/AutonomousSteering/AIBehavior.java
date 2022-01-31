package AutonomousSteering;

import java.awt.geom.Ellipse2D;

public class AIBehavior {
    public static void updateForces(Ellipse2D.Double ai, double[] velocity, double[] acceleration) {
        // Apply acceleration
        velocity[0] += acceleration[0];
        velocity[1] += acceleration[1];

        double velocityLength = VectorMath.vectorLength(0, velocity[0], 0, velocity[1]);
        if (velocityLength > Constants.MAX_SPEED) {
            VectorMath.limitVector(velocity, Constants.MAX_SPEED);
        }

        // Apply velocity
        ai.x += velocity[0];
        ai.y += velocity[1];

        // Reset acceleration
        acceleration[0] = 0;
        acceleration[1] = 0;
    }

    public static void seek(Ellipse2D.Double ai, double targetX, double targetY, double[] velocity, double[] acceleration) {
        // Calculate desired velocity and limit by max speed
        double[] desiredVector = {targetX - ai.x, targetY - ai.y};
        VectorMath.limitVector(desiredVector, Constants.MAX_SPEED);

        // Calculate steering force and limit by max force
        double[] steeringVec = {desiredVector[0] - velocity[0], desiredVector[1] - velocity[1]};
        VectorMath.limitVector(steeringVec, Constants.MAX_FORCE);
        applyForce(steeringVec, acceleration);
    }

    public static void applyForce(double[] force, double[] acceleration) {
        System.out.println("test");
        acceleration = force;
    }
}
