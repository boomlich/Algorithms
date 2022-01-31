package AutonomousSteering;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseInput implements MouseListener {

    static boolean mouseActivated = false;

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x=e.getX();
        int y=e.getY();
        System.out.println(x+","+y);
        mouseActivated = true;
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
}
