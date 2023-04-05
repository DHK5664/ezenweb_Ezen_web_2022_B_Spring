package ezenweb.example.day03;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//롬북 사용
@Data   // getter , setter , toString
@AllArgsConstructor @NoArgsConstructor @Builder
public class NoteDto {
    private int nno;
    private String ncontents;

    // dto --> entity [ 서비스에서 사용 ]
        // this : 현재 클래스내 필드명
    public NoteEntity toEntity(){
        return NoteEntity.builder()
                .nno(this.nno)
                .ncontents(this.ncontents)
                .build();
    }
}
