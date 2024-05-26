package Doostam.ChatRoom;

import Doostam.User;

import java.util.Calendar;
import java.util.Date;

public class Message {
    private final int userId;
    private final Date date;
    private final String Text;
    private final String username;

    public Message(User user, String text) {
        this.username = user.getUsername();
        this.userId = user.getId();
        this.date = Calendar.getInstance().getTime();
        Text = text;
    }


    public String getUsername() {
        return username;
    }

    public int getUserId() {
        return userId;
    }

    public Date getDate() {
        return date;
    }

    public String getText() {
        return Text;
    }
}
