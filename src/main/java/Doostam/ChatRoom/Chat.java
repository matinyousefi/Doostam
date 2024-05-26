package Doostam.ChatRoom;

import Doostam.User;

import java.util.ArrayList;

public class Chat {
    private final int id;
    private final int user1Id;
    private final int user2Id;
    private final ArrayList<Message> messages;
    private int unseenUser1;
    private int unseenUser2;
    private String user1Username;
    private String user2Username;

    public Chat(User user1, User user2, int id) throws Exception {
        if(user1.blocked(user2)) throw new Exception("Unfortunately you can not chat with this user.");
        if(user2.blocked(user1)) throw new Exception("You can not chat with a user you have blocked.");
        if (!(
                user1.getFollowing().contains(user2.getId()) ||
                        user2.getFollowing().contains(user1.getId())
        ) &&
                user1.getId() != user2.getId()) {
            throw new Exception("You can only chat with people you have followed or those following you.");
        }
        this.id = id;
        this.user1Id = user1.getId();
        this.user2Id = user2.getId();
        this.user1Username = user1.getUsername();
        this.user2Username = user2.getUsername();
        this.messages = new ArrayList<>();
    }

    public Chat(User user, int id){
        this.id = id;
        this.user1Id = user.getId();
        this.user2Id = user.getId();
        this.user1Username = user.getUsername();
        this.user2Username = user.getUsername();
        this.messages = new ArrayList<>();
    }

    public void add(Message message) {
        messages.add(message);
        if(message.getUserId()==user1Id){
            unseenUser2++;
        }
        else unseenUser1++;
    }


    public int getUser1Id() {
        return user1Id;
    }

    public int getUser2Id() {
        return user2Id;
    }

    public boolean hasUnseenMessage(int id) {
        return getUnseen(id)!=0;
    }

    public String getUser1Username() {
        return user1Username;
    }

    public String getUser2Username() {
        return user2Username;
    }

    public int getUnseen(int id) {
        if(id==user1Id){
            return unseenUser1;
        }
        else return unseenUser2;
    }

    public void seen(int id) {
        if(id == user1Id) unseenUser1=0;
        else unseenUser2 =0;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public Integer getId() {
        return id;
    }
}
