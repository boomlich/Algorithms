package AutonomousSteering;

import java.util.LinkedList;

public class VectorMath {

    public static double[] limitVector(double[] inputVector, double force){
        double vectorLength = vectorLength(0, inputVector[0], 0, inputVector[1]);
        inputVector[0] = inputVector[0] / vectorLength * force;
        inputVector[1] = inputVector[1] / vectorLength * force;
        return inputVector;
    }

    public static double vectorLength(double x1, double x2, double y1, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1,2));
    }

    public static double vectorLength(double[] vector) {
        return vectorLength(0, vector[0], 0, vector[1]);
    }


    /**
     *
     * @param x
     * @param y
     * @param pointIndex
     * @param point
     * @return smallestDistance and pointIndex (double)
     */
    public static double[] nearestPoint(double x, double y, int pointIndex, double[] point) {
        int i = 0;
        double smallestDistance = 1000000;
        for (double[] coord: Track.getTrackCoordinates()) {
            double distance = vectorLength(x, coord[0], y, coord[1]);
            if (distance < smallestDistance) {
                smallestDistance = distance;
                point[0] = coord[0];
                point[1] = coord[1];
                pointIndex = i;
            }
            i ++;
        }
        return new double[]{smallestDistance, pointIndex};
    }


    public static double forceFalloff(double distance, double maxDistance, double steepness) {
        double slope = maxDistance/steepness;
        return 1 / (1 + Math.pow(Math.E, - ((slope * 2) / maxDistance) * distance + slope));
    }

}
