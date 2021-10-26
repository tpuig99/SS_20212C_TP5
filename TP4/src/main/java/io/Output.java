package io;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Output {

    @JsonProperty("events")
    private List<Event> events;

    public Output(List<Event> events) {
        this.events = events;
    }

    public Output() {
        /* POJO */
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
