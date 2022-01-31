package AutonomousSteering;

public class AIPathFollowing {

    public static void futurePoint(AI ai) {
        ai.futurePos.x = ai.getBody().x + ai.getVelocity()[0] * Constants.PREDICT_DISTANCE;
        ai.futurePos.y = ai.getBody().y + ai.getVelocity()[1] * Constants.PREDICT_DISTANCE;
    }

    public static void seek(AI ai) {
        futurePoint(ai);
        double smallestDistance = findNearestPoint(ai);

        if (smallestDistance > Constants.TRACK_RADIUS) {
            steer(ai);
        } else {
            double[] velocity = ai.getVelocity();
            double[] acceleration = ai.getAcceleration();
            double speed = VectorMath.vectorLength(0, velocity[0], 0, velocity[1]);
            acceleration[0] += velocity[0] / speed * 0.01;
            acceleration[1] += velocity[1] / speed * 0.01;
            ai.setAcceleration(acceleration);
        }
    }

    private static void steer(AI ai) {
        // Calculate desired velocity and limit by max speed
        double[] desiredVector = {ai.getTarget().x - ai.body.x, ai.getTarget().y - ai.body.y};
        VectorMath.limitVector(desiredVector, Constants.MAX_SPEED);

        // Calculate steering force and limit by max force
        double[] steeringVec = {desiredVector[0] - ai.getVelocity()[0], desiredVector[1] - ai.getVelocity()[1]};
        VectorMath.limitVector(steeringVec, Constants.MAX_FORCE);
        ai.setAcceleration(steeringVec);
//        Physics.applyForce(ai, steeringVec);
    }

    private static double findNearestPoint(AI ai) {
        double smallestDistance = 1000000;
        int predictPointIndex = 0;
        int i = 0;
        for (double[] coord: Track.getTrackCoordinates()) {
            double distance = VectorMath.vectorLength(ai.getFuturePos().x, coord[0], ai.getFuturePos().y, coord[1]);
            if (distance < smallestDistance) {
                smallestDistance = distance;
                ai.onLine.x = coord[0];
                ai.onLine.y = coord[1];
                predictPointIndex = i;
            }
            i ++;
        }
        double[] targetCoord = Track.getTrackCoordinates().get((predictPointIndex + Constants.TARGET_OFFSET) % Track.getTrackCoordinates().size());
        ai.target.x = targetCoord[0];
        ai.target.y = targetCoord[1];
        return smallestDistance;
    }
}
