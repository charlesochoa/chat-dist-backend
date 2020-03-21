package chatdist.backend.dao;

import chatdist.backend.model.Message;

import java.util.UUID;

public interface MessageDao {

    int insertMessage(UUID id, Message msg);

    default int addMessage(Message msg) {
        UUID id = UUID.randomUUID();
        return insertMessage(id, msg);
    }
}
