package io;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Output {

    @JsonProperty("events")
    private List<Event> events;
    @JsonProperty("Lx")
    private double lx;
    @JsonProperty("Ly")
    private double ly;
    @JsonProperty("gap")
    private double gap;

    public Output(List<Event> events, double lx, double ly, double gap) {
        this.events = events;
        this.lx = lx;
        this.ly = ly;
        this.gap = gap;
    }

    public Output() {
        /* POJO */
    }

    public double getLx() {
        return lx;
    }

    public void setLx(double lx) {
        this.lx = lx;
    }

    public double getLy() {
        return ly;
    }

    public void setLy(double ly) {
        this.ly = ly;
    }

    public double getGap() {
        return gap;
    }

    public void setGap(double gap) {
        this.gap = gap;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
