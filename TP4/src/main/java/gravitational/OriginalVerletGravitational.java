package gravitational;

import models.Body;
import models.BodyType;
import models.Oscillator;
import oscillators.IntegrationScheme;

import java.awt.geom.Point2D;
import java.util.List;

public class OriginalVerletGravitational{
    private final double G = 6.693E-20;
    private Body currBody;
    private Body prevBody;
    private List<Body> bodies;

    public OriginalVerletGravitational(Body body, List<Body> bodies) {
        this.currBody = body;
        this.bodies = bodies;
        if(currBody.getType() == BodyType.EARTH){
            prevBody = new Body(1.494756064696226E+08,-2.819926499723464E+04,-1.133804292301369E+00,2.780167351164691E+01,currBody.getMass(),currBody.getRadio(),currBody.getType());
        }
        if(currBody.getType() == BodyType.MARS){
            prevBody = new Body(-2.436819140046635E+08,-3.364859267457952E+07,3.539367926945636E+00,-2.379158153666075E+01,currBody.getMass(),currBody.getRadio(),currBody.getType());
        }
    }

    private void generatePrevBody(double dt) {
        Point2D eulerPos = eulerPosition(-dt);
        Point2D eulerVel = eulerVelocity(-dt);
        prevBody = new Body(eulerPos.getX(), eulerPos.getY(), eulerVel.getX(), eulerVel.getY(), currBody.getMass(),currBody.getRadio(), currBody.getType());
    }

    private Point2D eulerPosition(double dt) {
        double x = currBody.getR().getX() + currBody.getV().getX() * dt + calculateAcceleration(currBody).getX() * Math.pow(dt, 2) / 2;
        double y = currBody.getR().getY() + currBody.getV().getY() * dt + calculateAcceleration(currBody).getY() * Math.pow(dt, 2) / 2;
        return new Point2D.Double(x, y);
    }

    private Point2D eulerVelocity(double dt) {
        double x = currBody.getV().getX() + calculateAcceleration(currBody).getX() * dt;
        double y = currBody.getV().getY() + calculateAcceleration(currBody).getY() * dt;

        return new Point2D.Double(x, y);

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
    private Point2D calculateGravitationalForce(Body b1, Body b2) {
        double distance = b1.getR().distance(b2.getR());
        double force = G* b1.getMass() * b2.getMass() / Math.pow(distance,2);

        double dx = b2.getR().getX() - b1.getR().getX();
        double dy = b2.getR().getY() - b1.getR().getY();
        double angle = Math.atan2(dy,dx);

        double fx = force * Math.cos(angle);
        double fy = force * Math.sin(angle);

        return new Point2D.Double(fx, fy);
    }

    public Point2D calculatePosition(double dt) {

        if (prevBody == null) {
            generatePrevBody(dt);
        }

        Point2D ac = calculateAcceleration(currBody);
        double rx =2 * currBody.getR().getX() - prevBody.getR().getX() + ac.getX() * Math.pow(dt, 2);
        double ry =2 * currBody.getR().getY() - prevBody.getR().getY() + ac.getY() * Math.pow(dt, 2);
        return new Point2D.Double(rx,ry);
    }

    public Point2D calculateVelocity(double dt) {
        if (prevBody == null) {
            generatePrevBody(dt);
        }
        Point2D nextP = calculatePosition(dt);
        double vx = (nextP.getX() - prevBody.getR().getX())/ (2 * dt);
        double vy = (nextP.getY() - prevBody.getR().getY())/ (2 * dt);

        return new Point2D.Double(vx,vy);
    }

    public void updateBody(Point2D p, Point2D v) {
        prevBody = new Body(currBody.getR().getX(), currBody.getR().getY(), currBody.getV().getX(), currBody.getV().getY(), currBody.getMass(),currBody.getRadio(), currBody.getType());
        currBody = new Body(p.getX(), p.getY(), v.getX(),v.getY(), currBody.getMass(),currBody.getRadio(), currBody.getType());
    }

}
