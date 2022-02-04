package AutonomousSteering;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Array;
import java.nio.file.Path;
import java.util.ArrayList;

public class Wall extends Rectangle2D.Double implements IObstacle {

    Rectangle2D.Double body;

    public Wall(int x, int y, int width, int height) {
        this.body = new Rectangle2D.Double(x, y, width, height);
    }


    @Override
    public Rectangle2D getBoundingBox() {
        return body.getBounds2D();
    }

    @Override
    public ArrayList<Line2D.Double> getBodyLines() {
        return getLines(body);
    }
}
