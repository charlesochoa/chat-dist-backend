package chatdist.backend.model;

import java.sql.Timestamp;
import javax.persistence.*;

@Entity
public class Message {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    private User sender;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    private User receiver;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    private Chatroom chatRoom;

    private byte[] file;
    private Timestamp time;
    private String content;

    protected Message() {}

    public Message(User sender, User receiver, Chatroom chatRoom, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.chatRoom = chatRoom;
        this.content = content;
        this.time = new Timestamp(System.currentTimeMillis());
    }

    @Override
    public String toString() {
        return String.format(
                "Message[id=%d, sender='%s', receiver='%s', content='%s']",
                id, sender.getName(), receiver.getName(), content);
    }

    public Long getId() { return id; }

    public Timestamp getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }

    public byte[] getFile() {
        return file;
    }

    public Chatroom getChatRoom() {
        return chatRoom;
    }

    public User getReceiver() {
        return receiver;
    }

    public User getSender() {
        return sender;
    }
}
