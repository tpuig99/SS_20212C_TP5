package models;

import io.Circle;

public class Person implements Cloneable{

    private double id;
    private Versor e;
    private Vector x;
    private double vd;
    private double r;
    private boolean out;
    private double t_out = -1;

    public Person(double id,Vector x, Versor e, double r,double vd) {
        this.id = id;
        this.x = x;
        this.e = e;
        this.r = r;
        this.vd = vd;
        this.out = false;
    }

    public double getId() {
        return id;
    }

    public Vector getX() {
        return x;
    }

    public void setX(Vector x) {
        this.x = x;
    }

    public Versor getE() {
        return e;
    }

    public void setE(Versor e) {
        this.e = e;
    }

    public double getVd() {
        return vd;
    }

    public void setVd(double vd) {
        this.vd = vd;
    }
    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public boolean isOut() {
        return out;
    }

    public void setOut(boolean out) {
        this.out = out;
    }

    public double getT_out() {
        return t_out;
    }

    public void setT_out(double t_out) {
        this.t_out = t_out;
    }

    public void updatePosition (double x, double y) {
        this.x.setX(x);
        this.x.setY(y);
    }

    public Circle getAsCircle(){
        return new Circle(x.getX(), x.getY(), r,out,t_out);
    }


    @Override
    public Person clone() {
        try {
            return (Person) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
