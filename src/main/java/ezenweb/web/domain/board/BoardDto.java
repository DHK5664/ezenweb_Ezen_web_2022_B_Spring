package ezenweb.web.domain.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class BoardDto {
    private int bno;
    private String btitle;
    private String bcontent;
    private int cno;
    private String cname;
    // 작성자 회원정보
    private int mno;
    private String mname;
    // 작성일
    private String bdate;
    // 조회수
    private int bview;

    List<ReplyDto> replyDtoList = new ArrayList<>();
    
    // Entity 변환 메소드
    // 1. toSaveCategoryEntity
    public CategoryEntity toCategoryEntity(){ return CategoryEntity.builder() .cname(this.cname).build();
    }
    // 2. toBoardEntity
    public BoardEntity toBoardEntity(){
        return BoardEntity.builder()
                .btitle(this.btitle)
                .bcontent(this.bcontent)
                .build();
    }
    
}
