package io;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Circles {
    @JsonProperty("id")
    private int id;
    @JsonProperty("x")
    private double  x;
    @JsonProperty("y")
    private double y;

    public Circles(int id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public Circles() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
}
