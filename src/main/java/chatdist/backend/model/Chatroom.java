package chatdist.backend.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @OneToMany
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

    public boolean removeUser(User user) { return users.remove(user); }

    public Set<User> getUsers() {
        return users;
    }

    public void clearUsers() {
        this.users.clear();
    }

    public void setUsers(HashSet<User> users) { this.users = users; }

    public boolean equals(Object obj){
        if (obj instanceof Chatroom) {
            Chatroom chat = (Chatroom) obj;
            return this.bindingName.equals(chat.bindingName);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return bindingName.hashCode();
    }

    public Set<GroupMessage> getGroupMessages() {
        return groupMessages;
    }

    public void setGroupMessages(Set<GroupMessage> groupMessages) {
        this.groupMessages = groupMessages;
    }

    public void clearGroupMessages() {
        this.groupMessages.clear();
    }
}