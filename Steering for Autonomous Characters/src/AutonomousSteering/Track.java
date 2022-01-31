package AutonomousSteering;

import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class Track {

    ArrayList<int[]> cornerPoints = new ArrayList<>();
    private static LinkedList<double[]> trackCoordinates = new LinkedList<>();
    private static Path2D.Double trackPath;

    public Track() {
        cornerPoints.addAll(Arrays.asList(Constants.TRACK_CORNERS));
        constructTrack();
        calcAllCoordinates();
    }

    private void constructTrack() {
        trackPath = new Path2D.Double();
        trackPath.moveTo(cornerPoints.get(0)[0], cornerPoints.get(0)[1]);
        for (int i = 1; i < cornerPoints.size(); i++) {
            trackPath.lineTo(cornerPoints.get(i)[0], cornerPoints.get(i)[1]);
        }
        trackPath.closePath();
    }


    private void calcAllCoordinates(){
        double totalPathLength = 0;
        for (int i = 0; i < cornerPoints.size(); i++) {
            int x1 = cornerPoints.get(i)[0];
            int y1 = cornerPoints.get(i)[1];
            int x2 = cornerPoints.get((i + 1) % cornerPoints.size())[0];
            int y2 = cornerPoints.get((i + 1) % cornerPoints.size())[1];

            double segmentLength = VectorMath.vectorLength(x1, x2, y1, y2);
            totalPathLength += segmentLength;

            for (int j = 0; j < (int) segmentLength; j+= Constants.TRACK_RESOLUTION) {
                trackCoordinates.add(new double[]{x1 + (x2 - x1) / segmentLength * j,
                                                  y1 + (y2 - y1) / segmentLength * j});
            }
        }
    }

    public static Path2D.Double getTrackPath(){
        return trackPath;
    }

    public static LinkedList<double[]> getTrackCoordinates() {
        return trackCoordinates;
    }
}
