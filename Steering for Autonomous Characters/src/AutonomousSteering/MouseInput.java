package AutonomousSteering;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;

public class MouseInput implements MouseListener {

    private static boolean mouseActivated = false;
    private static int selectedPoint;
    private static int x;
    private static int y;
    private static double[] closestPoint = new double[2];

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        x = e.getX() - (int) Constants.INSETS_SIDES;
        y = e.getY() - (int) Constants.INSETS_TOP;
        closestPoint = new double[]{-1,-1};


        // Check if clicked on a corner point
        int i = 0;
        for (Ellipse2D.Double corner: Track.getCornerShapes()) {
            boolean insideX = corner.getX() < (x + Constants.CORNER_SIZE / 2.0) && (x + Constants.CORNER_SIZE / 2.0) < corner.getX() + corner.getWidth();
            boolean insideY = corner.getY() < (y + Constants.CORNER_SIZE / 2.0) && (y + Constants.CORNER_SIZE / 2.0) < corner.getY() + corner.getHeight();

            if (insideX && insideY) {
                mouseActivated = true;
                selectedPoint = i;
                break;
            }
            i ++;
        }

        // Check if clicked on path segment
        if (!mouseActivated) {
            int j = 0;
            double smallestDistance = 1000000;
            for (double[] coord: Track.getTrackCoordinates()) {
                double distance = VectorMath.vectorLength(x, coord[0], y, coord[1]);
                if (distance < smallestDistance) {
                    smallestDistance = distance;
                    closestPoint[0] = coord[0];
                    closestPoint[1] = coord[1];
                    selectedPoint = j;
                }
                j ++;
            }
            if (smallestDistance < Constants.TRACK_SEG_INTERACT_LIMIT) {
                int sum = 0;
                for (int k = 0; k < Track.getSegmentPointIndex().size()+1; k++) {
                    if (sum < selectedPoint) {
                        sum += Track.getSegmentPointIndex().get(k);
                    } else {
                        System.out.println("point: " + selectedPoint + "     belongs in: " + k);
                        Track.cornerPoints.add(k, new int[]{x, y});
                        selectedPoint = k;
                        break;
                    }
                }
                System.out.println(selectedPoint);
                mouseActivated = true;
            }
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseActivated = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    public static boolean isMouseActivated() {
        return mouseActivated;
    }

    public static int getSelectedPoint() {
        return selectedPoint;
    }

    public static int getX() {
        return x;
    }

    public static int getY() {
        return y;
    }

    public static double[] getClosestPoint() {
        return closestPoint;
    }
}
