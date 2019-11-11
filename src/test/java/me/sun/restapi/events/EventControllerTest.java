package me.sun.restapi.events;


import com.fasterxml.jackson.databind.ObjectMapper;
import me.sun.restapi.common.TestDescription;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @TestDescription("정상적으로 이벤트를 생성하는 테스트")
    public void createEvent() throws Exception {

        EventDto event = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 3, 12, 0))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 3, 14, 0))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 7, 10, 0))
                .endEventDateTime(LocalDateTime.of(2018, 11, 7, 12, 0))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 스타텁 팩토리")
                .build();


        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8) // 본문에 JSON으로 보내고 있다
                .accept(MediaTypes.HAL_JSON) // 어떠한 응답을 원한다
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated()) // 201 -> Created
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()));

    }

    @Test
    @TestDescription("입력 받을 수 없는 값을 사용한 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request() throws Exception {

        Event event = Event.builder()
                .id(100)
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 3, 12, 0))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 3, 14, 0))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 7, 10, 0))
                .endEventDateTime(LocalDateTime.of(2018, 11, 7, 12, 0))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 스타텁 팩토리")
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.BEGAN_ENROLLMEND)
                .build();


        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8) // 본문에 JSON으로 보내고 있다
                .accept(MediaTypes.HAL_JSON) // 어떠한 응답을 원한다
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest()) // 201 -> Created
        ;

    }

    @Test
    @TestDescription("입력값이 비어 있을때 에러가 발생하는 테스트")
    public void 입력값이비어있을때() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest());

    }

    @Test
    @TestDescription("입력값이 이상할때 에러가 발생하는 테스트")
    public void 입력값이이상할때() throws Exception {

        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 3, 12, 0))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 3, 14, 0))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 7, 10, 0))
                .endEventDateTime(LocalDateTime.of(2018, 11, 7, 12, 0))
                .basePrice(1000) // base가 max보다 큼
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 스타텁 팩토리")
                .build();

        this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].objectName").exists())
                .andExpect(jsonPath("$[0].code").exists())
                .andExpect(jsonPath("$[0].defaultMessage").exists())

        ;

    }

}
