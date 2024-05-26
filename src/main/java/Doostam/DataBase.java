package Doostam;

import Doostam.ArticleAndComment.*;
import Doostam.ChatRoom.Chat;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class DataBase {
    private static File file;
    private static File usernames;
    private static File areas;
    private static File usersDirectory;
    private static File articlesDirectory;
    private static File commentsDirectory;
    private static File chatDirectory;
    private static File userIdFile;
    private static File commentIdFile;
    private static File articleIdFile;
    private static File chatIdFile;
    private static int userId;
    private static int articleId;
    private static int commentId;
    private static int chatId;
    private static TreeMap<String, User> usernameUserHashMap;
    private static TreeMap<Integer, User> idUserTreeMap;
    private static TreeMap<Integer, Article> idArticleTreeMap;
    private static TreeMap<Integer, Comment> idCommentTreeMap;
    private static TreeMap<Integer, Chat> idChatTreeMap;
    private static Gson gson;
    private static User currentUser;
    private static final Logger logger = LogManager.getLogger(DataBase.class);

    /*
    create or load necessary data
     */
    public static void load() {
        try {
            createFile();
            createUsernames();
            createAreas();
            loadAreas();
            createUsersDirectory();
            createCommentsDirectory();
            createArticlesDirectory();
            createChatDirectory();
            createUserIdFile();
            createCommentIdFile();
            createArticleIdFile();
            createChatIdFile();

            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setPrettyPrinting();
            gson = gsonBuilder.create();

            createIdUserTreeMap();
            createIdArticleTreeMap();
            createIdCommentTreeMap();
            createIdChatTreeMap();
        } catch (IOException e) {
            e.printStackTrace();
            ExceptionHandler.handle("Data Base can't load properly.");
        }
    }

    private static void createAreas() throws IOException {
        areas = new File(file + "\\areas.txt");
        if (!areas.exists()) areas.createNewFile();
    }

    private static void createIdChatTreeMap() throws IOException {
        idChatTreeMap = new TreeMap<>();
        if (chatDirectory.listFiles() != null) {
            for (File file :
                    chatDirectory.listFiles()) {
                String path = file.getPath();
                List<String> lines;
                lines = Files.readAllLines(Paths.get(path));
                StringBuilder s = new StringBuilder();
                for (String line :
                        lines) {
                    s.append(line);
                }
                Chat chat = gson.fromJson(s.toString(), Chat.class);
                idChatTreeMap.put(chat.getId(), chat);
                User user1 = getUser(chat.getUser1Id());
                User user2 = getUser(chat.getUser2Id());
                user1.getChats().add(chat.getId());
                user2.getChats().add(chat.getId());
            }
        }
    }

    private static void createChatIdFile() throws IOException {
        chatIdFile = new File(file + "\\" + "chatId.txt");
        if (!chatIdFile.exists()) {
            chatIdFile.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(chatIdFile);
            PrintStream printStream = new PrintStream(fileOutputStream);
            printStream.println(0);
            printStream.flush();
            printStream.close();
        } else {
            Scanner scanner = new Scanner(chatIdFile);
            chatId = scanner.nextInt();
        }
    }

    private static void createChatDirectory() {
        chatDirectory = new File(file + "\\chats");
        if (!chatDirectory.exists()) chatDirectory.mkdir();
    }

    private static void createIdCommentTreeMap() throws IOException {
        idCommentTreeMap = new TreeMap<>();
        if (commentsDirectory.listFiles() != null) {
            for (File file :
                    commentsDirectory.listFiles()) {
                String path = file.getPath();
                List<String> lines;
                lines = Files.readAllLines(Paths.get(path));
                StringBuilder s = new StringBuilder();
                for (String line :
                        lines) {
                    s.append(line);
                }
                Comment comment = gson.fromJson(s.toString(), Comment.class);
                idCommentTreeMap.put(comment.getId(), comment);
            }
        }

    }

    private static void createIdArticleTreeMap() throws IOException {
        idArticleTreeMap = new TreeMap<>();
        if (articlesDirectory.listFiles() != null) {
            for (File file :
                    articlesDirectory.listFiles()) {
                String path = file.getPath();
                List<String> lines;
                lines = Files.readAllLines(Paths.get(path));
                StringBuilder s = new StringBuilder();
                for (String line :
                        lines) {
                    s.append(line);
                }
                Article article = gson.fromJson(s.toString(), Article.class);
                idArticleTreeMap.put(article.getId(), article);
            }
        }
    }

    private static void createIdUserTreeMap() throws IOException {
        idUserTreeMap = new TreeMap<>();
        usernameUserHashMap = new TreeMap<>();
        if (usersDirectory.listFiles() != null) {
            for (File file :
                    usersDirectory.listFiles()) {
                String path = file.getPath();
                List<String> lines;
                lines = Files.readAllLines(Paths.get(path));
                StringBuilder s = new StringBuilder();
                for (String line :
                        lines) {
                    s.append(line);
                }
                User user = gson.fromJson(s.toString(), User.class);
                idUserTreeMap.put(user.getId(), user);
                usernameUserHashMap.put(user.getUsername(), user);
            }
        }
    }

    private static void createArticleIdFile() throws IOException {
        articleIdFile = new File(file + "\\" + "articleId.txt");
        if (!articleIdFile.exists()) {
            articleIdFile.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(articleIdFile);
            PrintStream printStream = new PrintStream(fileOutputStream);
            printStream.println(0);
            printStream.flush();
            printStream.close();
        } else {
            Scanner scanner = new Scanner(articleIdFile);
            articleId = scanner.nextInt();
        }
    }

    private static void createCommentIdFile() throws IOException {
        commentIdFile = new File(file + "\\" + "commentId.txt");
        if (!commentIdFile.exists()) {
            commentIdFile.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(commentIdFile);
            PrintStream printStream = new PrintStream(fileOutputStream);
            printStream.println(0);
            printStream.flush();
            printStream.close();
        } else {
            Scanner scanner = new Scanner(commentIdFile);
            commentId = scanner.nextInt();
        }

    }

    private static void createUserIdFile() throws IOException {
        userIdFile = new File(file + "\\" + "userId.txt");
        if (!userIdFile.exists()) {
            userIdFile.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(userIdFile);
            PrintStream printStream = new PrintStream(fileOutputStream);
            printStream.println(0);
            printStream.flush();
            printStream.close();
        } else {
            Scanner scanner = new Scanner(userIdFile);
            userId = scanner.nextInt();
        }
    }

    private static void createArticlesDirectory() {
        articlesDirectory = new File(file + "\\articles");
        if (!articlesDirectory.exists()) articlesDirectory.mkdir();
    }

    private static void createCommentsDirectory() {
        commentsDirectory = new File(file + "\\comments");
        if (!commentsDirectory.exists()) commentsDirectory.mkdir();
    }

    private static void createUsersDirectory() {
        usersDirectory = new File(file + "\\users");
        if (!usersDirectory.exists()) usersDirectory.mkdir();
    }

    private static void createUsernames() throws IOException {
        usernames = new File(file + "\\usernames.txt");
        if (!usernames.exists()) usernames.createNewFile();
    }

    private static void createFile() {
        file = new File("DataBase");
        if (!file.exists()) file.mkdir();
    }

    public static int newCommentId() {
        try {
            commentId++;
            FileOutputStream fileOutputStream = new FileOutputStream(commentIdFile, false);
            PrintStream printStream = new PrintStream(fileOutputStream);
            printStream.println(commentId);
            printStream.flush();
            printStream.close();
        } catch (IOException e) {
            logger.fatal("{IOException writing to commentIdFile.}");
        }
        return commentId;
    }

    public static int newArticleId() {
        try {
            articleId++;
            FileOutputStream fileOutputStream = new FileOutputStream(articleIdFile, false);
            PrintStream printStream = new PrintStream(fileOutputStream);
            printStream.println(articleId);
            printStream.flush();
            printStream.close();
            logger.info(currentUser.getUsername()+" wrote an article");
        } catch (IOException e) {
            logger.fatal("{IOException writing to articleIdFile.}");
        }
        return articleId;
    }

    public static int newUserId() {
        try {
            userId++;
            FileOutputStream fileOutputStream = new FileOutputStream(userIdFile, false);
            PrintStream printStream = new PrintStream(fileOutputStream);
            printStream.println(userId);
            printStream.flush();
            printStream.close();
        } catch (IOException e) {
            logger.fatal("{IOException writing to userIdFile.}");
        }
        return userId;
    }

    public static int newChatId() {
        try {
            chatId++;
            FileOutputStream fileOutputStream = new FileOutputStream(chatIdFile, false);
            PrintStream printStream = new PrintStream(fileOutputStream);
            printStream.println(chatId);
            printStream.flush();
            printStream.close();
        } catch (IOException e) {
            logger.fatal("{IOException writing to chatIdFile.}");
        }
        return chatId;
    }

    /*
    this method saves/updates user date
     */
    public static void updateUser(User user) {
        try {
            if (!idUserTreeMap.containsKey(user.getId())) idUserTreeMap.put(user.getId(), user);
            // my friend Sina helped me with gson syntheses
            File file = new File(usersDirectory + "\\" + user.getUsername() + ".json");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter;
            fileWriter = new FileWriter(file, false);
            gson.toJson(user, fileWriter);
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateArticle(Article article) {
        try {
            if (!idArticleTreeMap.containsKey(article.getId())) idArticleTreeMap.put(article.getId(), article);
            File file = new File(articlesDirectory + "\\" + article.getId() + ".json");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter;
            fileWriter = new FileWriter(file, false);
            gson.toJson(article, fileWriter);
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addUsername(String username) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(usernames, true);
            PrintStream printStream = new PrintStream(fileOutputStream);
            printStream.println(username);
            printStream.flush();
            printStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    this method loads User and returns null if not found
     */
    public static User loadUser(String username, String password) throws Exception {
        String path = usersDirectory + "\\" + username + ".json";
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            logger.error("{Username not found.}"+" {username: "+username+"}");
            throw new Exception("User not found.");
        }
        StringBuilder s = new StringBuilder();
        for (String line :
                lines) {
            s.append(line);
        }
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        User user = gson.fromJson(s.toString(), User.class);
        if (user.isPassword(password)) {
            return user;
        } else {
            logger.error("{Wrong password.} "+"{username: "+username+"}");
            throw new Exception("Wrong Password.");
        }
    }

    public static void usernameIsNew(String username) throws Exception {
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get(usernames.getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String s :
                lines) {
            if (s.equalsIgnoreCase(username)) {
                logger.info("{Username already taken.} "+"{username: "+username+"}");
                throw new Exception("Username already taken.");
            }
        }
    }

    public static User getUser(int id) {
        if (idUserTreeMap.containsKey(id)) return idUserTreeMap.get(id);
        else {
            logger.fatal("{Invalid User ID: "+id+"} {CurrentUser: "+currentUser.getId()+"}");
            return null;
        }
    }

    public static HashSet<User> getUsers(HashSet<Integer> input) {
        HashSet<User> output = new HashSet<>();
        for (int id :
                input) {
            output.add(getUser(id));
        }
        return output;
    }

    public static Article getArticle(int id) {
        if (idArticleTreeMap.containsKey(id)) return idArticleTreeMap.get(id);
        else {
            logger.fatal("{Invalid Article ID: "+id+"} {CurrentUser: "+currentUser.getId()+"}");
            return null;
        }
    }

    public static Comment getComment(int id) {
        if (idCommentTreeMap.containsKey(id)) return idCommentTreeMap.get(id);
        else {
            logger.fatal("{Invalid Comment ID: "+id+"} {CurrentUser: "+currentUser.getId()+"}");
            return null;
        }
    }

    public static User getUser(String username) throws Exception {
        if (usernameUserHashMap.containsKey(username)) return usernameUserHashMap.get(username);
        else {
            logger.error("{Username Not Found: "+username+"} {CurrentUser: "+currentUser.getId()+"}");
            throw new Exception("Username Not Found.");
        }
    }

    public static HashSet<Chat> getChats(HashSet<Integer> input) {
        HashSet<Chat> output = new HashSet<>();
        for (Integer integer :
                input) {
            if (idChatTreeMap.containsKey(integer)) {
                output.add(idChatTreeMap.get(integer));
            } else {
                logger.fatal("{Invalid Chat ID: "+integer+"} {CurrentUser: "+currentUser.getId()+"}");
            }
        }
        return output;

    }

    public static void updateChat(Chat chat) {
        try {
            if (!idChatTreeMap.containsKey(chat.getId())) idChatTreeMap.put(chat.getId(), chat);
            File file = new File(chatDirectory + "\\" + chat.getId() + ".json");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter;
            fileWriter = new FileWriter(file, false);
            gson.toJson(chat, fileWriter);
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            logger.fatal("{IOException writing to ChatDirectory}" + " {Chat: " + chat.getId() + "}");
        }
    }

    public static void canChat(User user, User anotherUser) throws Exception {
        if (user.getId() == anotherUser.getId()) return;
        for (Chat chat :
                getChats(user.getChats())) {
            if (chat.getUser1Id() == anotherUser.getId() ||
                    chat.getUser2Id() == anotherUser.getId()) {
                throw new Exception("You already have an ongoing chat with this user.");
            }
        }
    }

    public static boolean hasChat(User user, User anotherUser) {
        if (user.getId() == anotherUser.getId()) {
            return true;
        }
        for (Chat chat :
                getChats(user.getChats())) {
            if (chat.getUser1Id() == anotherUser.getId() ||
                    chat.getUser2Id() == anotherUser.getId()) {
                return true;
            }
        }
        return false;
    }

    private static void loadAreas() {
        try {
            for (String line :
                    Files.readAllLines(Paths.get(areas.getPath()))) {
                new Area(line);
            }
        } catch (IOException e) {
            logger.fatal("{IOException loading areas File.}");
        }
    }

    public static void addArea(Area area) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(areas, true);
            PrintStream printStream = new PrintStream(fileOutputStream);
            printStream.println(area.toString());
            printStream.flush();
            printStream.close();
        } catch (IOException e) {
            logger.fatal("{IOException writing to areas File.}");
        }
    }

    public static void updateArticleOrComment(ArticleOrComment articleOrComment) {
        if (articleOrComment instanceof Article) updateArticle((Article) articleOrComment);
        if (articleOrComment instanceof Comment) updateComment((Comment) articleOrComment);
    }

    public static void updateComment(Comment comment) {
        try {
            if (!idCommentTreeMap.containsKey(comment.getId())) idCommentTreeMap.put(comment.getId(), comment);
            File file = new File(commentsDirectory + "\\" + comment.getId() + ".json");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter;
            fileWriter = new FileWriter(file, false);
            gson.toJson(comment, fileWriter);
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            logger.fatal("{IOException updating comment file.}"+" {Comment: "+comment.getId()+"}");
        }
    }

    public static Chat getChat(User user2, User user1) throws Exception {
        if (user1.getId() == user2.getId()) {
            return user1.getSavedMessages();
        }
        for (Chat chat :
                getChats(user1.getChats())) {
            if (chat.getUser2Id() == user2.getId() ||
                    chat.getUser1Id() == user2.getId()) {
                return chat;
            }
        }
        logger.error("{Chat not found.}"+" {ChatBetweenUsers: "+user1.getId()+","+user2.getId()+"}");
        throw new Exception("Chat not found.");
    }

    public static HashSet<Article> getArticles(HashSet<Integer> ids) {
        HashSet<Article> articles = new HashSet<>();
        for (int id :
                ids) {
            articles.add(getArticle(id));
        }
        return articles;
    }

    public static void updateUser(int id) {
        updateUser(getUser(id));
    }

    public static HashSet<Integer> exploreArticles(User user) {
        HashSet<Integer> output = new HashSet<>();
        for (Article article :
                idArticleTreeMap.values()) {
            if ((getUser(article.getUserId())).isPublic())
                if (!article.getSeen().contains(user.getId()))
                    output.add(article.getId());
        }
            return output;
    }


    public static void save(){
        currentUser.setLastOnline();
        updateUser(currentUser);
    }

    public static void setCurrentUser(User currentUser) {
        DataBase.currentUser = currentUser;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void deleteUser(User user) {
        try {
        for (User eachUser :
                idUserTreeMap.values()) {
            eachUser.erase(user);
            updateUser(eachUser);
        }
        usernameUserHashMap.remove(user.getUsername());
        FileOutputStream fileOutputStream = new FileOutputStream(usernames, false);
        PrintStream printStream = new PrintStream(fileOutputStream);
        for (String username :
                usernameUserHashMap.keySet()) {
            printStream.println(username);
        }
        File userFile = new File(usersDirectory + "\\" + user.getUsername() + ".json");
        userFile.delete();
        load();
        } catch (FileNotFoundException ignored) {
            logger.fatal("{Can not rewrite on usernames file.}");
        }
    }
}

