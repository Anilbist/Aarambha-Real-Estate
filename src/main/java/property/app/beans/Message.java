package property.app.beans;

import lombok.*;
import org.ocpsoft.prettytime.PrettyTime;
import property.app.service.BeanUtil;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
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
public class Message extends Auditable{

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver")
    private User receiver;

    @NonNull
    @NotEmpty(message = "Invalid Message")
    @Column(name = "messageText", length = 25000)
    String messageText;

    public String getPrettyTime() {
        PrettyTime pt = BeanUtil.getBean(PrettyTime.class);
        return pt.format(convertToDateViaInstant(getCreationDate()));
    }

    private Date convertToDateViaInstant(LocalDateTime dateToConvert) {
        if(dateToConvert!=null) {
            return Date
                    .from(dateToConvert.atZone(ZoneId.systemDefault()).toInstant());
        }else{
            return new Date("01/01/2021");
        }
    }
}
