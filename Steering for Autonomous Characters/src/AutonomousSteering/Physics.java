package AutonomousSteering;

public class Physics {

    /**
     * Update acceleration, velocity and position
     *
     * @param ai
     */
    public static void updateForces(AI ai) {
        // Apply acceleration
        double[] velocity = ai.getVelocity();
        velocity[0] += ai.getAcceleration()[0];
        velocity[1] += ai.getAcceleration()[1];

        double velocityLength = VectorMath.vectorLength(0, ai.getVelocity()[0], 0, ai.getVelocity()[1]);
        if (velocityLength > Constants.MAX_SPEED) {
            ai.setVelocity(VectorMath.limitVector(ai.getVelocity(), Constants.MAX_SPEED));
        }

        // Apply velocity
        ai.body.x += ai.getVelocity()[0];
        ai.body.y += ai.getVelocity()[1];

        // Reset acceleration
        ai.setAcceleration(new double[]{0, 0});
    }

}
