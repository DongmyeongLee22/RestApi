package me.sun.restapi.events;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class EventResourceOne extends Resource<EventOne> {

    public EventResourceOne(EventOne content, Link... links) {
        super(content, links);
        add(linkTo(EventController.class).slash(content.getEvent().getId()).withSelfRel());

    }
}
