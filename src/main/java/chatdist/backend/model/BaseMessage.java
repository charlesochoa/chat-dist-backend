package chatdist.backend.model;

import javax.persistence.*;
import java.sql.Timestamp;

@MappedSuperclass
public class BaseMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    private Timestamp time;

    private String content;

    private Boolean text;

    protected BaseMessage() {
    }

    public BaseMessage(String content, Boolean text, User sender) {
        this.content = content;
        this.text = text;
        this.sender = sender;
        this.time = new Timestamp(System.currentTimeMillis());
    }

    public Long getId() {
        return id;
    }

    public User getSender() {
        return sender;
    }

    public Timestamp getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }

    public Boolean getText() {
        return text;
    }

    public void setText(Boolean text) {
        this.text = text;
    }

}
