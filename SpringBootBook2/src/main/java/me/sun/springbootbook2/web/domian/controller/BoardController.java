package me.sun.springbootbook2.web.domian.controller;

import lombok.RequiredArgsConstructor;
import me.sun.springbootbook2.web.domian.service.BoardService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RequiredArgsConstructor
@Controller
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    @GetMapping({"", "/"})
    public String board(@RequestParam(value = "id", defaultValue = "0") Long id,
                        Model model) {
        model.addAttribute("board", boardService.findById(id));

        return "/board/from";
    }

    @GetMapping("/list")
    public String list(@PageableDefault Pageable pageable, Model model) {
        model.addAttribute("boardList", boardService.findBoardList(pageable));
        return "/board/list";
    }
}
