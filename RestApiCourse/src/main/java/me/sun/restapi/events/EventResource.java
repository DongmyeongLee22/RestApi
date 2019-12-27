package me.sun.restapi.events;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class EventResource extends Resource<Event> {

    public EventResource(Event event, Link... links) {
        super(event, links);

        add(linkTo(EventController.class).slash(event.getId()).withSelfRel());

        //Type Safe하지 않은 방법이므로 위의 방법을 사용
        //add(new Link("http://localhost:8080/api/events/" + event.getId()));
    }
}
