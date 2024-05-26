package Doostam;

import Doostam.ArticleAndComment.*;
import Doostam.ChatRoom.Chat;
import Doostam.ChatRoom.Message;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import static Doostam.ExceptionHandler.handle;
import static Doostam.Signin.*;
import java.util.*;
import static Doostam.InputOutput.*;


public class Doostam {
    private final User user;
    private static final Logger logger = LogManager.getLogger(Doostam.class);
    public static final Throwable back = new Throwable();

    public Doostam(User user) throws Throwable {
        logger.info("{logged in.} "+"{user: "+user.getId()+"}");
        this.user = user;
        main();
    }

    private void main() throws Throwable {
        Helper.setLocation("Doostam:\\> ");
        String command = line("Doostam:\\> ");
        if(command.equals("back")) throw back;
        try {
            switch (command) {
                case "mypage":
                    myPage();
                case "timeline":
                    timeline();
                case "explore":
                    explore();
                case "chatroom":
                    chatroom();
                default:
                    ExceptionHandler.invalidSyntax();
            }
        }
        catch(Throwable ignored){}
        main();
    }

    private void explore() throws Throwable {
        Helper.setLocation("Doostam:\\Explore> ");
        HashSet<Integer> articles = DataBase.exploreArticles(this.user);
        printArticles(articles);
        articlesActions(articles,"Doostam:\\Explore> ");
    }

    private void myPage() throws Throwable {
        Helper.setLocation("Doostam:\\My Page> ");
        String command = line("Doostam:\\My Page> ");
        if(command.equals("back")) throw back;
        try {
            switch (command) {
                case "me":
                    printThisUser();
                case "write":
                    writeArticle();
                case "privacy":
                    privacySetting();
                    break;
                case "editprofile":
                    editProfile();
                    break;
                case "notifications":
                    printNotifications();
                    notificationsActions();
                case "followers":
                    printUsers(this.user.getFollowers());
                    usersActions(this.user.getFollowers(), "Doostam:\\My Page\\Followers> ");
                case "following":
                    printUsers(this.user.getFollowing());
                    usersActions(this.user.getFollowing(), "Doostam:\\My Page\\Following> ");
                case "blocked":
                    printUsers(this.user.getBlocked());
                    usersActions(this.user.getBlocked(),"Doostam:\\My Page\\Blocked> ");
                case "muted":
                    printUsers(this.user.getMuted());
                    usersActions(this.user.getMuted(),"Doostam:\\My Page\\Blocked> ");
                case "deleteaccount":
                    if(yn("This can not be undone. Are you sure?")){
                        logger.info("{Deleted account.} {user: "+this.user.getId()+"}");
                        DataBase.deleteUser(this.user);
                        System.gc();
                        DataBase.load();
                        Signin.login();
                    }
                default:
                    ExceptionHandler.invalidSyntax();
            }
        } catch (Throwable ignored){}
        myPage();
    }

    private void printNotifications() {
        if(this.user.getNotifications().size()==0){
            print("No notification.");
            return;
        }
        for (String string :
                this.user.getNotifications()) {
            print(string);
        }
    }

    private void notificationsActions() throws Throwable {
        Helper.setLocation("Doostam:\\My Page\\Notifications> ");
        String command = line("Doostam:\\My Page\\Notifications> ");
        if(command.equals("back")) throw back;
        else if(command.equals("print")) printNotifications();
        else if(command.equals("clearnotifications")) this.user.clearNotifications();
        else if(command.equals("acceptallrequests")) acceptAllRequests();
        else if(command.split(" ")[0].equals("accept")) {
            try {
                User user = DataBase.getUser(command.split(" ")[1]);
                acceptRequest(user.getId());
            } catch (Exception e){
                handle(e);
            }
        }
        else if(command.equals("rejectallrequests")) rejectAllRequests();
        else if(command.split(" ")[0].equals("reject")) {
            try {
                User user = DataBase.getUser(command.split(" ")[1]);
                rejectRequest(user.getId());
            } catch (Exception e) {
                handle(e);
            }
        }
        else ExceptionHandler.invalidSyntax();
        notificationsActions();
    }

    private void rejectRequest(int id){
        if(yn("Do you want the requester to find out you have rejected this request?")){
            User user = Objects.requireNonNull(DataBase.getUser(id));
            user.addNotification(this.user.getUsername()+" rejected your follow request");
            DataBase.updateUser(user);
        }
        this.user.removeFollowRequest(id);
    }

    private void rejectAllRequests() {
        this.user.emptyFollowRequest();
    }

    private void usersActions(HashSet<Integer> users, String message) throws Throwable {
        String string = line(message);
        switch (string) {
            case "back":
                throw back;
            case "print":
                printUsers(users);
                break;
            case "group":
                group(users);
                break;
            default:
                boolean flag = false;
                for (User user :
                        DataBase.getUsers(users)) {
                    if (user.getUsername().equals(string)) {
                        flag = true;
                        printUser(user);
                        try {
                            userActions(user);
                        } catch (Throwable ignored) {

                        }
                    }
                }
                if (!flag) ExceptionHandler.invalidSyntax();
                break;
        }
        usersActions(users, message);
    }

    private void group(HashSet<Integer> users) throws Throwable {
        String lines = text("Enter the usernames of users you want to group consecutively.");
        String[] usernames = lines.split("\n");
        if(Arrays.asList(usernames).contains("back")) throw back;
        HashSet<String> usernames1 = new HashSet<>();
        for (String username:
                usernames) {
            try {
                User user1 = DataBase.getUser(username);
                String id = user1.getUsername();
                if (!users.contains(user1.getId())) throw new Exception("You can only choose users from the list you are in.");
                usernames1.add(id);
            } catch(Exception e){
                handle(e);
                group(users);
            }
        }
        String name = line("Enter a name for this group of users: ");
        try {
            checkUsername(name);
        } catch (Exception e) {
            if(!e.getMessage().equals("Usernames can only consist of alphabet and numbers.")) handle(e);
            else handle("Group names can only consist of alphabet and numbers");
        }
        this.user.addGroup(name,usernames1);
    }

    private void acceptAllRequests() {
        print("Consider setting your account public so you do not have to accept all requests everytime.");
        for (int i :
                user.getFollowRequests()) {
            acceptRequest(i);
        }
    }

    private void acceptRequest(int id) {
        this.user.removeFollowRequest(id);
        this.user.addFollower(id);
        DataBase.getUser(id).addNotification(this.user.getUsername()+" "+"accepted your follow request");
        DataBase.getUser(id).addFollowing(this.user.getId());
        DataBase.updateUser(this.user);
        DataBase.updateUser(id);
    }

    private void privacySetting() {
        print("Doostam:\\My Page\\Privacy Setting> ");
        print("If you set your privacy level to public everyone can see your articles and can follow you." +
                " Your birthday, last seen, email and telephone will only be shown if you choose to, in the following steps. " +
                "Also the articles and username of a public user will be included in the explore tab.");
        user.setPublic(InputOutput.yn("Do you want your user to be public?"));
        user.setShowBirthday(InputOutput.yn("Do you want your birthday to be visible to all users?"));
        user.setShowLastSeen(InputOutput.yn("Do you want your last seen to be visible to all users?"));
        user.setShowEmail(InputOutput.yn("Do you want your email to be visible to all users?"));
        user.setShowTel(InputOutput.yn("Do you want your telephone number to be visible to all users?"));
        logger.info("{Changed privacy setting.}"+" {user: "+this.user.getId()+"}");
        DataBase.updateUser(user);
    }

    private void editProfile() {
        Helper.setLocation("Doostam:\\My Page\\Profile Edit> ");
        print("Doostam:\\My Page\\Profile Edit> ");
        user.setPassword(inputPassword());
        user.setTelephoneNumber(inputTelephoneNumber());
        user.setEmail(inputEmail());
        user.setBiography(inputBiography());
        logger.info("{Edited profile.}"+" {user: "+this.user.getId()+"}");
        DataBase.updateUser(user);
    }

    private void printThisUser() throws Throwable {
        Helper.setLocation("Doostam:\\My Page\\Me> ");
        print("Username: "+user.getUsername()+
                "\nName: "+user.getName()+
                "\nBiography: "+user.getBiography());
        printConditional("Telephone Number: "+user.getTelephoneNumber(),user.isShowTel());
        printConditional("Birthday: "+user.getBirthday(),user.isShowBirthday());
        printConditional("Email: "+user.getEmail(),user.isShowEmail());
        printConditional("Last seen: "+user.getLastOnline(), user.isShowLastSeen());
        printArticles(this.user.getArticles());
        articlesActions(this.user.getArticles(),"Doostam:\\My Page\\Me> ");
    }

    private void timeline() throws Throwable {
        if(this.user.getTimeline().size()==0){
            print("No article in your timeline.");
            throw back;
        }
        try {
            printArticles(user.getTimeline());
            articlesActionsTimeline();
        } catch (Throwable ignored){
            throw back;
        }
        timeline();
    }

    private void articlesActionsTimeline() throws Throwable {
        Helper.setLocation("Doostam:\\Timeline> ");
        String message = "Doostam:\\Timeline> ";
        HashSet<Integer> articles = this.user.getTimeline();
        String string = line(message);
        if ("back".equals(string)) {
            throw back;
        }
        else if(string.equals("cleartimeline")){
            for (Article a :
                    DataBase.getArticles(this.user.getTimeline())) {
                a.addSeen(this.user);
            }
            this.user.clearTimeline();
            throw back;
        }
        else if(string.equals("print")){
            printArticles(user.getTimeline());
            throw back;
        }
        else if(string.split(" ")[0].equals("dislike")){
            string=string.split(" ")[1];
            if (!InputOutput.isNumber(string)) {
                ExceptionHandler.invalidSyntax();
            } else if (articles.contains(Integer.parseInt(string))) {
                Objects.requireNonNull(DataBase.getArticle(Integer.parseInt(string))).addSeen(this.user);
            } else {
                ExceptionHandler.invalidSyntax();
            }
        }
        else {
            if (!InputOutput.isNumber(string)) {
                ExceptionHandler.invalidSyntax();
            } else if (articles.contains(Integer.parseInt(string))) {
                Article article = DataBase.getArticle(Integer.parseInt(string));
                try {
                    assert article != null;
                    article.addSeen(this.user);
                    printArticle(article);
                    articleOrCommentActions(article);
                } catch (Throwable t) {
                    if(t == back)
                        articlesActions(articles, message);
                    else {
                        logger.fatal(t);
                        t.printStackTrace();
                    }
                }
            } else {
                ExceptionHandler.invalidSyntax();
            }
        }
        articlesActionsTimeline();
    }

    private void articlesActions(HashSet<Integer> articles, String message) throws Throwable {
        String string = line(message);
        if (string.equals("back")) {
            throw back;
        }
        if (string.equals("print")) {
            printArticles(articles);
        } else {
            int number;
            try {
                number = Integer.parseInt(string);
                if (!articles.contains(number))
                    ExceptionHandler.invalidSyntax();
                else {
                    Article article = DataBase.getArticle(number);
                    try {
                        article.addSeen(this.user);
                        printArticle(article);
                        articleOrCommentActions(article);
                    } catch (Throwable ignored) {
                    }
                }
            } catch (NumberFormatException e) {
                ExceptionHandler.invalidSyntax();
            }
        }
        articlesActions(articles, message);
    }

    private void printArticles(HashSet<Integer> articles) throws Throwable {
        if(articles.size()==0){
            print("No articles.");
            throw back;
        }
        for (int articleId :
                articles) {
            printArticlePreview(DataBase.getArticle(articleId));
        }
    }

    private void articleOrCommentActions(ArticleOrComment articleOrComment) throws Throwable {
        String string;
        if(articleOrComment instanceof  Article){
            string = line("Doostam:\\Articles\\"+ articleOrComment.getId()+"> ");
            Helper.setLocation("Doostam:\\Articles\\>");
        }
        else {
            string = line("Doostam:\\Comments\\"+ articleOrComment.getId()+"> ");
            Helper.setLocation("Doostam:\\Comments\\>");
        }
        if("back".equals(string)) throw back;
        try{
            switch (string){
                case "like":
                    boolean result = articleOrComment.like(this.user);
                    /*
                    the following lines keep track of which user liked
                    which articles so in case the number of users grow
                    it would be possible to design a better DataBase.explore() algorithm.
                     */
                    if(result){
                        print("Liked.");
                        if(articleOrComment instanceof Article) user.like(articleOrComment.getId());
                    }
                    else {
                        print("Unliked.");
                        if(articleOrComment instanceof Article) user.unlike(articleOrComment.getId());
                    }
                    DataBase.updateArticleOrComment(articleOrComment);
                    break;
                case "comment":
                    String s = text();
                    if(s.equals("back")) throw back;
                    else {
                        Comment comment = new Comment(DataBase.newCommentId(), this.user.getId(), s);
                        articleOrComment.addComment(comment);
                        DataBase.updateArticleOrComment(articleOrComment);
                        DataBase.updateComment(comment);
                        print("Comment added.");
                    }
                    break;
                case "comments":
                    printComments(articleOrComment);
                    commentsActions(articleOrComment);
                case "user":
                    printUser(DataBase.getUser(articleOrComment.getUserId()));
                    userActions(DataBase.getUser(articleOrComment.getUserId()));
                case "save":
                    if(articleOrComment instanceof Comment) {
                        handle("You cannot save comments.");
                        break;
                    }
                    Chat savedMessages = this.user.getSavedMessages();
                    Message savedToBeMessage = new Message(this.user,"Saved article:\n"+stringIt((Article) articleOrComment));
                    savedMessages.add(savedToBeMessage);
                    DataBase.updateChat(savedMessages);
                    break;
                case "forward":
                    if(articleOrComment instanceof Comment) {
                        handle("You cannot forward comments to other users.");
                        break;
                    }
                    assert articleOrComment instanceof Article;
                    char ug = ug("Forward to a user or group?");
                    if (ug == 'u') {
                        printInLine("To whom?");
                        User anotherUser = enterUsername();
                        try {
                            Chat chat = DataBase.getChat(this.user, anotherUser);
                            Message message = new Message(this.user,"Forwarded article:\n"+stringIt((Article) articleOrComment));
                            chat.add(message);
                            DataBase.updateChat(chat);
                        } catch (Exception e) {
                            ExceptionHandler.handle("You have not started chatting with this user");
                        }
                    }
                    else {
                        String name = line("To which group? ");
                        if(!user.hasGroup(name)) throw new Exception("Group not found.");
                        for (String username :
                                user.getGroup(name)) {
                            try {
                                DataBase.getChat(this.user, DataBase.getUser(username));
                            } catch (Exception e) {
                                handle("You have not started chatting with some users of this group.");
                            }
                        }
                        for (String username :
                                user.getGroup(name)) {
                            try {
                                Chat chat = DataBase.getChat(this.user, DataBase.getUser(username));
                                Message message = new Message(this.user,"Forwarded article:\n"+stringIt((Article) articleOrComment));
                                chat.add(message);
                                DataBase.updateChat(chat);
                            } catch (Exception ignored) {

                            }
                        }
                    }
            }
        }
        catch (Exception e){
            handle(e);
        }
        catch (Throwable t){
            articleOrCommentActions(articleOrComment);
        }
        articleOrCommentActions(articleOrComment);
    }

    private void printUser(User user) {
        print("Username: "+user.getUsername()+
                "\nName: "+user.getName()+
                "\nBiography: "+user.getBiography());
        print(user.getFollowers().size()+" followers    "+user.getFollowing().size()+" following");
        if(canSee(user)){
            if(user.isShowTel()) print("Telephone Number: "+user.getTelephoneNumber());
            if(user.isShowBirthday()) print("Birthday: "+user.getBirthday());
            if(user.isShowEmail()) print("Email: "+user.getEmail());
            if(user.isShowLastSeen()) print("Last seen: "+user.getLastOnline());
        }
        if(user.getFollowers().contains(this.user.getId())) print("Followed by you;");
        else print("Not followed by you;");
        if(this.user.getFollowers().contains(user.getId())) print("Is following you.");
        else print("Isn't following you.");
        if(this.user.getBlocked().contains(user.getId())) print("Blocked.");
        else if(this.user.getMuted().contains(user.getId())) print("Muted.");
    }

    private boolean canSee(User user) {
        boolean canSee = false;
        if(user.isPublic()) canSee = true;
        else if (user.getFollowers().contains(this.user.getId())){
            canSee =true;
        }
        return canSee;
    }

    private void userActions(User user) throws Throwable {
        /*
        no need to worry about myPage being completed and then the rest of methods cause we have not catch the back
        command of myPage and it will be thrown to one more step automatically
         */
        if(this.user.getId()==user.getId()) myPage();
        String string = line("Doostam:\\Users\\"+ user.getUsername()+"> ");
        Helper.setLocation("Doostam:\\Users\\");
        if(string.equals("back")) throw back;
        try {
            switch (string) {
                case "articles":
                    if (canSee(user)) {
                        printArticles(user.getArticles());
                        Helper.setLocation("Doostam:\\Users\\\\Articles> ");
                        articlesActions(user.getArticles(), "Doostam:\\Users\\" + user.getUsername() + "\\Articles> ");
                    } else {
                        print("You do not have access to this user's articles.");
                    }
                case "follow":
                    if (user.getFollowers().contains(this.user.getId()))
                        print("You are already following this user.");
                    else {
                        user.followRequest(this.user);
                        if (user.isPublic()) {
                            this.user.addFollowing(user.getId());
                            user.addFollower(this.user.getId());
                            user.addNotification(this.user.getUsername()+" started following you.");
                            print("You are now following this user.");
                        } else {
                            print("A follow request is sent to the user. You will be informed if your request is accepted.");
                        }
                    }
                    break;
                case "unfollow":
                    if (this.user.getFollowing().contains(user.getId())) {
                        this.user.unfollow(user);
                        user.addNotification(this.user.getUsername()+" is no longer following you");
                        print("User unfollowed.");
                    } else {
                        print("You are already not following this user.");
                    }
                    break;
                case "unblock":
                    if (this.user.getBlocked().contains(user.getId())) {
                        this.user.unblock(user);
                        this.user.unblockChat(DataBase.getChat(this.user, user));
                        print("Unblocked.");
                        break;
                    } else print("You have never blocked this user.");
                case "block":
                    this.user.block(user);
                    this.user.blockChat(DataBase.getChat(this.user, user));
                    print("Blocked.");
                    break;
                case "mute":
                    this.user.mute(user);
                    for (Article article :
                            DataBase.getArticles(user.getArticles())) {
                        if (article.getUserId() == user.getId()) {
                            this.user.removeFromTimeline(article.getId());
                        }
                    }
                    print("Muted.");
                    break;
                case "chat":
                    if (DataBase.hasChat(this.user, user)) {
                        enterChat(DataBase.getChat(this.user, user));
                    } else {
                        newChatWith(user);
                    }
                    break;
                case "addtogroup":
                    String groupName = line("To which group? ");
                    if(this.user.hasGroup(groupName)){
                        this.user.addToGroup(groupName, user);
                        print("Added.");
                    }
                    else {
                        handle("Group not found.");
                        if(yn("Do you want to create a group and include this user in it?")){
                            String name = line("Enter a name for this group: ");
                            HashSet<String> group = new HashSet<>();
                            group.add(user.getUsername());
                            this.user.addGroup(name, group);
                            print("Done.");
                        }
                    }

                default:
                    ExceptionHandler.invalidSyntax();
            }
        } catch (Throwable t){
            userActions(user);
        }
        DataBase.updateUser(this.user);
        DataBase.updateUser(user);
        userActions(user);
    }

    private void commentsActions(ArticleOrComment articleOrComment) throws Throwable {
        String string;
        Helper.setLocation("Doostam:\\Articles\\\\comments> ");
        if(articleOrComment instanceof  Article){
            string = line("Doostam:\\Articles\\"+ articleOrComment.getId()+"\\"+"comments> ");
        }
        else {
            string = line("Doostam:\\Comments\\"+ articleOrComment.getId()+"\\"+"comments> ");
        }
        if (string.equals("back")) throw back;
        else if (string.equals("print")) printComments(articleOrComment);
        else {
            int number;
            try {
                number = Integer.parseInt(string);
                if (!articleOrComment.getComments().contains(number))
                    ExceptionHandler.invalidSyntax();
                else {
                    Comment comment = DataBase.getComment(number);
                    try {
                        printComment(comment);
                        articleOrCommentActions(comment);
                    } catch (Throwable ignored) {
                    }
                }
            } catch (NumberFormatException e) {
                ExceptionHandler.invalidSyntax();
            }
        }
        commentsActions(articleOrComment);
    }

    private void chatroom() throws Throwable {
        try {
            HashSet<Chat> chatRoom = DataBase.getChats(user.getChats());
            Helper.setLocation("Doostam:\\Chat Room> ");
            String string = line("Doostam:\\Chat Room> ");
            switch (string){
                case "back":
                    throw back;
                case "listchats":
                    if (chatRoom.size() == 0) print("No chats yet.");
                    for (Chat chat :
                            chatRoom) {
                        if (chat.hasUnseenMessage(user.getId())) {
                            printChatPreviewUnseen(chat);
                        }
                    }
                    for (Chat chat :
                            chatRoom) {
                        if (!chat.hasUnseenMessage(user.getId())) {
                            printChatPreviewSeen(chat);
                        }
                    }
                    chatroom();
                case "newchat":
                    try {
                        newChat();
                    } catch (Throwable throwable) {
                        chatroom();
                    }
                default: {
                    User user = DataBase.getUser(string);
                    Chat chat = DataBase.getChat(user, this.user);
                    enterChat(chat);
                }
            }
        }
        catch (Exception e){
            handle(e);
            chatroom();
        }
    }

    private void newChat() throws Throwable {
        try {
            printInLine("With whom? (username) ");
            User anotherUser = enterUsername();
            newChatWith(anotherUser);
        } catch (Exception e){
            handle(e);
            newChat();
        }
    }

    private void newChatWith (User anotherUser) throws Throwable{
        try {
            DataBase.canChat(this.user, anotherUser);
            Chat chat = new Chat(anotherUser,this.user, DataBase.newChatId());
            this.user.addChat(chat.getId());
            anotherUser.addChat(chat.getId());
            DataBase.updateChat(chat);
            DataBase.updateUser(this.user);
            DataBase.updateUser(anotherUser);
            enterChat(chat);
        } catch (Exception e){
            handle(e);
            newChat();
        }
    }

    private void newMessage(Chat chat) throws Throwable {
        String text = text();
        if(text.endsWith("back")) throw back;
        Message message = new Message(this.user,text);
        chat.add(message);
        DataBase.updateChat(chat);
        System.out.println(ConsoleColors.RESET+"Message Sent."+ConsoleColors.GREEN);
        throw back;
    }

    private void enterChat(Chat chat) throws Throwable {
        String another;
        if(chat.getUser2Id()==this.user.getId()) another=chat.getUser1Username();
        else another = chat.getUser2Username();
        Helper.setLocation("Doostam:\\Chat Room\\>");
        print("Doostam:\\Chat Room\\"+another+">");
        printChat(chat);
        chat.seen(user.getId());
        newMessage(chat);
    }

    public void writeArticle() throws Throwable {
        print("Doostam:\\My Page\\Write> ");
        Helper.setLocation("Doostam:\\My Page\\Write> ");
        Subject subject = inputSubject();
        String abs = inputAbs();
        Field field = inputField();
        Area area = inputArea();
        DataBase.addArea(area);
        String body = inputBody();
        if(!InputOutput.yn("Submit the article?")){
            throw back;
        }
        Article article = new Article(subject, this.user.getId(), abs, field, area, body, DataBase.newArticleId());
        this.user.addArticle(article);
        DataBase.updateArticle(article);
        DataBase.updateUser(this.user);
        for (User user :
                DataBase.getUsers(this.user.getFollowers())) {
            user.addToTimeline(article);
            DataBase.updateUser(user);
        }
        throw back;
    }

    private void printChatPreviewSeen(Chat chat) {
        String another;
        if(chat.getUser2Id()==this.user.getId()) another=chat.getUser1Username();
        else another = chat.getUser2Username();
        System.out.println(ConsoleColors.BLUE+another+ConsoleColors.RESET);
    }

    private void printChatPreviewUnseen(Chat chat) {
        String another;
        if(chat.getUser2Id()==this.user.getId()) another=chat.getUser1Username();
        else another = chat.getUser2Username();
        if(chat.getUnseen(user.getId())!=1){
            System.out.println(ConsoleColors.BLUE+another+"    "+chat.getUnseen(user.getId())+" new messages"+ConsoleColors.RESET);
        }
        else System.out.println(ConsoleColors.BLUE+another+"    "+"new message"+ConsoleColors.RESET);
    }
}