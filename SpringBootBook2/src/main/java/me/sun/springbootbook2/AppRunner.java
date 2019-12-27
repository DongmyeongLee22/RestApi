package me.sun.springbootbook2;

import lombok.RequiredArgsConstructor;
import me.sun.springbootbook2.web.domian.Board;
import me.sun.springbootbook2.web.domian.Repository.BoardRepository;
import me.sun.springbootbook2.web.domian.Repository.UserRepository;
import me.sun.springbootbook2.web.domian.User;
import me.sun.springbootbook2.web.domian.enums.BoardType;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

@RequiredArgsConstructor
@Component
public class AppRunner implements ApplicationRunner {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        User user = User.builder()
                .name("dexter")
                .password("password")
                .email("nasd@gmail.com")
                .build();
        userRepository.save(user);

        IntStream.range(1, 200).forEach(i -> {
            Board board = Board.builder()
                    .title("게시글" + i)
                    .subTitle("순서" + i)
                    .content("내용")
                    .boardType(BoardType.FREE)
                    .user(user)
                    .build();
            boardRepository.save(board);
        });
    }
}
