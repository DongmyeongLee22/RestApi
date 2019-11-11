package me.sun.restapi.events;


import com.fasterxml.jackson.databind.ObjectMapper;
import me.sun.restapi.common.RestDocsConfiguration;
import me.sun.restapi.common.TestDescription;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.hypermedia.HypermediaDocumentation;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
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
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-events").exists())
                .andExpect(jsonPath("_links.update-event").exists())
                .andDo(document("create-event",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query events"),
                                linkWithRel("update-event").description("link to upadte event")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        requestFields(
                                fieldWithPath("name").description("Nmae of new evnet"),
                                fieldWithPath("description").description("description of new evnet"),
                                fieldWithPath("beginEnrollmentDateTime").description("beginEnrollmentDateTime of new evnet"),
                                fieldWithPath("closeEnrollmentDateTime").description("closeEnrollmentDateTime of new evnet"),
                                fieldWithPath("beginEventDateTime").description("beginEventDateTime of new evnet"),
                                fieldWithPath("endEventDateTime").description("endEventDateTime of new evnet"),
                                fieldWithPath("basePrice").description("basePrice of new evnet"),
                                fieldWithPath("maxPrice").description("maxPrice of new evnet"),
                                fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of new evnet"),
                                fieldWithPath("location").description("location of new evnet")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("Location header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content Type")
                        ),
                        //relaxed를 사용하면 필드의 모든 값을 안써도 되지만 그렇기 때문에 정확한 문서를 생성하지 못한다.
                        responseFields(
                                fieldWithPath("id").description("id of new evnet"),
                                fieldWithPath("name").description("Nmae of new evnet"),
                                fieldWithPath("description").description("description of new evnet"),
                                fieldWithPath("beginEnrollmentDateTime").description("beginEnrollmentDateTime of new evnet"),
                                fieldWithPath("closeEnrollmentDateTime").description("closeEnrollmentDateTime of new evnet"),
                                fieldWithPath("beginEventDateTime").description("beginEventDateTime of new evnet"),
                                fieldWithPath("endEventDateTime").description("endEventDateTime of new evnet"),
                                fieldWithPath("basePrice").description("basePrice of new evnet"),
                                fieldWithPath("maxPrice").description("maxPrice of new evnet"),
                                fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of new evnet"),
                                fieldWithPath("location").description("location of new evnet"),
                                fieldWithPath("free").description("free of new evnet"),
                                fieldWithPath("offline").description("offline of new evnet"),
                                fieldWithPath("eventStatus").description("envet status"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.query-events.href").description("link to query-events"),
                                fieldWithPath("_links.update-event.href").description("link to update-event")
                        )
                ))
        ;


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
