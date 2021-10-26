package models;

import java.awt.geom.Point2D;
import java.nio.channels.Pipe;

public class Person {

    private Point2D r;
    private Point2D e;
    private double v;
    private double radius;

    public Person(Point2D r, Point2D e, double v, double radius) {
        this.r = r;
        this.e = e;
        this.v = v;
        this.radius = radius;
    }

    public Point2D calculateV() {
        return new Point2D.Double(e.getX()*v, e.getY()*v);
    }

    public Point2D getR() {
        return r;
    }

    public void setR(Point2D r) {
        this.r = r;
    }

    public Point2D getE() {
        return e;
    }

    public void setE(Point2D e) {
        this.e = e;
    }

    public double getV() {
        return v;
    }

    public void setV(double v) {
        this.v = v;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    public String toString() {
        return "Person{" +
                "r=" + r +
                ", e=" + e +
                ", v=" + v +
                ", radius=" + radius +
                '}';
    }
}
