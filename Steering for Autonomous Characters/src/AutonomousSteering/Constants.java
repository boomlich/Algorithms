package AutonomousSteering;

public class Constants {

    public static final int PANEL_WIDTH = 1000;
    public static final int PANEL_HEIGHT = 1000;


    public static final int TRACK_RESOLUTION = 20;
    public static final int SPAWN_X = 50;
    public static final int SPAWN_Y = 50;
    public static final int NPC_SIZE = 40;
    public static final int CORNER_SIZE = 25;


    public static final int DEBUG_SIZE = 5;
    public static final int TARGET_OFFSET = 5;
    public static final int PREDICT_DISTANCE = 15;

    public static final double[] VELOCITY = {0, 0};
    public static final double[] ACCELERATION = {0, 0};
    public static final int MAX_SPEED = 5;
    public static final double MAX_FORCE = 0.1;
    public static final int REFRESH_RATE = 10;

    public static final int TRACK_RADIUS = 25;
    public static final int TRACK_SEG_INTERACT_LIMIT = 25;

    public static boolean DEBUG = false;
    public static boolean SHOW_PATH = true;

    public static final int AI_COUNT = 4;
    public static final boolean RND_SPAWN = true;

    public static double INSETS_TOP = 0;
    public static double INSETS_BOTTOM = 0;
    public static double INSETS_SIDES = 0;

    public static boolean RESET = false;



    // Track initialisation corners
    public static final int[][] TRACK_CORNERS = {
            {100, 100},
            {100, 900},
            {500, 500},
            {685, 750},
            {750, 825},
            {800, 840},
            {865, 820},
            {900, 750},
            {900, 100}
    };


}
