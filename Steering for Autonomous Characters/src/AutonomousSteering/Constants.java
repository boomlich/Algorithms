package AutonomousSteering;

public class Constants {

    public static final int TRACK_RESOLUTION = 20;
    public static final int SPAWN_X = 50;
    public static final int SPAWN_Y = 50;
    public static final int NPC_SIZE = 50;


    public static final int PREDICT_DISTANCE = 15;
    public static final int TARGET_OFFSET = 5;

    public static final double[] VELOCITY = {0, 0};
    public static final double[] ACCELERATION = {0, 0};
    public static final int MAX_SPEED = 5;
    public static final double MAX_FORCE = 0.1;
    public static final int REFRESH_RATE = 10;


    // Track initialisation corners
    public static final int[][] TRACK_CORNERS = {
            {100, 100},
            {100, 900},
            {500, 500},
            {900, 900},
            {900, 100}
    };


}
