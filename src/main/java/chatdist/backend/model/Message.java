package chatdist.backend.model;

import java.sql.Timestamp;
import java.util.UUID;

public class Message {
    private final UUID id;
    private final Timestamp time;

    public Message(UUID id) {
        this.id = id;
        this.time = new Timestamp(System.currentTimeMillis());
    }

    public UUID getId() {
        return id;
    }

    public Timestamp getTime() {
        return time;
    }
}
