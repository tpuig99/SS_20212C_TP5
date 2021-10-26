package io;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class InitialConditions {
    @JsonProperty("N")
    private long N;

    @JsonProperty("Lx")
    private double Lx;

    @JsonProperty("Ly")
    private double Ly;

    @JsonProperty("rmin")
    private double rmin;

    @JsonProperty("rmax")
    private double rmax;

    @JsonProperty("v")
    private double v;

    @JsonProperty("circles")
    private List<Circle> circles;

    public InitialConditions(long N, double Lx, double Ly, double rmin, double rmax, double v, List<Circle> circles) {
        this.N = N;
        this.Lx = Lx;
        this.Ly = Ly;
        this.rmin = rmin;
        this.rmax = rmax;
        this.v = v;
        this.circles = circles;
    }

    public InitialConditions() {
        /* POJO */
    }

    public long getN() {
        return N;
    }

    public void setN(long n) {
        this.N = n;
    }

    public double getLx() {
        return Lx;
    }

    public void setLx(double lx) {
        this.Lx = lx;
    }

    public double getLy() {
        return Ly;
    }

    public void setLy(double ly) {
        this.Ly = ly;
    }

    public double getRmin() {
        return rmin;
    }

    public void setRmin(double rmin) {
        this.rmin = rmin;
    }

    public double getRmax() {
        return rmax;
    }

    public void setRmax(double rmax) {
        this.rmax = rmax;
    }

    public double getV() {
        return v;
    }

    public void setV(double v) {
        this.v = v;
    }

    public List<Circle> getCircles() {
        return circles;
    }

    public void setCircles(List<Circle> circles) {
        this.circles = circles;
    }
}
