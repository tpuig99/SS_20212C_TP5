package io;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Event {
    @JsonProperty("circles")
    private List<Circles> circles;
    @JsonProperty("t")
    private double  t;
    @JsonProperty("total_t")
    private double total_t;

    public Event(List<Circles> circles, double t, double total_t) {
        this.circles = circles;
        this.t = t;
        this.total_t = total_t;
    }

    public Event() {
        /* POJO */
    }

    public List<Circles> getCircles() {
        return circles;
    }

    public void setCircles(List<Circles> circles) {
        this.circles = circles;
    }

    public double getT() {
        return t;
    }

    public void setT(double t) {
        this.t = t;
    }

    public double getTotal_t() {
        return total_t;
    }

    public void setTotal_t(double total_t) {
        this.total_t = total_t;
    }
}
