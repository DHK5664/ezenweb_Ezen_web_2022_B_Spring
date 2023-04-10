package ezenweb.web.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners( AuditingEntityListener.class ) // @EnableJpaAuditing 있어야 사용가능
public class BaseTime {
    @CreatedDate
    public LocalDateTime cdate;
    @LastModifiedDate
    public LocalDateTime udate;
}
