package me.sun.springbootbook2.web.domian.Repository;

import me.sun.springbootbook2.web.domian.Board;
import me.sun.springbootbook2.web.domian.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Optional<Board> findByUser(User user);
}
