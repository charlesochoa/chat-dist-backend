package chatdist.backend.util;

import chatdist.backend.model.BaseMessage;
import chatdist.backend.model.Chatroom;
import chatdist.backend.model.User;
import chatdist.backend.repository.ChatroomRepository;
import chatdist.backend.repository.DirectMessageRepository;
import chatdist.backend.repository.GroupMessageRepository;

import java.sql.Timestamp;
import java.util.*;

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

    public static Timestamp timeOneHourAgo(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        return new Timestamp(timestamp.getTime() - (MILLIS_PER_SECOND*
                SECONDS_PER_MINUTE*MINUTES_PER_HOUR));
    }
    public static Timestamp timeOneDayAgo(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return new Timestamp(timestamp.getTime() - (MILLIS_PER_SECOND*
                SECONDS_PER_MINUTE*MINUTES_PER_HOUR*HOURS_PER_DAY));
    }

    public int nullSafeSum(Integer numA, Integer numB) {
        return (numA!=null?numA : 0) + (numB!=null?numB : 0);
    }

    public int messagesLastHour(){
        return nullSafeSum(directMessageRepository.getTotalMessagesFromTime(timeOneHourAgo()),
                groupMessageRepository.getTotalMessagesFromTime(timeOneHourAgo()));
    }

    public int messagesLastDay(){
        return nullSafeSum(directMessageRepository.getTotalMessagesFromTime(timeOneDayAgo()),
                groupMessageRepository.getTotalMessagesFromTime(timeOneDayAgo()));
    }
    public int messagesAllTime(){
        return nullSafeSum(directMessageRepository.getTotalMessagesFromTime(new Timestamp(0)),
                groupMessageRepository.getTotalMessagesFromTime(timeOneDayAgo()));
    }

    public int bytesLastHour(){
        return nullSafeSum(directMessageRepository.getTotalBytesFromTime(timeOneHourAgo()),
                groupMessageRepository.getTotalBytesFromTime(timeOneHourAgo()));
    }

    public Integer bytesLastDay(){
        return nullSafeSum(directMessageRepository.getTotalBytesFromTime(timeOneDayAgo()),
                groupMessageRepository.getTotalBytesFromTime(timeOneDayAgo()));
    }
    public Integer bytesAllTime(){
        return nullSafeSum(directMessageRepository.getTotalBytesFromTime(new Timestamp(0)),
                groupMessageRepository.getTotalBytesFromTime(new Timestamp(0)));
    }

    public static long differenceInMinutes(Timestamp t1, Timestamp t2) {
        return (t2.getTime() - t1.getTime()) / (MILLIS_PER_SECOND * SECONDS_PER_MINUTE);
    }

    public int getActiveChatrooms() {
        int activeChatrooms = 0;
        List<Chatroom> chatList = new ArrayList<Chatroom>();
        for (User loggedIn: queues) {
            chatList.addAll(chatroomRepository.findByParticipant(loggedIn));
        }
        Set<Chatroom> chatSet = new HashSet<Chatroom>(chatList);
        return chatSet.size();
    }

    public float getMessagesPerMinute() {
        int totalDirectMessages = directMessageRepository.getTotalDirectMessages(BaseMessage.ContentType.MESSAGE);
        Timestamp minTimestampDirectMessages = directMessageRepository.getMinTimestamp(BaseMessage.ContentType.MESSAGE);
        Timestamp maxTimestampDirectMessages = directMessageRepository.getMaxTimestamp(BaseMessage.ContentType.MESSAGE);

        int totalGroupMessages = groupMessageRepository.getTotalGroupMessages(BaseMessage.ContentType.MESSAGE);
        Timestamp minTimestampGroupMessages = groupMessageRepository.getMinTimestamp(BaseMessage.ContentType.MESSAGE);
        Timestamp maxTimestampGroupMessages = groupMessageRepository.getMaxTimestamp(BaseMessage.ContentType.MESSAGE);

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
            return (float) 0.0;
        }

        long minutes = differenceInMinutes(minTimestamp, maxTimestamp);
        if (minutes > 0.0) {
            return ((float) totalMessages) / minutes;
        }
        return (float) 0.0;
    }

    public float getBytesPerMinute() {

        Long totalDirectMessagesBytes = directMessageRepository.getTotalBytes(BaseMessage.ContentType.FILE);
        Timestamp minTimestampDirectMessages = directMessageRepository.getMinTimestamp(BaseMessage.ContentType.FILE);
        Timestamp maxTimestampDirectMessages = directMessageRepository.getMaxTimestamp(BaseMessage.ContentType.FILE);

        Long totalGroupMessagesBytes = groupMessageRepository.getTotalBytes(BaseMessage.ContentType.FILE);
        Timestamp minTimestampGroupMessages = groupMessageRepository.getMinTimestamp(BaseMessage.ContentType.FILE);
        Timestamp maxTimestampGroupMessages = groupMessageRepository.getMaxTimestamp(BaseMessage.ContentType.FILE);

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
