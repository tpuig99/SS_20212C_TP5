package gravitational;

import models.Body;
import models.BodyType;
import models.Oscillator;

import java.awt.geom.Point2D;
import java.util.List;

public class BeemanGravitational {

    private final double G = 6.693E-20;
    private Body currBody;
    private Body prevBody;
    private List<Body> bodies;


    public BeemanGravitational(Body body, List<Body> bodies) {
        this.currBody = body;
        this.bodies = bodies;
        if(currBody.getType() == BodyType.EARTH){
            prevBody = new Body(1.494756064696226E+08,-2.819926499723464E+04,-1.133804292301369E+00,2.780167351164691E+01,currBody.getMass(),currBody.getRadio(),currBody.getType());
        }
        if(currBody.getType() == BodyType.MARS){
            prevBody = new Body(-2.436819140046635E+08,-3.364859267457952E+07,3.539367926945636E+00,-2.379158153666075E+01,currBody.getMass(),currBody.getRadio(),currBody.getType());
        }
    }

    private Point2D calculateAcceleration(Body currBody) {
        double forcex = 0;
        double forcey = 0;
        for (Body body : bodies) {
            if(body.getType()!=currBody.getType()){
                Point2D aux = calculateGravitationalForce(currBody, body);
                forcex += aux.getX();
                forcey += aux.getY();
            }
        }
        double accelerationMod = Math.sqrt( Math.pow(forcex/currBody.getMass(), 2) + Math.pow(forcey/currBody.getMass(), 2) );
        //System.out.println("Acceleration for " + currBody.getType().name() + " is " + accelerationMod);
        return new Point2D.Double(forcex/currBody.getMass(),forcey/currBody.getMass());
    }

    private void generatePrevBody(double dt) {
        Point2D eulerPos = eulerPosition(-dt);
        Point2D eulerVel = eulerVelocity(-dt);
        prevBody = new Body(eulerPos.getX(), eulerPos.getY(), eulerVel.getX(), eulerVel.getY(), currBody.getMass(),currBody.getRadio(), currBody.getType());
    }

    private Point2D eulerPosition(double dt) {
        double x = currBody.getR().getX() + currBody.getV().getX() * dt +
                calculateAcceleration(currBody).getX() * Math.pow(dt, 2) / 2;
        double y = currBody.getR().getY() + currBody.getV().getY() * dt +
                calculateAcceleration(currBody).getY() * Math.pow(dt, 2) / 2;

        return new Point2D.Double(x, y);
    }

    private Point2D eulerVelocity(double dt) {
        double x = currBody.getV().getX() +
                calculateAcceleration(currBody).getX() * dt;
        double y = currBody.getV().getY() +
                calculateAcceleration(currBody).getY() * dt;

        return new Point2D.Double(x, y);

    }

    private Point2D predictVelocity(double dt) {
        Point2D aCurr = calculateAcceleration(currBody);
        Point2D aPrev = calculateAcceleration(prevBody);

        double x = currBody.getV().getX() + 1.5 * aCurr.getX() * dt -
                0.5 * aPrev.getX() * dt;
        double y = currBody.getV().getY() + 1.5 * aCurr.getY() * dt -
                0.5 * aPrev.getY() * dt;

        return new Point2D.Double(x, y);

    }

    private Point2D calculateGravitationalForce(Body b1, Body b2) {
        double distance = calculateDistance(b1, b2);
        double force = G* b1.getMass() * b2.getMass() / Math.pow(distance,2);

        double dx = b2.getR().getX() - b1.getR().getX();
        double dy = b2.getR().getY() - b1.getR().getY();
        double angle = Math.atan2(dy,dx);

        double fx = force * Math.cos(angle);
        double fy = force * Math.sin(angle);

        return new Point2D.Double(fx, fy);
    }

    private double calculateDistance(Body b1, Body b2) {
        double dx = Math.abs(b2.getR().getX() - b1.getR().getX());
        double dy = Math.abs(b2.getR().getY() - b1.getR().getY());
        return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
    }

    public Point2D calculatePosition(double dt) {

        if (prevBody == null) {
            generatePrevBody(dt);
        }

        Point2D aCurr = calculateAcceleration(currBody);
        Point2D aPrev = calculateAcceleration(prevBody);

        double xPos = currBody.getR().getX() + currBody.getV().getX() * dt +
                (1.0 / 6) * (4 * aCurr.getX() - aPrev.getX()) * Math.pow(dt, 2);
        double yPos = currBody.getR().getY() + currBody.getV().getY() * dt +
                (1.0 / 6) * (4 * aCurr.getY() - aPrev.getY()) * Math.pow(dt, 2);

        return new Point2D.Double(xPos, yPos);
    }

    public Point2D calculateVelocity(double dt) {
        if (prevBody == null) {
            generatePrevBody(dt);
        }

        Point2D aCurr = calculateAcceleration(currBody);
        Point2D aPrev = calculateAcceleration(prevBody);

        Point2D predictedPos = calculatePosition(dt);
        Point2D predictedVel = predictVelocity(dt);
        Body predictedBody = new Body(predictedPos.getX(), predictedPos.getY(), predictedVel.getX(), predictedVel.getY(), currBody.getMass(),currBody.getRadio(), currBody.getType());

        Point2D aFut = calculateAcceleration(predictedBody);

        double xPos = currBody.getV().getX() + (1.0 / 6) * (2 * aFut.getX()
                + 5 * aCurr.getX() - aPrev.getX()) * dt;

        double yPos = currBody.getV().getY() + (1.0 / 6) * (2 * aFut.getY()
                + 5 * aCurr.getY() - aPrev.getY()) * dt;

        return new Point2D.Double(xPos, yPos);
    }

    public void updateBody(Point2D p, Point2D v) {
        prevBody = new Body(currBody.getR().getX(), currBody.getR().getY(), currBody.getV().getX(), currBody.getV().getY(), currBody.getMass(),currBody.getRadio(), currBody.getType());
        currBody = new Body(p.getX(), p.getY(), v.getX(),v.getY(), currBody.getMass(),currBody.getRadio(), currBody.getType());
    }

}
