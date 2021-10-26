package models;

import io.Circles;

import java.awt.geom.Point2D;

public class Body {
    private final double radio;
    private Point2D r;
    private Point2D v;
    private double mass = 0;
    private BodyType type;
    

    public Body(double rx,double ry,double vx, double vy,double mass, double radio,BodyType type) {
        this.r = new Point2D.Double(rx, ry);
        this.v = new Point2D.Double(vx,vy);
        this.mass = mass;
        this.type = type;
        this.radio = radio;
    }
    public Body(double rx,double ry,double vx, double vy,double mass, BodyType type) {
        this.r = new Point2D.Double(rx, ry);
        this.v = new Point2D.Double(vx,vy);
        this.mass = mass;
        this.type = type;
        this.radio = 0;
    }
    public Point2D getR() {
        return r;
    }

    public void setR(Point2D r) {
        this.r = r;
    }

    public Point2D getV() {
        return v;
    }

    public void setV(Point2D v) {
        this.v = v;
    }

    public double getMass(){
        return mass;
    }

    public BodyType getType() {
        return type;
    }

    public Point2D calculateTVector(Body other){
        double deltaR = getR().distance(other.getR());
        return new Point2D.Double(-1*(other.getR().getY()-getR().getY())/deltaR,(other.getR().getX()-getR().getX())/deltaR);
    }

    public Point2D calculateNormalR(Body other){
        double deltaR = getR().distance(other.getR());
        return new Point2D.Double((other.getR().getX()-getR().getX())/deltaR,(other.getR().getY()-getR().getY())/deltaR);
    }
    public double calculateR() {
        return Math.sqrt(Math.pow(r.getX(),2)+Math.pow(r.getY(),2));
    }

    public double calculateV() {
        return Math.sqrt(Math.pow(v.getX(),2)+Math.pow(v.getY(),2));
    }

    public double getRadio() {
        return radio;
    }

    public Circles getAsCircle(){
        return new Circles(type.ordinal(),r.getX(),r.getY());
    }
    public String xyz(){
        return String.format("%s %f %f %f",type.toString(), r.getX(),r.getY(), radio);
    }
    @Override
    public String toString() {
        return String.format("%s r:(%.5f %.5f) v:(%.5f %.5f)",type.toString(), r.getX(),r.getY(), v.getX(),v.getY());
    }
}
