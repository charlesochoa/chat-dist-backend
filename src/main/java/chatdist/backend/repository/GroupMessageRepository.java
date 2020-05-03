package chatdist.backend.repository;

import chatdist.backend.model.GroupMessage;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupMessageRepository  extends BaseMessageRepository<GroupMessage> {
}
