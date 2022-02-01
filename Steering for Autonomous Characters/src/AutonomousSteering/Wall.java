package AutonomousSteering;

import java.awt.geom.Rectangle2D;

public class Wall extends Rectangle2D.Double implements IObstacle {

    Rectangle2D.Double body;

    public Wall(int x, int y, int width, int height) {
        this.body = new Rectangle2D.Double(x, y, width, height);
    }
}
