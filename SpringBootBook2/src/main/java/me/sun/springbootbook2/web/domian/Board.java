package me.sun.springbootbook2.web.domian;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.sun.springbootbook2.web.domian.enums.BoardType;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String subTitle;

    private String content;

    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @Builder
    public Board(String title, String subTitle, String content, BoardType boardType, User user) {
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.boardType = boardType;
        this.user = user;
    }
}
