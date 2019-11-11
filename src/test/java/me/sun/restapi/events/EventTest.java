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

    @Test
    public void testFree() throws Exception {
        //given
        Event event = Event.builder()
                .basePrice(0)
                .maxPrice(0)
                .build();

        //when
        event.update();

        //then
        assertThat(event.isFree()).isTrue();

        //given
        event = Event.builder()
                .basePrice(100)
                .maxPrice(0)
                .build();

        //when
        event.update();

        //then
        assertThat(event.isFree()).isFalse();

        //given
        event = Event.builder()
                .basePrice(0)
                .maxPrice(100)
                .build();

        //when
        event.update();

        //then
        assertThat(event.isFree()).isFalse();
    }

    @Test
    public void testOffine() throws Exception {
        //given
        Event event = Event.builder()
                .location("장소")
                .build();

        //when
        event.update();

        //then
        assertThat(event.isOffline()).isTrue();

        //given
        event = Event.builder()
                .build();

        //when
        event.update();

        //then
        assertThat(event.isOffline()).isFalse();
    }






}