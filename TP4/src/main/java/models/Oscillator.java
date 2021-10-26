package models;

public class Oscillator {

    private double r;
    private double v;

    public Oscillator(double r, double v) {
        this.r = r;
        this.v = v;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public double getV() {
        return v;
    }

    public void setV(double v) {
        this.v = v;
    }

    @Override
    public String toString() {
        return String.format("%.5f %.5f", r, v);
    }
}
