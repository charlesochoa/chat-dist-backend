package chatdist.backend.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Chatroom {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String name;

    protected Chatroom() {}

    public Chatroom(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format(
                "Chatroom[id=%d, name='%s']",
                id, name);
    }

    public Long getId() { return id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }
}