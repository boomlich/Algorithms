package AutonomousSteering;

import java.awt.*;

public class Constants {

    public static final int PANEL_WIDTH = 800;
    public static final int PANEL_HEIGHT = 600;

    public static final int TRACK_RESOLUTION = 20;
    public static final int SPAWN_X = 500;
    public static final int SPAWN_Y = 50;
    public static final int NPC_SIZE = 20;
    public static final int CORNER_SIZE = 12;


    public static final int DEBUG_SIZE = 5;
    public static final int TARGET_OFFSET = 5;
    public static final int PREDICT_DISTANCE = 15;

    public static final int DETECTION_RANGE = 100;

    public static final double[] VELOCITY = {0, 0};
    public static final double[] ACCELERATION = {0, 0};
    public static final int MAX_SPEED = 3;
    public static final double MAX_FORCE = 0.1;
    public static final int REFRESH_RATE = 10;

    public static final int TRACK_RADIUS = 12;
    public static final int TRACK_THICKNESS = 40;
    public static final int TRACK_SEG_INTERACT_LIMIT = 12;


    public static boolean DEBUG = true;
    public static boolean SHOW_PATH = true;

    public static final int AI_COUNT = 5;
    public static final boolean RND_SPAWN = true;

    public static double INSETS_TOP = 0;
    public static double INSETS_BOTTOM = 0;
    public static double INSETS_SIDES = 0;

    public static boolean RESET = false;

    public static final Color C_EDGE = new Color(255,255,255, 50);
    public static final Color C_BG = new Color(153,185,152);
    public static final Color C_PATH = new Color(255,255,255,200);
    public static final Color C_PATH_RADIUS = new Color(255,255,255,50);
    public static final Color C_TRACK = new Color(39,54,59);

    //    public static final Color C_TRACK_MID = new Color(244, 131, 125);
    public static final Color C_CORNER = new Color(235, 73, 96);
    public static final Color C_CORNER_EDGE = new Color(237, 165, 175);


    // Track initialisation corners
    public static final int[][] TRACK_CORNERS = {
            {100, 100},
            {100, 415},
            {160, 465},
            {210, 470},
            {330, 320},
            {400, 312},
            {650, 500},
            {700, 460},
            {600, 300},
            {670, 250},
            {724, 100}
    };
}
