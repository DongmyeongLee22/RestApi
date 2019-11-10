package me.sun.restapi.events;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EventTest {

    @Test
    public void builder() throws Exception {
        Event event = Event.builder()
                .name("이름")
                .description("설명")
                .build();
        assertThat(event).isNotNull();

    }

    @Test
    public void javaBean() throws Exception{
        Event event = new Event();
        String name = "Event";
        String description = "Description";

        event.setName(name);
        event.setDescription(description);

        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);

        }



}