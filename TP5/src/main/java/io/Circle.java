package io;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class Circle {
    @JsonProperty("x")
    private double  x;
    @JsonProperty("y")
    private double y;
    @JsonProperty("r")
    private double r;
    @JsonProperty("out")
    private boolean isOut;
    @JsonProperty("exit_time")
    private double exit;

    public Circle(double x, double y, double r, boolean isOut, double exit) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.isOut = isOut;
        this.exit = exit;
    }

    public Circle() {
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

    public boolean isOut() {
        return isOut;
    }

    public void setOut(boolean out) {
        isOut = out;
    }

    public double getExit() {
        return exit;
    }

    public void setExit(double exit) {
        this.exit = exit;
    }

    public String xyz() {
        return String.format("%s %s %s", x, y, r);
    }
}
