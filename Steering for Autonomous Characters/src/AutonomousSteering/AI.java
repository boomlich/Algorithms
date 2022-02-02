package AutonomousSteering;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Random;

public class AI extends Ellipse2D.Double implements IObstacle {

    private double[] velocity = Arrays.copyOf(Constants.VELOCITY, 2);
    private double[] acceleration = Arrays.copyOf(Constants.ACCELERATION, 2);
    private boolean avoidObject = false;
    private boolean turnRight = false;

    private double[] steerTarget = new double[2];
    private double force;

    Ellipse2D.Double body;
    Rectangle2D.Double body_detect;
    Ellipse2D.Double futurePos;
    Ellipse2D.Double onLine;
    Ellipse2D.Double target;

    Line2D.Double desiredVel;
    Line2D.Double steeringVel;

    Line2D.Double detection1;
    Line2D.Double detection2;
    Line2D.Double detection3;


    public AI(int x, int y) {
        if (Constants.RND_SPAWN) {
            Random random = new Random();
            x = random.nextInt(Constants.PANEL_WIDTH) + 1;
            y = random.nextInt(Constants.PANEL_HEIGHT) + 1;
        }

        body = new Ellipse2D.Double(x, y, Constants.NPC_SIZE, Constants.NPC_SIZE);
        body_detect = new Rectangle2D.Double(x, y, Constants.NPC_SIZE, Constants.NPC_SIZE);

        // Debugging tools
        futurePos = new Ellipse2D.Double(0, 0, Constants.DEBUG_SIZE, Constants.DEBUG_SIZE);
        onLine = new Ellipse2D.Double(0, 0, Constants.DEBUG_SIZE, Constants.DEBUG_SIZE);
        target = new Ellipse2D.Double(0, 0, Constants.DEBUG_SIZE, Constants.DEBUG_SIZE);


        // Velocity
        steeringVel = new Line2D.Double(0,0, 0, 0);
        desiredVel = new Line2D.Double(0,0, 0,0);

        // Object avoidance
        detection1 = new Line2D.Double(0,0, 10, 10);
        detection2 = new Line2D.Double(0,0, 10, 10);
        detection3 = new Line2D.Double(0,0, 10, 10);
    }

    public void update() {
        body_detect.x = body.x;
        body_detect.y = body.y;

        AIObstacleAvoidance.detectObjects(this);
        if (!avoidObject) {
            force = AIPathFollowing.seek(this);
            steerTarget[0] = target.x;
            steerTarget[1] = target.y;
        }
        AISteering.steer(this, steerTarget, force);
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

    public boolean isAvoidObject() {
        return avoidObject;
    }

    public void setAvoidObject(boolean avoidObject) {
        this.avoidObject = avoidObject;
    }

    public double[] getSteerTarget() {
        return steerTarget;
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
}
