package Doostam;

import Doostam.ArticleAndComment.Article;
import Doostam.ChatRoom.Chat;


import java.text.SimpleDateFormat;
import java.util.*;

public class User {
    private final int id;
    private final Chat savedMessages;
    private String password;
    private String name;
    private String email;
    private String telephoneNumber;
    private String biography;
    private final String username;
    private final Date birthday;
    private final HashSet<Integer> following;
    private final HashSet<Integer> followers;
    private final HashSet<Integer> blocked;
    private final HashSet<Integer> muted;
    private final HashSet<Integer> articles;
    private final HashSet<Integer> chats;
    private final LinkedHashSet<Integer> timeline;
    private final LinkedHashSet<String> notifications;
    private final HashSet<Integer> followRequests;
    private final HashSet<Integer> likedArticles;
    private final HashMap<String, HashSet<String>> groups;
    private Date lastOnline;
    private boolean isPublic;
    private boolean showTel;
    private boolean showBirthday;
    private boolean showEmail;
    private boolean showLastSeen;

    public User(int id, String password, String name, String username, Date birthday, String email, String telephoneNumber, String biography) {
        this.id = id;
        this.biography = biography;
        this.name = name;
        this.username = username;
        this.password = password;
        this.birthday = birthday;
        this.email = email;
        this.telephoneNumber = telephoneNumber;
        this.articles = new HashSet<>();
        this.followers = new HashSet<>();
        this.following = new HashSet<>();
        this.muted = new HashSet<>();
        this.blocked = new HashSet<>();
        this.chats = new HashSet<>();
        this.timeline = new LinkedHashSet<>();
        this.followRequests = new HashSet<>();
        this.notifications = new LinkedHashSet<>();
        this.likedArticles = new HashSet<>();
        this.groups = new HashMap<>();
        this.savedMessages = new Chat(this, DataBase.newChatId());

    }

    public Chat getSavedMessages() {
        return savedMessages;
    }

    public HashSet<Integer> getArticles() {
        return articles;
    }

    public HashSet<Integer> getFollowRequests() {
        return followRequests;
    }

    public boolean isShowTel() {
        return showTel;
    }

    public void setShowTel(boolean showTel) {
        this.showTel = showTel;
    }

    public boolean isShowBirthday() {
        return showBirthday;
    }

    public void setShowBirthday(boolean showBirthday) {
        this.showBirthday = showBirthday;
    }

    public boolean isShowEmail() {
        return showEmail;
    }

    public void setShowEmail(boolean showEmail) {
        this.showEmail = showEmail;
    }

    public boolean isShowLastSeen() {
        return showLastSeen;
    }

    public void setShowLastSeen(boolean showLastSeen) {
        this.showLastSeen = showLastSeen;
    }

    public int getId() {
        return id;
    }

    public void addArticle(Article article){
        articles.add(article.getId());
    }

    /*
    returns name signatures like M. Yousefi or G. H. Hardy
     */
    public String getSignature(){
        /*
        names in Doostam are either 2 or 3 parts
         */
        if(name.split(" ").length==2){
            return name.split(" ")[0].charAt(0)+". "+name.split(" ")[1];
        }
        else {
            return name.split(" ")[0].charAt(0)+". "+name.split(" ")[1].charAt(0)+". "+name.split(" ")[2];
        }
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public String getBiography() {
        return biography;
    }

    public String getUsername() {
        return username;
    }

    public String getBirthday() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy MMMM dd");
        return simpleDateFormat.format(birthday);
    }

    public boolean isPassword(String password){
        return this.password.equals(password);
    }

    public HashSet<Integer> getFollowers() {
        return followers;
    }

    public void addToTimeline(Article article) {
        if(!this.blocked.contains(article.getUserId()) && !this.muted.contains(article.getUserId())) timeline.add(article.getId());
    }

    public boolean blocked(User user){
        return this.blocked.contains(user.getId());
    }

    public HashSet<Integer> getChats() {
        return chats;
    }

    public void addChat(int id) {
        chats.add(id);
    }

    public LinkedHashSet<Integer> getTimeline() {
        return timeline;
    }

    public void followRequest(User user) {
        if (!this.isPublic) {
            this.followRequests.add(user.getId());
            this.notifications.add(user.getUsername() + " wants to follow you.");
        }
    }

    private void addFollowing(User user) {
        following.add(user.getId());
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void block(User user) {
        this.blocked.add(user.getId());
    }

    public void blockChat(Chat chat){
        this.chats.remove(chat.getId());
    }

    public void mute(User user) {
        this.muted.add(user.getId());
    }

    public String getLastOnline() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy MMMM dd hh:mm");
        return simpleDateFormat.format(lastOnline);
    }

    public void setLastOnline() {
        this.lastOnline = Calendar.getInstance().getTime();
    }

    public HashSet<Integer> getFollowing() {
        return following;
    }

    public HashSet<Integer> getBlocked() {
        return blocked;
    }

    public HashSet<Integer> getMuted() {
        return muted;
    }

    public void unfollow(User user) {
        this.following.remove( user.getId());
        user.lostFollower(this);
    }

    private void lostFollower(User user) {
        followers.remove( user.getId());
    }

    public void unblock(User user) {
        this.blocked.remove( user.getId());
    }

    public void removeFromTimeline(int id) {
        this.timeline.remove( id);
    }

    public void unblockChat(Chat chat) {
        this.chats.add(chat.getId());
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public void clearTimeline() {
        this.timeline.clear();
    }

    public void clearNotifications() {
        this.notifications.clear();
    }


    public void addFollower(int id) {
        this.followers.add(id);
    }

    public void addFollowing(int id) {
        this.following.add(id);
    }

    public LinkedHashSet<String> getNotifications() {
        return notifications;
    }

    public void like(int id) {
        this.likedArticles.add(id);
    }

    public void unlike(int id) {
        this.likedArticles.remove(id);
    }

    public void addNotification(String s){
        this.notifications.add(s);
    }

    public void removeFollowRequest(int id){
        this.followRequests.remove(id);
    }

    public HashSet<Integer> getLikedArticles() {
        return likedArticles;
    }

    public void emptyFollowRequest() {
        this.followRequests.clear();
    }

    public void addGroup(String name, HashSet<String> userIds) {
        groups.put(name, userIds);
    }

    public boolean hasGroup(String groupName) {
        return groups.containsKey(groupName);
    }

    public void addToGroup(String groupName, User user) {
        groups.get(groupName).add(user.getUsername());
    }

    public HashSet<String> getGroup(String name) {
        return groups.get(name);
    }


    public void erase(User user) {
        int id = user.getId();
        this.blocked.remove(id);
        this.followers.remove(id);
        this.following.remove(id);
        this.followRequests.remove(id);
        this.muted.remove(id);
    }
}
