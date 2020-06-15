package chatdist.backend.api;

import chatdist.backend.config.WebSocketEventListener;
import chatdist.backend.model.Statistics;
import chatdist.backend.model.User;
import chatdist.backend.repository.ChatroomRepository;
import chatdist.backend.repository.DirectMessageRepository;
import chatdist.backend.repository.GroupMessageRepository;
import chatdist.backend.repository.StatisticsRepository;
import chatdist.backend.util.RabbitMQConstants;
import chatdist.backend.util.StatisticsUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.ConcurrentModificationException;
import java.util.Optional;
import java.util.Set;

@Controller
public class WebSocketController {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketController.class);

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

    @MessageMapping("/receive-message")
    public void receive(@Payload User user) throws Exception {
        logger.info("Preparing reception in queue: {}", user.getBindingName());
        StatisticsUtils statisticsUtils = new StatisticsUtils(chatroomRepository, directMessageRepository,
                groupMessageRepository, queues);
        Optional<Statistics> optionalStatistics = statisticsRepository.findById(1L);
        Statistics statistics = null;
        if (optionalStatistics.isPresent()) {
            statistics = optionalStatistics.get();
        } else {
            statistics = new Statistics();
            statistics.setId(1L);
            // Admin is not counted
        }
        statistics.setActiveUsers(queues.size()-1);
        statistics.setActiveChatrooms(statisticsUtils.getActiveChatrooms());
        statisticsRepository.save(statistics);
        queues.add(user);
        logger.info("Elements in queue: {}", queues);
        if (queues.size() <= 1) {
            do {
                try {
                    for (User loggedIn: queues) {
                        GetResponse response = channel.basicGet(loggedIn.getBindingName(), RabbitMQConstants.AUTO_ACK);
                        if (response == null) {
                            Thread.sleep(50);
                        } else {
                            byte[] body = response.getBody();
                            this.messagingTemplate.convertAndSend("/topic/chat/" + loggedIn.getUsername(),
                                    new String(body));
                        }
                    }
                } catch (ConcurrentModificationException e) {
                    continue;
                }
            } while (queues.size() > 0);
            logger.info("Empty queue, ending receive...");
        } else {
            logger.info("Queue has more than one element!");
        }
    }
}
