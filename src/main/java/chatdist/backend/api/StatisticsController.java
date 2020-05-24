package chatdist.backend.api;

import chatdist.backend.config.WebSocketEventListener;
import chatdist.backend.model.Statistics;
import chatdist.backend.model.User;
import chatdist.backend.repository.ChatroomRepository;
import chatdist.backend.repository.DirectMessageRepository;
import chatdist.backend.repository.GroupMessageRepository;
import chatdist.backend.repository.StatisticsRepository;
import chatdist.backend.util.StatisticsUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.rabbitmq.client.Channel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.Set;

@Controller
public class StatisticsController {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private StatisticsRepository statisticsRepository;

    @Autowired
    private ChatroomRepository chatroomRepository;

    @Autowired
    private DirectMessageRepository directMessageRepository;

    @Autowired
    private GroupMessageRepository groupMessageRepository;

    @Autowired
    private Channel channel;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Resource(name = "queues")
    private Set<User> queues;

    @MessageMapping("/receive-statistics")
    public void receive(@Payload User user) throws Exception {
        logger.info("Preparing statistics reception in queue: {}", user.getBindingName());
        do {
            logger.info("Elements in queue: {}", queues);
            StatisticsUtils statisticsUtils = new StatisticsUtils(chatroomRepository, directMessageRepository,
                    groupMessageRepository, queues);

            Statistics statistics = new Statistics();
            // Admin is not counted
            statistics.setActiveUsers(queues.size()-1);
            statistics.setActiveChatrooms(statisticsUtils.getActiveChatrooms());
            statistics.setMessagesPerMinute(statisticsUtils.getMessagesPerMinute());
            statistics.setBytesPerMinute(statisticsUtils.getBytesPerMinute());

            ObjectMapper mapper = new ObjectMapper();
            try {
                String json = mapper.writeValueAsString(statistics);
                logger.info("Returning statistics {}", json);
                this.messagingTemplate.convertAndSend("/topic/chat/admin/statistics", json);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            Thread.sleep(1000);
        } while (queues.contains(user));

    }
}