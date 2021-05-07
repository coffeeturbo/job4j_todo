package todo.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "item")
public class Item implements IdAble {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String description;

    @Temporal(TemporalType.TIMESTAMP)
    Date created;
    Boolean done;

    @JoinColumn(name = "user_id")
    @ManyToOne
    User user;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    List<Category> categories = new ArrayList<>();
}
