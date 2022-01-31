package AutonomousSteering;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class Track {

    static ArrayList<int[]> cornerPoints = new ArrayList<>();
    private static LinkedList<double[]> trackCoordinates = new LinkedList<>();
    private static Path2D.Double trackPath;
    static ArrayList<Ellipse2D.Double> cornerShapes = new ArrayList<>();

    public Track() {
        cornerPoints.addAll(Arrays.asList(Constants.TRACK_CORNERS));
        constructTrack();
        calcAllCoordinates();
        constructCorners();

    }

    private static void constructCorners() {
        cornerShapes.clear();
        for (int[] i: cornerPoints) {
            cornerShapes.add(new Ellipse2D.Double(i[0], i[1], Constants.CORNER_SIZE, Constants.CORNER_SIZE));
        }
    }

    private static void constructTrack() {
        trackPath = new Path2D.Double();
        trackPath.moveTo(cornerPoints.get(0)[0], cornerPoints.get(0)[1]);
        for (int i = 1; i < cornerPoints.size(); i++) {
            trackPath.lineTo(cornerPoints.get(i)[0], cornerPoints.get(i)[1]);
        }
        trackPath.closePath();
    }

    private static void calcAllCoordinates(){
        trackCoordinates.clear();
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

    public static void update() {
        Point p = MouseInfo.getPointerInfo().getLocation();
        int mousediffX = p.x - MyFrame.getFrameX() - MouseInput.getX();
        int mousediffY = p.y - MyFrame.getFrameY() - MouseInput.getY();

//        System.out.println(mousediffX + " ::: " + MouseInput.getX());
//        System.out.println(mousediffX);

        cornerPoints.set(MouseInput.getSelectedPoint(), new int[]{p.x - MyFrame.getFrameX(), p.y - MyFrame.getFrameY() - Constants.CORNER_SIZE});
        constructTrack();
        calcAllCoordinates();
        constructCorners();
    }

    public static Path2D.Double getTrackPath(){
        return trackPath;
    }

    public static LinkedList<double[]> getTrackCoordinates() {
        return trackCoordinates;
    }

    public static ArrayList<int[]> getCornerPoints() {
        return cornerPoints;
    }

    public static ArrayList<Ellipse2D.Double> getCornerShapes() {
        return cornerShapes;
    }
}
