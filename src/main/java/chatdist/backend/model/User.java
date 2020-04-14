package chatdist.backend.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Table(name="chatUser")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String name;
    private String email;
    private String queueName;

    protected User() {}

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.queueName = email;
    }

    @Override
    public String toString() {
        return String.format(
                "User[id=%d, name='%s', email='%s']",
                id, name, email);
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getQueueName() { return queueName; }

    public void setQueueName(String queueName) { this.queueName = queueName; }
}