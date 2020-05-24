package chatdist.backend.util;

import chatdist.backend.model.User;
import chatdist.backend.repository.ChatroomRepository;
import chatdist.backend.repository.DirectMessageRepository;
import chatdist.backend.repository.GroupMessageRepository;

import java.sql.Timestamp;
import java.util.Set;

public class StatisticsUtils {
    private static final long MINUTES_PER_HOUR = 60;
    private static final long SECONDS_PER_MINUTE = 60;
    private static final long HOURS_PER_DAY = 24;
    private static final long MILLIS_PER_SECOND = 1000L;
    private ChatroomRepository chatroomRepository;
    private DirectMessageRepository directMessageRepository;
    private GroupMessageRepository groupMessageRepository;
    private Set<User> queues;

    public StatisticsUtils(ChatroomRepository chatroomRepository, DirectMessageRepository directMessageRepository,
                           GroupMessageRepository groupMessageRepository, Set<User> queues) {
        this.chatroomRepository = chatroomRepository;
        this.directMessageRepository = directMessageRepository;
        this.groupMessageRepository = groupMessageRepository;
        this.queues = queues;
    }

    public static long differenceInMinutes(Timestamp t1, Timestamp t2) {
        return (t2.getTime() - t1.getTime()) / (MILLIS_PER_SECOND * SECONDS_PER_MINUTE);
    }

    public int getActiveChatrooms() {
        int activeChatrooms = 0;
        for (User loggedIn: queues) {
            activeChatrooms = activeChatrooms + chatroomRepository.totalChatroomsByParticipant(loggedIn);
        }
        return activeChatrooms;
    }

    public float getMessagesPerMinute() {
        int totalDirectMessages = directMessageRepository.getTotalDirectMessages(true);
        Timestamp minTimestampDirectMessages = directMessageRepository.getMinTimestamp(true);
        Timestamp maxTimestampDirectMessages = directMessageRepository.getMaxTimestamp(true);

        int totalGroupMessages = groupMessageRepository.getTotalGroupMessages(true);
        Timestamp minTimestampGroupMessages = groupMessageRepository.getMinTimestamp(true);
        Timestamp maxTimestampGroupMessages = groupMessageRepository.getMaxTimestamp(true);

        int totalMessages = totalDirectMessages + totalGroupMessages;
        Timestamp minTimestamp = null;
        if (minTimestampDirectMessages != null && minTimestampGroupMessages != null) {
            minTimestamp = minTimestampDirectMessages.compareTo(minTimestampGroupMessages) < 0
                    ? minTimestampDirectMessages : minTimestampGroupMessages;
        } else if (minTimestampDirectMessages == null && minTimestampGroupMessages != null) {
            minTimestamp = minTimestampGroupMessages;
        } else if (minTimestampDirectMessages != null && minTimestampGroupMessages == null) {
            minTimestamp = minTimestampDirectMessages;
        }
        Timestamp maxTimestamp = null;
        if (maxTimestampDirectMessages != null && maxTimestampGroupMessages != null) {
            maxTimestamp = maxTimestampDirectMessages.compareTo(maxTimestampGroupMessages) < 0
                    ? maxTimestampGroupMessages : maxTimestampDirectMessages;
        } else if (maxTimestampDirectMessages == null && maxTimestampGroupMessages != null) {
            maxTimestamp = maxTimestampGroupMessages;
        } else if (maxTimestampDirectMessages != null && maxTimestampGroupMessages == null) {
            maxTimestamp = maxTimestampDirectMessages;
        }

        if (minTimestamp == null || maxTimestamp == null) {
            System.out.println("Some of the timestamps are null");
            return (float) 0.0;
        }
        long minutes = differenceInMinutes(minTimestamp, maxTimestamp);

        if (minutes > 0.0) {
            return ((float) totalMessages) / minutes;
        }
        return (float) 0.0;
    }

    public float getBytesPerMinute() {
        Long totalDirectMessagesBytes = directMessageRepository.getTotalBytes(false);
        Timestamp minTimestampDirectMessages = directMessageRepository.getMinTimestamp(false);
        Timestamp maxTimestampDirectMessages = directMessageRepository.getMaxTimestamp(false);

        Long totalGroupMessagesBytes = groupMessageRepository.getTotalBytes(false);
        Timestamp minTimestampGroupMessages = groupMessageRepository.getMinTimestamp(false);
        Timestamp maxTimestampGroupMessages = groupMessageRepository.getMaxTimestamp(false);

        totalDirectMessagesBytes = totalDirectMessagesBytes == null ? 0 : totalDirectMessagesBytes;
        totalGroupMessagesBytes = totalGroupMessagesBytes == null ? 0 : totalGroupMessagesBytes;

        long totalBytes = totalDirectMessagesBytes + totalGroupMessagesBytes;
        Timestamp minTimestamp = null;
        if (minTimestampDirectMessages != null && minTimestampGroupMessages != null) {
            minTimestamp = minTimestampDirectMessages.compareTo(minTimestampGroupMessages) < 0
                    ? minTimestampDirectMessages : minTimestampGroupMessages;
        } else if (minTimestampDirectMessages == null && minTimestampGroupMessages != null) {
            minTimestamp = minTimestampGroupMessages;
        } else if (minTimestampDirectMessages != null && minTimestampGroupMessages == null) {
            minTimestamp = minTimestampDirectMessages;
        }
        Timestamp maxTimestamp = null;
        if (maxTimestampDirectMessages != null && maxTimestampGroupMessages != null) {
            maxTimestamp = maxTimestampDirectMessages.compareTo(maxTimestampGroupMessages) < 0
                    ? maxTimestampGroupMessages : maxTimestampDirectMessages;
        } else if (maxTimestampDirectMessages == null && maxTimestampGroupMessages != null) {
            maxTimestamp = maxTimestampGroupMessages;
        } else if (maxTimestampDirectMessages != null && maxTimestampGroupMessages == null) {
            maxTimestamp = maxTimestampDirectMessages;
        }

        if (minTimestamp == null || maxTimestamp == null) {
            return (float) 0.0;
        }

        long minutes = differenceInMinutes(minTimestamp, maxTimestamp);

        if (minutes > 0) {
            return totalBytes / minutes;
        }
        return (float) 0.0;
    }
}
