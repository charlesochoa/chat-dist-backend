package chatdist.backend.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Chatroom {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "chatroom_user",
            joinColumns = @JoinColumn(name = "chatroom_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private Set<User> users;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private User admin;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<GroupMessage> groupMessages;

    private String bindingName;

    private String name;

    protected Chatroom() {
    }

    public Chatroom(String name, User admin) {
        this.name = name;
        this.admin = admin;
        this.users = new HashSet<>();
    }

    @Override
    public String toString() {
        System.out.println("Chatroom String");
        System.out.println(getId());
        System.out.println(getName());
        return String.format(
                "Chatroom[id=%d, name='%s']",
                id, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBindingName() {
        return bindingName;
    }

    public void setBindingName(String bindingName) {
        this.bindingName = bindingName;
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public boolean addUser(User user) {
        return users.add(user);
    }

    public Set<User> getUsers() {
        return users;
    }
}