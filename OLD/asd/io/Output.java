package io;

import com.fasterxml.jackson.annotation.JsonProperty;
import models.Event;

import java.util.List;

public class Output {
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

    @JsonProperty("gap")
    private double gap;

    @JsonProperty("frames")
    private long frames;

    @JsonProperty("events")
    private List<Event> events;

    public Output(long N, double Lx, double Ly, double rmin, double rmax, double v, double gap, long frames, List<Event> events) {
        this.N = N;
        this.Lx = Lx;
        this.Ly = Ly;
        this.rmin = rmin;
        this.rmax = rmax;
        this.v = v;
        this.gap = gap;
        this.frames = frames;
        this.events = events;
    }

    public Output() {
        /* POJO */
    }

    public long getN() {
        return N;
    }

    public void setN(long n) {
        N = n;
    }

    public double getLx() {
        return Lx;
    }

    public void setLx(double lx) {
        Lx = lx;
    }

    public double getLy() {
        return Ly;
    }

    public void setLy(double ly) {
        Ly = ly;
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

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public double getGap() {
        return gap;
    }

    public void setGap(double gap) {
        this.gap = gap;
    }

    public long getFrames() {
        return frames;
    }

    public void setFrames(long frames) {
        this.frames = frames;
    }
}
