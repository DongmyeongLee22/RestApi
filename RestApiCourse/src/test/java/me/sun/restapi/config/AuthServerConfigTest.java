package me.sun.restapi.config;

import me.sun.restapi.account.Account;
import me.sun.restapi.account.AccountRepository;
import me.sun.restapi.account.AccountService;
import me.sun.restapi.common.AppProperties;
import me.sun.restapi.common.BaseControllerTest;
import me.sun.restapi.common.RestDocsConfiguration;
import me.sun.restapi.common.TestDescription;
import me.sun.restapi.events.EventRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthServerConfigTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    AccountService accountService;

    @Autowired
    AppProperties appProperties;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    EventRepository eventRepository;


    @Before
    public void clean(){
        eventRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @After
    public void cleanAfter(){
        eventRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    @TestDescription("인증 토큰을 발급 받는 테스트")
    public void getAuthToken() throws Exception {
        String username = "test@gmail.com";
        String password = "password";
        Account account = Account.builder()
                .email(username)
                .password(password)
                .build();

        accountService.saveAccount(account);


        this.mockMvc.perform(post("/oauth/token")
                .with(httpBasic(appProperties.getClientId(), appProperties.getClientSecret()))
                .param("username", username)
                .param("password", password)
                .param("grant_type", "password"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists());
    }
}