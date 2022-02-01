package AutonomousSteering;

public class AISteering {
    public static void steer(AI ai, double[] target, double force) {
        // Calculate desired velocity and limit by max speed
        double[] desiredVector = {target[0] - ai.body.x, target[1] - ai.body.y};
        VectorMath.limitVector(desiredVector, Constants.MAX_SPEED);

        // Calculate steering force and limit by max force
        double[] velocity = ai.getVelocity();
        double[] steeringVec = {desiredVector[0] - velocity[0], desiredVector[1] - velocity[1]};
        VectorMath.limitVector(steeringVec, Constants.MAX_FORCE);

        steeringVec[0] = steeringVec[0] * force;
        steeringVec[1] = steeringVec[1] * force;

        ai.setAcceleration(steeringVec);
        updateDebug(ai, desiredVector, steeringVec);
    }

    private static void updateDebug(AI ai, double[] desired, double[] steering) {

        ai.steeringVel.x1 = ai.body.x;
        ai.steeringVel.x2 = ai.body.x + steering[0] * 1000;
        ai.steeringVel.y1 = ai.body.y;
        ai.steeringVel.y2 = ai.body.y + steering[1] * 1000;

        ai.desiredVel.x1 = ai.body.x;
        ai.desiredVel.x2 = ai.body.x + desired[0] * 50;
        ai.desiredVel.y1 = ai.body.y;
        ai.desiredVel.y2 = ai.body.y + desired[1] * 50;
    }
}
