package chatdist.backend.model;

import java.sql.Timestamp;
import java.util.UUID;

public class Message {
    private final UUID id;
    private final User sender;
    private final User receiver;
    private final Chatroom chatRoom;
    private final byte[] file;
    private final Timestamp time;
    private final String content;

    public Message(UUID id, User senderId, User receiverId, UUID chatId, User sender, User receiver, Chatroom chatRoom, byte[] file, String content) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.chatRoom = chatRoom;
        this.file = file;
        this.content = content;
        this.time = new Timestamp(System.currentTimeMillis());
    }

    public UUID getId() {
        return id;
    }

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
