package chatdist.backend.model;

import javax.persistence.*;

@Entity
@Table(name = "statistics")
public class Statistics {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int activeChatrooms;

    private int activeUsers;

    private float messagesPerMinute;

    private float bytesPerMinute;

    public Statistics() {
    }

    public Statistics(int activeChatrooms, int activeUsers, float messagesPerMinute, float bytesPerMinute) {
        this.activeChatrooms = activeChatrooms;
        this.activeUsers = activeUsers;
        this.messagesPerMinute = messagesPerMinute;
        this.bytesPerMinute = bytesPerMinute;
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
