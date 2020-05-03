package chatdist.backend.repository;

import chatdist.backend.model.DirectMessage;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectMessageRepository  extends BaseMessageRepository<DirectMessage> {
}
