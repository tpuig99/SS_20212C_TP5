package gravitational;

import models.Body;
import models.BodyType;
import models.Oscillator;
import oscillators.IntegrationScheme;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class GearOrder5Gravitational{

    private final double G = 6.674E-20;
    private Body currBody;
    List<Body> bodies;

    private final Point2D [] currents;
    private final Point2D [] previous;
    private final double [] coefficients = new double[] { 3.0/20, 251.0/360, 1, 11.0/18, 1.0/6, 1.0/60};

    public GearOrder5Gravitational(Body body, List<Body> bodies) {
        this.currBody = body;
        this.bodies = bodies;

        currents = new Point2D[] {
                new Point2D.Double(body.getR().getX(),body.getR().getY()),
                new Point2D.Double(body.getV().getX(),body.getV().getY()),
                new Point2D.Double(0,0),
                new Point2D.Double(0,0),
                new Point2D.Double(0,0),
                new Point2D.Double(0,0),
        };
        Body prevBody= new Body(1.494756064696226E+08,-2.819926499723464E+04,-1.133804292301369E+00,2.780167351164691E+01,currBody.getMass(),currBody.getRadio(),currBody.getType());
        if(currBody.getType() == BodyType.EARTH){
            prevBody = new Body(1.494756064696226E+08,-2.819926499723464E+04,-1.133804292301369E+00,2.780167351164691E+01,currBody.getMass(),currBody.getRadio(),currBody.getType());
        }
        if(currBody.getType() == BodyType.MARS){
            prevBody = new Body(-2.436819140046635E+08,-3.364859267457952E+07,3.539367926945636E+00,-2.379158153666075E+01,currBody.getMass(),currBody.getRadio(),currBody.getType());
        }
        previous = new Point2D[] {
                new Point2D.Double(prevBody.getR().getX(),prevBody.getR().getY()),
                new Point2D.Double(prevBody.getV().getX(), prevBody.getV().getY()),
                calculateAcceleration(prevBody),
                new Point2D.Double(0,0),
                new Point2D.Double(0,0),
                new Point2D.Double(0,0),
        };
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

    private static int factorial(int n){
        if (n == 0)
            return 1;
        else
            return(n * factorial(n-1));
    }

    public Point2D calculatePosition(double dt) {
        Point2D[] currRps = new Point2D[6];

        double auxX, auxY;

        for (int k = 0 ; k < currents.length; k++) {
            auxX = 0;
            auxY = 0;
            for (int i = 0 ; i < currents.length - k; i++) {
                auxX += previous[i + k].getX() * Math.pow(dt, i) / factorial(i);
                auxY += previous[i + k].getY() * Math.pow(dt, i) / factorial(i);
            }
            currRps[k] = new Point2D.Double(auxX, auxY);
        }

        Body auxiliaryBody = new Body(currRps[0].getX(), currRps[0].getY(), currRps[1].getX(), currRps[1].getY(),currBody.getMass(),currBody.getRadio(),currBody.getType());
        Point2D predictedA = calculateAcceleration(auxiliaryBody);

        Point2D deltaA = new Point2D.Double( predictedA.getX() - currRps[2].getX(),
                predictedA.getY() - currRps[2].getY());

        Point2D deltaR2 = new Point2D.Double( (deltaA.getX() * Math.pow(dt, 2)) / 2 ,
                (deltaA.getY() * Math.pow(dt, 2)) / 2 );

        for (int i = 0 ; i < currents.length ; i++) {
            currents[i] = new Point2D.Double(
                    currRps[i].getX() + coefficients[i] * deltaR2.getX() * factorial(i) / Math.pow(dt, i),
                    currRps[i].getY() + coefficients[i] * deltaR2.getY() * factorial(i) / Math.pow(dt, i)
            );
            previous[i] = (Point2D) currents[i].clone();
        }

        return currents[0];
    }

    public Point2D calculateVelocity(double dt) {
        return currents[1];
    }

}
