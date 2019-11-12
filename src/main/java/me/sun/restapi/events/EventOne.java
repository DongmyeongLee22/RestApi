package me.sun.restapi.events;

import lombok.Getter;

@Getter
public class EventOne {

    private Event event;

    public EventOne(Event event) {
        this.event = event;
    }
}
