package ezenweb.example.day01Q;

import lombok.*;

import javax.persistence.*;

@Entity // DB 테이블과 해당 클래스를 매핑[연결]
@Table(name="testboard") // DB테이블명
@Setter@Getter@ToString // 롬북 제공하는 getter,setter
@AllArgsConstructor     // 롬북 제공하는 빈생성자 풀생성자
@NoArgsConstructor
@Builder // 롬북 제공하는 빌더패턴
public class BoardEntity {
    @Id // pk 선언 : 생략시 오류 발생 [JPA 엔티티/테이블당 pk 하나이상 무조건 선언]
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto key
    private int bno;
    @Column // 필드선언
    private String btitle;
    @Column // 필드선언
    private String bcontent;
}
