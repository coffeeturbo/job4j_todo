package todo.model;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Value
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
public class Category implements IdAble {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id = null;
    String name= null;

}
