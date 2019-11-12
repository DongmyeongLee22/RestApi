package me.sun.restapi.config;

import me.sun.restapi.account.Account;
import me.sun.restapi.account.AccountRole;
import me.sun.restapi.account.AccountService;
import me.sun.restapi.common.AppProperties;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class AppConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {

            @Autowired
            AccountService accountService;

            @Autowired
            AppProperties appProperties;

            @Override
            public void run(ApplicationArguments args) throws Exception {
                saveAccount(appProperties.getAdminUsername(), appProperties.getAdminPassword()
                        , AccountRole.ADMIN, AccountRole.USER);

                saveAccount(appProperties.getUserUsername(), appProperties.getUserPassword(), AccountRole.USER);
            }

            private void saveAccount(String username, String password, AccountRole... roles) {
                Account account = Account.builder()
                        .email(username)
                        .password(password)
                        .roles(Set.of(roles))
                        .build();

                accountService.saveAccount(account);
            }
        };
    }
}
