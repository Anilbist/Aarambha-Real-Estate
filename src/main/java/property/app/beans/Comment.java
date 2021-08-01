package property.app.beans;

import property.app.service.BeanUtil;
import lombok.*;
import org.ocpsoft.prettytime.PrettyTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


/** Description of annotations.
 *  - Entity:
 *      Specifies that the class can be mapped to the table.
 *  - NoArgsConstructor:
 *      Generates Constructor with no arguments.
 *  - Data:
 *      A shortcut for toString, EqualsAndHashCode, Getter, Setter and RequiredArgsConstructor.
 *  - Id:
 *      Specifies the primary key of an entity.
 *  - GeneratedValue:
 *      Provides for the specification of generation strategies for the values of primary keys.
 */

@Entity
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Comment extends Auditable{

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    private String body;

    // Post. Mapping: Many to One. Comments -> Post.
    @ManyToOne
    @NonNull
    private Adds adds;


    public String getPrettyTime() {
        PrettyTime pt = BeanUtil.getBean(PrettyTime.class);
        return pt.format(convertToDateViaInstant(getCreationDate()));
    }

    private Date convertToDateViaInstant(LocalDateTime dateToConvert) {
        if(dateToConvert==null){
            return new Date("01/01/2021");
        }
        return java.util.Date.from(dateToConvert.atZone(ZoneId.systemDefault()).toInstant());
    }

}
