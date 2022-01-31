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

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        x = e.getX() - (int) Constants.INSETS_SIDES;
        y = e.getY() - (int) Constants.INSETS_TOP;


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
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        System.out.println("Mouse released");
        mouseActivated = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

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
}
