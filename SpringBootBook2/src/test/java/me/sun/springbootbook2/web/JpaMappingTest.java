package me.sun.springbootbook2.web;

import me.sun.springbootbook2.web.domian.Repository.UserRepository;
import me.sun.springbootbook2.web.domian.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class JpaMappingTest {


    @Autowired
    UserRepository userRepository;


    @Test
    void resositoryTest() throws Exception {
        //given
        User user = User.builder()
                .name("name")
                .password("password")
                .email("email")
                .build();
        User savedUser = userRepository.save(user);
        //when
        User findUser = userRepository.findById(savedUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("잘못된 아이디입니다"));
        //then
        assertThat(findUser.getName()).isEqualTo("name");
        assertThat(findUser.getPassword()).isEqualTo("password");
        assertThat(findUser.getEmail()).isEqualTo("email");
    }


}
