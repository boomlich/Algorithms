package AutonomousSteering;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class AI extends Ellipse2D.Double {

    double[] velocity = Constants.VELOCITY;
    double[] acceleration = Constants.ACCELERATION;
    int predictDistance = 15;
    int predictPointIndex = 0;
    int targetOffset = 5;

    public AI(int x, int y, int size) {
        new Ellipse2D.Double(x, y, size, size);
        setFrame(new Rectangle(x, y, size, size));
    }

    public void updateForces() {
        // Apply acceleration
        velocity[0] += acceleration[0];
        velocity[1] += acceleration[1];

        double velocityLength = VectorMath.vectorLength(0, velocity[0], 0, velocity[1]);
        if (velocityLength > Constants.MAX_SPEED) {
            VectorMath.limitVector(velocity, Constants.MAX_SPEED);
        }

        // Apply velocity
        this.x += velocity[0];
        this.y += velocity[1];

        // Reset acceleration
        acceleration[0] = 0;
        acceleration[1] = 0;
    }


    public void seek(double targetX, double targetY) {
        // Calculate desired velocity and limit by max speed
        double[] desiredVector = {targetX - this.x, targetY - this.y};
        VectorMath.limitVector(desiredVector, Constants.MAX_SPEED);

        // Calculate steering force and limit by max force
        double[] steeringVec = {desiredVector[0] - velocity[0], desiredVector[1] - velocity[1]};
        VectorMath.limitVector(steeringVec, Constants.MAX_FORCE);
        applyForce(steeringVec);
    }

    public void applyForce(double[] force) {
        System.out.println("test");
        acceleration = force;
    }
}
