package chatdist.backend.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "chatUser")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL)
    private Set<Chatroom> chatrooms;

    @OneToMany(mappedBy = "sender")
    private Set<DirectMessage> sent_directMessages;

    @OneToMany(mappedBy = "sender")
    private Set<GroupMessage> sent_groupMessages;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    private Set<DirectMessage> received_directMessages;

    private String name;

    private String email;

    private String bindingName;

    protected User() {
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.bindingName = email;
    }

    @Override
    public String toString() {
        return String.format(
                "User[id=%d, name='%s', email='%s']",
                id, name, email);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBindingName() {
        return bindingName;
    }

    public void setBindingName(String queueName) {
        this.bindingName = queueName;
    }
}