package AutonomousSteering;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public interface IObstacle {

    Rectangle2D getBoundingBox();

    /**
     *
     * @return The lines that make up the body of the object
     */
    ArrayList<Line2D.Double> getBodyLines();



    default ArrayList<Line2D.Double> getLines(Shape object) {
        ArrayList<Point2D> points = new ArrayList<>(4);
        PathIterator pi = object.getBounds2D().getPathIterator(null, 0.5);
        while (!pi.isDone()) {
            double[] coords = new double[6];
            switch (pi.currentSegment(coords)) {
                case PathIterator.SEG_MOVETO, PathIterator.SEG_LINETO -> points.add(new Point2D.Double(coords[0], coords[1]));
            }
            pi.next();
        }

        ArrayList<Line2D.Double> allLines = new ArrayList<>();
        for (int i = 0; i < points.size()-1; i++) {
            double x1 = points.get(i).getX();
            double x2 = points.get((i+1) % points.size()).getX();
            double y1 = points.get(i).getY();
            double y2 = points.get((i+1) % points.size()).getY();

            allLines.add(new Line2D.Double(x1, y1, x2, y2));
        }
        return allLines;
    }


}
