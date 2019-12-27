package me.sun.springbootbook2.web.domian.service;

import lombok.RequiredArgsConstructor;
import me.sun.springbootbook2.web.domian.Board;
import me.sun.springbootbook2.web.domian.Repository.BoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public Board findById(Long id) {
        return boardRepository.findById(id)
                .orElse(new Board());
    }

    public Page<Board> findBoardList(Pageable pageable) {
        pageable = PageRequest
                .of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());

        return boardRepository.findAll(pageable);
    }
}
