package property.app.beans;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.ocpsoft.prettytime.PrettyTime;
import property.app.service.BeanUtil;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

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


@RequiredArgsConstructor
@Getter
@Setter
public class InboxMessageGroup {
    private User sender;
    private List<Message> messageList;
}
