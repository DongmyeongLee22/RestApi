package me.sun.restapi.events;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.ResourceSupport;

public class EventResourceOld extends ResourceSupport {

    //@JsonUnwrapped를 붙이면 json이 event로 감싸지 않는다.
    @JsonUnwrapped
    private Event event;

    public EventResourceOld(Event event){
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }
}
