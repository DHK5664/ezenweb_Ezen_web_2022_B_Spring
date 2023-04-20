package ezenweb.web.domain.todo;

import lombok.*;

import javax.persistence.*;

@Getter@Setter@ToString@AllArgsConstructor@NoArgsConstructor@Builder
@Entity@Table(name="todo")
public class TodoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String title;
    @Column
    private boolean done;

    public TodoDto todoDto(){
        return TodoDto.builder()
                .id(this.id)    .title(this.title)
                .done(this.done).build();
    }

}
