package chatdist.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
public class GroupMessage extends BaseMessage {
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty("chatroom")
    private Chatroom chatroom;

    protected GroupMessage() {
    }

    public GroupMessage(String content, ContentType contentType, User sender, Chatroom chatroom) {
        super(content, contentType, sender);
        chatroom = chatroom;
    }

    @Override
    public String toString() {
        return String.format(
                "GroupMessage[id=%d, sender='%s', chatroom='%s', content='%s']",
                getId(), getSender().getUsername(), chatroom.getName(), getContent());
    }

    public Chatroom getChatRoom() {
        return chatroom;
    }

    public void setChatroom(Chatroom chatroom) {
        this.chatroom = chatroom;
    }
}
