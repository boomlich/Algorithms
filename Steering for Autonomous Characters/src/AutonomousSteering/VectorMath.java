package AutonomousSteering;

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
}
