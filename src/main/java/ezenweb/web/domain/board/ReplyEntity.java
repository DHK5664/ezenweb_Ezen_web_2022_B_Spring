package ezenweb.web.domain.board;

import ezenweb.web.domain.BaseTime;
import ezenweb.web.domain.member.MemberEntity;
import lombok.*;

import javax.persistence.*;

@Entity@Table(name = "reply")
@Data@NoArgsConstructor@AllArgsConstructor@Builder
public class ReplyEntity extends BaseTime {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rno;
    @Column
    private String rcontent;
    // 작성자fk
    @ManyToOne @JoinColumn(name = "mno") @ToString.Exclude
    private MemberEntity memberEntity;
    // 게시물fk
    @ManyToOne @JoinColumn(name = "bno") @ToString.Exclude
    private BoardEntity boardEntity;

    public ReplyDto todto(){
        return ReplyDto.builder()
                .rno(this.rno).rcontent(this.rcontent)
                .rdate(this.cdate.toLocalDate().toString())
                // cdate[LocalDateTime] rdate[String] 왜?? <--> 형변환 시 LocalDateTime에 대해서는 형변환이 안됨 (objectMapper)에서
                .build();
    }

}
