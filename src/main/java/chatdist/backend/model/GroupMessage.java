package chatdist.backend.model;

import javax.persistence.*;

@Entity
public class GroupMessage extends BaseMessage {
    @OneToOne
    @JoinColumn(unique = true)
    private Chatroom chatroom;

    protected GroupMessage() {
    }

    public GroupMessage(String content, Boolean isFile, User sender, Chatroom chatroom) {
        super(content, isFile, sender);
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
}
