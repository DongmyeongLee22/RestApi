package me.sun.restapi.events;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
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
    public void javaBean() throws Exception {
        Event event = new Event();
        String name = "Event";
        String description = "Description";

        event.setName(name);
        event.setDescription(description);

        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);

    }

    @Test
    @Parameters(method = "parametersForTestFree") // parametersFor로하면 method 생략가능
    public void testFree(int basePrice, int maxPrice, boolean isFree) throws Exception {
        //given
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();

        //when
        event.update();

        //then
        assertThat(event.isFree()).isEqualTo(isFree);

    }

    private Object[] parametersForTestFree(){
        return new Object[]{
                new Object[] {0, 0, true},
                new Object[] {100, 0, false},
                new Object[] {0, 100, false}
        };
    }

    @Test
    @Parameters
    public void testOffline(String locaiton, boolean isOffline) throws Exception {
        //given
        Event event = Event.builder()
                .location(locaiton)
                .build();

        //when
        event.update();

        //then
        assertThat(event.isOffline()).isEqualTo(isOffline);

    }

    private Object[] parametersForTestOffline(){
        return new Object[]{
                new Object[] {"강남", true},
                new Object[] {null, false},
                new Object[] {"  ", false},
        };
    }


}