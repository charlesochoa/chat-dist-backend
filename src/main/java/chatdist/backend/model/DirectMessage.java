package chatdist.backend.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class DirectMessage extends BaseMessage {
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    protected DirectMessage() {
    }

    public DirectMessage(String content, Boolean isFile, User sender, User receiver) {
        super(content, isFile, sender);
        receiver = receiver;
    }

    @Override
    public String toString() {
        return String.format(
                "DirectMessage[id=%d, sender='%s', receiver='%s', content='%s']",
                getId(), getSender().getUsername(), receiver.getUsername(), getContent());
    }

    public User getReceiver() {
        return receiver;
    }
}
