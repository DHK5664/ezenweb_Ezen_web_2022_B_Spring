package ezenweb.web.domain.board;

import ezenweb.web.domain.member.MemberEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity@Table(name="board")
@Data@NoArgsConstructor@AllArgsConstructor@Builder // @Data 안에 ToString이 존재함
public class BoardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bno;
    @Column
    private String btitle;
    @Column
    private String bcontent;
    // Fk = 외래키
    // 카테고리번호
    @ManyToOne // 다수가 하나에게 [ fk --> pk ]
    @JoinColumn(name="cno") // fk 필드명
    @ToString.Exclude // 해당 필드는  @ToString 제외 필드 [ * 양방향일때 필수 ]
    private CategoryEntity categoryEntity;
    // 회원번호 [작성자]
    @ManyToOne
    @JoinColumn(name="mno")
    @ToString.Exclude
    private MemberEntity memberEntity;

    // 댓글목록
    @OneToMany(mappedBy = "boardEntity") // 게시물 하나당 댓글 여러개
    @Builder.Default
    private List<ReplyEntity> replyEntityList = new ArrayList<>();

}