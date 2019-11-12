package me.sun.restapi.events;


import me.sun.restapi.account.Account;
import me.sun.restapi.account.AccountRepository;
import me.sun.restapi.account.AccountRole;
import me.sun.restapi.account.AccountService;
import me.sun.restapi.common.BaseControllerTest;
import me.sun.restapi.common.TestDescription;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.IntStream;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class EventControllerTest extends BaseControllerTest {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Before
    public void setUp() {
        this.accountRepository.deleteAll();
        this.eventRepository.deleteAll();
    }

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
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
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
                                linkWithRel("update-event").description("link to upadte event"),
                                linkWithRel("profile").description("link to profile")
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
                        relaxedResponseFields(
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
                                fieldWithPath("_links.update-event.href").description("link to update-event"),
                                fieldWithPath("_links.profile.href").description("link to profile")
                        )
                ))
        ;


    }

    private String getBearerToken() throws Exception {
        return "Bearer " + getAccessToken();
    }

    private String getAccessToken() throws Exception {
        //given
        String username = "dongmyeo2ng@gmail.com";
        String password = "dongmyeong";
        Account account = Account.builder()
                .email(username)
                .password(password)
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();

        this.accountService.saveAccount(account);

        String clientId = "myApp";
        String clientSecret = "pass";

        ResultActions perform = this.mockMvc.perform(post("/oauth/token")
                .with(httpBasic(clientId, clientSecret))
                .param("username", username)
                .param("password", password)
                .param("grant_type", "password"));

        var responseBody = perform.andReturn().getResponse().getContentAsString();
        Jackson2JsonParser parser = new Jackson2JsonParser();
        return parser.parseMap(responseBody).get("access_token").toString();
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
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
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
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
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
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("content[0].objectName").exists())
                .andExpect(jsonPath("content[0].code").exists())
                .andExpect(jsonPath("content[0].defaultMessage").exists())
                .andExpect(jsonPath("_links.index").exists())
        ;

    }

    @Test
    @TestDescription("30개의 이벤트를 10개씩 두번째 페이지 조회하기")
    public void queryEvents() throws Exception {
        //given
        IntStream.range(0, 30).forEach(this::generateEvent);

        //when&then
        this.mockMvc.perform(get("/api/events")
                .param("page", "1")
                .param("size", "10")
                .param("sort", "name,DESC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("query-events"))
        ;
    }

    @Test
    @TestDescription("기존의 이벤트 하나 조회하기")
    public void getEvent() throws Exception {
        //given
        Event event = this.generateEvent(100);

        //when&then
        this.mockMvc.perform(get("/api/events/{id}", event.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("get-an-event"));
    }

    @Test
    @TestDescription("없는 이벤트 조회시 404 응답 받기")
    public void getStatus404() throws Exception {
        //when & then
        this.mockMvc.perform(get("/api/events/12514"))
                .andExpect(status().isNotFound());
        ;
    }


    @Test
    @TestDescription("이벤트를 정상적으로 수정하기")
    public void updateEvent() throws Exception {
        //given
        Event event = this.generateEvent(20);
        EventDto eventDto = this.modelMapper.map(event, EventDto.class);
        String eventName = "수정된 이벤트";
        eventDto.setName(eventName);

        //when & then
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(eventName))
                .andExpect(jsonPath("_links.self").exists())
                .andDo(document("update-event"));
    }

    @Test
    @TestDescription("입력값이 비어있는 경우의 이벤트 수정 실패")
    public void updateEvent400_Empty() throws Exception {
        //given
        Event event = this.generateEvent(20);
        EventDto eventDto = new EventDto();

        //when
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
        //then
    }

    @Test
    @TestDescription("입력값이 잘못된 경우의 이벤트 수정 실패")
    public void updateEvent400_Wrong() throws Exception {
        //given
        Event event = this.generateEvent(20);
        EventDto eventDto = modelMapper.map(event, EventDto.class);
        eventDto.setBasePrice(10000);
        event.setMaxPrice(4000);
        //when
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
        //then
    }


    @Test
    @TestDescription("존재하지 않는 이벤트 수정 실패")
    public void updateEvent404() throws Exception {
        // given
        Event event = this.generateEvent(20);
        EventDto eventDto = modelMapper.map(event, EventDto.class);

        //when & then
        this.mockMvc.perform(put("/api/events/123241")
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    private Event generateEvent(int i) {
        Event event = Event.builder()
                .name("Spring" + i)
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 3, 12, 0))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 3, 14, 0))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 7, 10, 0))
                .endEventDateTime(LocalDateTime.of(2018, 11, 7, 12, 0))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 스타텁 팩토리")
                .free(false)
                .offline(true)
                .eventStatus(EventStatus.DRAFT)
                .build();

        return this.eventRepository.save(event);
    }
}
