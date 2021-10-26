package models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Particle {
    @JsonProperty("id")
    private String id;

    @JsonProperty("x")
    private double x;

    @JsonProperty("y")
    private double y;

    @JsonProperty("r")
    private double r;

    @JsonProperty("vx")
    private double vx;

    @JsonProperty("vy")
    private double vy;

    public Particle(String id, double x, double y, double r, double vx, double vy) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.r = r;
        this.vx = vx;
        this.vy = vy;
    }

    public Particle() {
        /* POJO */
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public double getVx() {
        return vx;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public double getVy() {
        return vy;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    public void updateVelocity(double vx, double vy) {
        this.vx = vx;
        this.vy = vy;
    }

    public void updatePosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double calculateSpeedModule() {
        return Math.sqrt(Math.pow(vx, 2) + Math.pow(vy, 2));
    }

    public Particle move(double t) {
        return new Particle(id,  x + vx * t, y + vy * t, r, vx, vy);
    }

    public Particle generateCopy() {
        return new Particle(id, x, y, r, vx, vy);
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", r=" + r +
                ", vx=" + vx +
                ", vy=" + vy;
    }
}
