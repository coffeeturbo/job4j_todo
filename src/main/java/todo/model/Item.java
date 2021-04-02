package todo.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String description;
    private Timestamp created;
    private Boolean done;

    public Item() {
    }

    public Item(Integer id) {
        this.id = id;
    }

    public Item(String description, Timestamp created, Boolean done) {
        this.description = description;
        this.created = created;
        this.done = done;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    @Override
    public String toString() {
        return String.format("{\"id\":\"%s\", \"desc\":\"%s\", \"created\":\"%s\", \"done\":\"%s\"}", id, description, created.toString(), done);
    }
}
