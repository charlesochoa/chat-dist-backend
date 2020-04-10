package chatdist.backend.model;

import java.util.UUID;

public class Chatroom {
    private final UUID id;
    private final String title;

    public Chatroom(UUID id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public UUID getId() {
        return id;
    }
}
