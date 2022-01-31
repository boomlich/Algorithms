package AutonomousSteering;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Arrays;
import java.util.Random;

public class AI extends Ellipse2D.Double {

    private double[] velocity = Arrays.copyOf(Constants.VELOCITY, 2);
    private double[] acceleration = Arrays.copyOf(Constants.ACCELERATION, 2);

    Ellipse2D.Double body;
    Ellipse2D.Double futurePos;
    Ellipse2D.Double onLine;
    Ellipse2D.Double target;


    public AI(int x, int y) {
        if (Constants.RND_SPAWN) {
            Random random = new Random();
            x = random.nextInt(Constants.PANEL_WIDTH) + 1;
            y = random.nextInt(Constants.PANEL_HEIGHT) + 1;
        }

        body = new Ellipse2D.Double(x, y, Constants.NPC_SIZE, Constants.NPC_SIZE);
        body.setFrame(new Rectangle(x, y, Constants.NPC_SIZE, Constants.NPC_SIZE));

        // Debugging tools
        futurePos = new Ellipse2D.Double(500, 500, Constants.DEBUG_SIZE, Constants.DEBUG_SIZE);
        futurePos.setFrame(new Rectangle(500, 500, Constants.DEBUG_SIZE, Constants.DEBUG_SIZE));

        onLine = new Ellipse2D.Double(500, 500, Constants.DEBUG_SIZE, Constants.DEBUG_SIZE);
        onLine.setFrame(new Rectangle(500, 500, Constants.DEBUG_SIZE, Constants.DEBUG_SIZE));

        target = new Ellipse2D.Double(0, 0, Constants.DEBUG_SIZE, Constants.DEBUG_SIZE);
        target.setFrame(new Rectangle(0, 0, Constants.DEBUG_SIZE, Constants.DEBUG_SIZE));
    }

    public void update() {
        AIPathFollowing.seek(this);
        Physics.updateForces(this);
    }

    public Ellipse2D.Double getBody() {
        return body;
    }

    public Double getFuturePos() {
        return futurePos;
    }

    public Double getOnLine() {
        return onLine;
    }

    public Double getTarget() {
        return target;
    }

    public double[] getVelocity() {
        return velocity;
    }

    public void setVelocity(double[] velocity) {
        this.velocity = velocity;
    }

    public double[] getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(double[] acceleration) {
        this.acceleration = acceleration;
    }
}
