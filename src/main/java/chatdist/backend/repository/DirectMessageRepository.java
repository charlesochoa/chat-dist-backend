package chatdist.backend.repository;

import chatdist.backend.model.DirectMessage;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DirectMessageRepository  extends BaseMessageRepository<DirectMessage> {
}
