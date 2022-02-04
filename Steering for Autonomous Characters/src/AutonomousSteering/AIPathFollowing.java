package AutonomousSteering;

public class AIPathFollowing {

    public static void futurePoint(AI ai) {
        ai.futurePos.x = ai.getBody().x + ai.getVelocity()[0] * Constants.PREDICT_DISTANCE;
        ai.futurePos.y = ai.getBody().y + ai.getVelocity()[1] * Constants.PREDICT_DISTANCE;
    }

    public static double seek(AI ai) {
        futurePoint(ai);
        double smallestDistance = findNearestPoint(ai);
        return VectorMath.forceFalloff(smallestDistance, 75, 15);
    }

    private static double findNearestPoint(AI ai) {
        double[] coordinate = new double[2];
        double[] findPoint = VectorMath.nearestPoint(ai.getFuturePos().x, ai.getFuturePos().y, 0, coordinate);
        double smallestDistance = findPoint[0];
        int predictPointIndex = (int) findPoint[1];
        ai.onLine.x = coordinate[0];
        ai.onLine.y = coordinate[1];

        double[] targetCoordinate = Track.getTrackCoordinates().get((predictPointIndex + Constants.TARGET_OFFSET) % Track.getTrackCoordinates().size());
        ai.target.x = targetCoordinate[0];
        ai.target.y = targetCoordinate[1];
        return smallestDistance;
    }
}
