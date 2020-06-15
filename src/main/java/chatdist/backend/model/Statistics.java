package chatdist.backend.model;

import javax.persistence.*;

@Entity
@Table(name = "statistics")
public class Statistics {
    @Id
    private Long id;

    private int activeChatrooms;

    private int activeUsers;

    private float averageUserPerChatroom;

    private float messagesPerMinute;

    private float bytesPerMinute;

    private int messagesLastHour;

    private int messagesLastDay;

    private int messagesAllTime;

    private int bytesLastHour;

    private int bytesLastDay;

    private int bytesAllTime;


    public Statistics() {
    }

    public Statistics(int activeChatrooms, int activeUsers, float messagesPerMinute, float bytesPerMinute,
                      float averageUserPerChatroom, int messagesLastHour, int messagesLastDay
            , int messagesAllTime, int bytesLastHour, int bytesLastDay, int bytesAllTime) {
        this.activeChatrooms = activeChatrooms;
        this.activeUsers = activeUsers;
        this.averageUserPerChatroom = averageUserPerChatroom;
        this.messagesPerMinute = messagesPerMinute;
        this.bytesPerMinute = bytesPerMinute;
        this.messagesLastHour = messagesLastHour;
        this.messagesLastDay = messagesLastDay;
        this.messagesAllTime = messagesAllTime;
        this.bytesLastHour = bytesLastHour;
        this.bytesLastDay = bytesLastDay;
        this.bytesAllTime = bytesAllTime;
    }



    public int getActiveChatrooms() {
        return activeChatrooms;
    }

    public void setActiveChatrooms(int activeChatrooms) {
        this.activeChatrooms = activeChatrooms;
    }

    public int getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(int activeUsers) {
        this.activeUsers = activeUsers;
    }

    public float getAverageUserPerChatroom() {
        return averageUserPerChatroom;
    }

    public void setAverageUserPerChatroom(float averageUserPerChatroom) {
        this.averageUserPerChatroom = averageUserPerChatroom;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getMessagesLastHour() {
        return messagesLastHour;
    }

    public void setMessagesLastHour(int messagesLastHour) {
        this.messagesLastHour = messagesLastHour;
    }

    public int getMessagesLastDay() {
        return messagesLastDay;
    }

    public void setMessagesLastDay(int messagesLastDay) {
        this.messagesLastDay = messagesLastDay;
    }

    public int getMessagesAllTime() {
        return messagesAllTime;
    }

    public void setMessagesAllTime(int messagesAllTime) {
        this.messagesAllTime = messagesAllTime;
    }

    public int getBytesLastHour() {
        return bytesLastHour;
    }

    public void setBytesLastHour(int bytesLastHour) {
        this.bytesLastHour = bytesLastHour;
    }

    public int getBytesLastDay() {
        return bytesLastDay;
    }

    public void setBytesLastDay(int bytesLastDay) {
        this.bytesLastDay = bytesLastDay;
    }

    public int getBytesAllTime() {
        return bytesAllTime;
    }

    public void setBytesAllTime(int bytesAllTime) {
        this.bytesAllTime = bytesAllTime;
    }

    public float getMessagesPerMinute() {
        return messagesPerMinute;
    }

    public void setMessagesPerMinute(float messagesPerMinute) {
        this.messagesPerMinute = messagesPerMinute;
    }

    public float getBytesPerMinute() {
        return bytesPerMinute;
    }

    public void setBytesPerMinute(float bytesPerMinute) {
        this.bytesPerMinute = bytesPerMinute;
    }

}
