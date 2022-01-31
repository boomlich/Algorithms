package AutonomousSteering;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyInput implements KeyListener {
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == '1') {
            Constants.DEBUG = !Constants.DEBUG;
        } else if (e.getKeyChar() == '2') {
            Constants.SHOW_PATH = !Constants.SHOW_PATH;
        } else if (e.getKeyChar() == 'r') {
            Track.reset();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
