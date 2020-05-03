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

    private Boolean isFile;

    protected BaseMessage() {
    }

    public BaseMessage(String content, Boolean isFile, User sender) {
        content = content;
        isFile = isFile;
        sender = sender;
        Timestamp time = new Timestamp(System.currentTimeMillis());
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

    public Boolean getFile() {
        return isFile;
    }
}
