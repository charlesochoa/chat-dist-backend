package chatdist.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
public class GroupMessage extends BaseMessage {
    @ManyToOne
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
        System.out.println(getId());
        System.out.println(getSender());
        System.out.println(getContent());
        System.out.println(getChatRoom());
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
