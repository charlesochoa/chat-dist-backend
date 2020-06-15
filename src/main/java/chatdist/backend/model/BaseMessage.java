package chatdist.backend.model;

import javax.persistence.*;
import java.sql.Timestamp;

@MappedSuperclass
public class BaseMessage {
    public enum ContentType {
        REMOVE,
        ADD,
        MESSAGE,
        FILE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    private Timestamp time;

    @Column(name = "content", columnDefinition = "varchar(500)")
    private String content;

    private long bytes;

    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    protected BaseMessage() {
    }

    public BaseMessage(String content, ContentType contentType, User sender) {
        this.content = content;
        this.contentType = contentType;
        this.sender = sender;
        this.time = new Timestamp(System.currentTimeMillis());
        this.bytes = 0;
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

    public long getBytes() {
        return bytes;
    }

    public void setBytes(long bytes) {
        this.bytes = bytes;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

}
