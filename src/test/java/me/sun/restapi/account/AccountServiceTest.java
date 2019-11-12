package me.sun.restapi.account;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AccountServiceTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    AccountService accountService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void findByUsername() throws Exception {
        //given
        String password = "dongmyeong";
        String username = "dongmyeong@gmail.com";
        Account account = Account.builder()
                .email(username)
                .password(password)
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();

        accountService.saveAccount(account);

        UserDetailsService userDetailsService = (UserDetailsService) accountService;
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        //when

        //then
        assertThat(this.passwordEncoder.matches(password, userDetails.getPassword()));
        System.out.println(password);
        System.out.println(userDetails.getPassword());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void findByUsernameFail() throws Exception {
        //when
        String username = "qwmikoqwd@gmail.com";

        UserDetails userDetails = accountService.loadUserByUsername(username);

        //then
        fail("예외가 발생해야 한다");
    }

    @Test
    public void findByUsernameFailV2() throws Exception {
        //when
        String username = "qwmikoqwd@gmail.com";

        try {
            accountService.loadUserByUsername(username);
            fail("catch 문으로 가야한다");

        } catch (UsernameNotFoundException e) {
            assertThat(e.getMessage()).containsSequence(username);
        }

    }

    @Test
    public void findByUsernameFailV3() throws Exception {

        /*
          Expected
          예상되는 예외를 미리 적어줘야 한다.
         */
        String username = "qwmikoqwd@gmail.com";
        expectedException.expect(UsernameNotFoundException.class);
        expectedException.expectMessage(Matchers.containsString(username));

        //When
        accountService.loadUserByUsername(username);

    }


}