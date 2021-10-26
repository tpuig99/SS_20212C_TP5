package models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Event {
    @JsonProperty("t")
    private double time;
    @JsonProperty("circles")
    private List<Particle>  particles;
    @JsonProperty("collision")
    private String[] collision;

    public Event(double time, List<Particle>  particles, String[] collision) {
        this.time = time;
        this.particles = particles;
        this.collision = collision;
    }

    public Event() {
        /* POJO */
    }

    public double getTime() {
        return time;
    }

    public List<Particle>  getParticles() {
        return particles;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public void setParticles(List<Particle>  particles) {
        this.particles = particles;
    }

    public String[] getCollision() {
        return collision;
    }

    public void setCollision(String[] collision) {
        this.collision = collision;
    }
}
