package chatdist.backend.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "appUser")
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

    private String username;

    private String email;

    private String password;

    private String bindingName;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    protected User() {
    }

    public User(String username,  String password) {
        this.username = username;
        this.bindingName = "user." + username;
        this.password = password;
    }

    public User(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.bindingName = user.getEmail();
        this.id = user.getId();
        this.roles = user.getRoles();
    }

    @Override
    public String toString() {
        return String.format(
                "User[id=%d, name='%s', email='%s']",
                id, username, email);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}