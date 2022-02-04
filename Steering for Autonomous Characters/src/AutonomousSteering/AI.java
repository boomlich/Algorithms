package AutonomousSteering;

import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class AI extends Ellipse2D.Double implements IObstacle {

    private double[] velocity = Arrays.copyOf(Constants.VELOCITY, 2);
    private double[] acceleration = Arrays.copyOf(Constants.ACCELERATION, 2);
    private boolean avoidObject = false;
    private boolean turnRight = false;
    private final int identifier;
    private final RayTrace[] visionRays = new RayTrace[Constants.VISION_ROD_CNT];

    private double[] steerTarget = new double[2];
    private double force;

    Ellipse2D.Double body;
    Ellipse2D.Double futurePos;
    Ellipse2D.Double onLine;
    Ellipse2D.Double target;

    Line2D.Double desiredVel;
    Line2D.Double steeringVel;


    public AI(int x, int y, int identifier) {
        if (Constants.RND_SPAWN) {
            Random random = new Random();
            x = random.nextInt(Constants.PANEL_WIDTH) + 1;
            y = random.nextInt(Constants.PANEL_HEIGHT) + 1;
        }
        body = new Ellipse2D.Double(x, y, Constants.NPC_SIZE, Constants.NPC_SIZE);
        this.identifier = identifier;

        // Debugging tools
        futurePos = new Ellipse2D.Double(0, 0, Constants.DEBUG_SIZE, Constants.DEBUG_SIZE);
        onLine = new Ellipse2D.Double(0, 0, Constants.DEBUG_SIZE, Constants.DEBUG_SIZE);
        target = new Ellipse2D.Double(0, 0, Constants.DEBUG_SIZE, Constants.DEBUG_SIZE);

        // Debug velocity
        steeringVel = new Line2D.Double(0,0, 0, 0);
        desiredVel = new Line2D.Double(0,0, 0,0);

        for (int i = 0; i < Constants.VISION_ROD_CNT; i++) {
            visionRays[i] = new RayTrace(0, 0, 0, 0);
        }
    }

    public void update() {
        for (RayTrace ray: visionRays) {
            ray.update(this);
        }

        AIObstacleAvoidance.detectObjects(this);
        if (!avoidObject) {
            force = AIPathFollowing.seek(this);
            steerTarget[0] = target.x;
            steerTarget[1] = target.y;
        }
        AISteering.steer(this, steerTarget, force);
        Physics.updateForces(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AI ai = (AI) o;
        return identifier == ai.identifier;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), identifier);
    }

    public Ellipse2D.Double getBody() {
        return body;
    }

    public RayTrace[] getVisionRays() {
        return visionRays;
    }

    public Double getFuturePos() {
        return futurePos;
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

    public boolean isAvoidObject() {
        return avoidObject;
    }

    public void setAvoidObject(boolean avoidObject) {
        this.avoidObject = avoidObject;
    }

    public void setSteerTarget(double[] steerTarget) {
        this.steerTarget = steerTarget;
    }

    public void setForce(double force) {
        this.force = force;
    }

    public boolean isTurnRight() {
        return turnRight;
    }

    public void setTurnRight(boolean turnRight) {
        this.turnRight = turnRight;
    }

    @Override
    public Rectangle2D getBoundingBox() {
        return body.getBounds2D();
//        return null;
    }

    @Override
    public ArrayList<Line2D.Double> getBodyLines() {

        // Adjust the hitbox
        x = body.x - body.getWidth() / 2.0;
        y = body.y - body.getWidth() / 2.0;
        Rectangle2D.Double objectBox = new Rectangle2D.Double(x, y, body.getWidth(), body.getHeight());

        return getLines(objectBox);
    }

}
