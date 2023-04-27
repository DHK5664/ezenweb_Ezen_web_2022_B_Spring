package ezenweb.web.domain.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@NoArgsConstructor@AllArgsConstructor@Builder
public class ReplyDto {
    private int rno;
    private String rcontent;
    private String rdate;

    private int bno;

     public ReplyEntity toEntity(){
        return ReplyEntity.builder()
                .rcontent((this.rcontent))
                .build();
    }
}
