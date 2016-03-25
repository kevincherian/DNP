package cz3002.dnp.Entity;

import java.util.Date;

/**
 * Created by hizac on 25/3/2016.
 */
public class Notification {
    int id;
    User sender;
    User recipient;
    Date time;
    String content;
    int type;

    public int getId() {
        return id;
    }

    public Date getTime() {
        return time;
    }

    public User getSender() {
        return sender;
    }

    public User getRecipient() {
        return recipient;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setType(int type) {
        this.type = type;
    }
}
