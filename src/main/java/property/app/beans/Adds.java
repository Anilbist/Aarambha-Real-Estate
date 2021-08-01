package property.app.beans;

import property.app.service.BeanUtil;
import lombok.*;
import org.ocpsoft.prettytime.PrettyTime;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
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


@Entity
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Adds extends Auditable{

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    @NotEmpty(message = "Please enter a title.")
    private String title;
    
    
   @NonNull
    @NotEmpty(message = "Please enter a title.")
   private String  price;
    
   @NonNull
    @NotEmpty(message = "Please enter a title.")  
   private String address;
     
      
     private Float longitude;
     
       
   
     private Float latitude;

    
    @Column(length = 1000)
    private String url;

    @Column(length = 26436700)
    private String imageBase64;

    @Column
    private String isPost;

    // Comments. Mapping: One to Many. Post -> Comments
    @OneToMany(mappedBy = "adds")
    private List<Comment> comments = new ArrayList<>();

    public void addComment(Comment comment) {
        comments.add(comment);
    }


    @ManyToOne      // Relationship. Many posts can belong to one user.
    private User user;


    public String getPrettyTime() {
        PrettyTime pt = BeanUtil.getBean(PrettyTime.class);
        return pt.format(convertToDateViaInstant(getCreationDate()));
    }

    private Date convertToDateViaInstant(LocalDateTime dateToConvert) {
        if(dateToConvert!=null) {
            return java.util.Date
                    .from(dateToConvert.atZone(ZoneId.systemDefault()).toInstant());
        }else{
            return new Date("01/01/2021");
        }
    }
}
