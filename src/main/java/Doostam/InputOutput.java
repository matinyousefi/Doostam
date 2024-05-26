package Doostam;

import Doostam.ArticleAndComment.*;
import Doostam.ChatRoom.Chat;
import Doostam.ChatRoom.Message;

import java.text.SimpleDateFormat;
import java.util.*;

/*
 this class handles getting an input from the user
 */
public class InputOutput {

    public final static String ALPHABET = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
    public final static String NUMBERS = "0123456789";
    public final static String ALPHANDNUM = ALPHABET + NUMBERS;
    public final static Scanner scanner = new Scanner(System.in);
    private final static Throwable back = new Throwable();

    public static String line(String message)  {
        System.out.print(ConsoleColors.RESET + message + ConsoleColors.GREEN);
        String line = scanner.nextLine();
        if (line.equals("help")) {
            Helper.help();
            System.out.print(ConsoleColors.RESET + message + ConsoleColors.GREEN);
            line = scanner.nextLine();
        }
        if (line.equals("exit")){
            DataBase.save();
            System.exit(0);
        }
        if (line.equals("logoff")){
            //https://stackoverflow.com/questions/1567979/how-to-free-memory-in-java#:~:text=Java%20uses%20managed%20memory%2C%20so,relying%20on%20the%20garbage%20collector.
            System.gc();
            DataBase.load();
            Signin.login();
        }
        return line;
    }

    public static String line() {
        String line = scanner.nextLine();
        if (line.equals("help")) {
            Helper.help();
            line = scanner.nextLine();
        }
        if (line.equals("exit")){
            DataBase.save();
        }
        if (line.equals("logoff")){
            System.gc();
            DataBase.load();
            Signin.login();
        }
        return line;
    }

    public static String text(String message) {
        print(message);
        StringBuilder output = new StringBuilder();
        String line;
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            if (line.equals("")) break;
            output.append(line).append("\n");
        }
        String finalOutput = null;
        try {
            finalOutput = output.substring(0, output.lastIndexOf("\n"));
        } catch (Exception exception) {
            ExceptionHandler.handle("CANNOT BE EMPTY");
            return text(message);
        }
        try {
            if(yn("Submit?")) return finalOutput;
            else return text(message);
        } catch (Throwable throwable) {
            return text(message);
        }
    }

    public static String text() {
        StringBuilder output = new StringBuilder();
        String line;
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            if (line.equals("")) break;
            output.append(line).append("\n");
        }
        String finalOutput = null;
        try {
            finalOutput = output.substring(0, output.lastIndexOf("\n"));
        } catch (Exception exception) {
            ExceptionHandler.handle("CANNOT BE EMPTY");
            return text();
        }
        try {
            if(yn("Submit?")) return finalOutput;
            else return text();
        } catch (Throwable throwable) {
            return text();
        }
    }

    public static boolean yn(String message)  {
        String string = InputOutput.line(message+" (y/n) ");
        if (string.equals("y")) return true;
        else if (string.equals("n")) return false;
        else return yn(message);
    }

    public static char ug(String message)  {
        String string = InputOutput.line(message+" (u/g) ");
        if (string.equals("u")) return 'u';
        else if (string.equals("g")) return 'g';
        else return ug(message);
    }

    public static void print(String s){
        System.out.println(ConsoleColors.RESET+s+ConsoleColors.GREEN);
    }

    public static void printGray(String s){
        print(ConsoleColors.WHITE+s);
    }

    public static void printConditional(String s, boolean b) {
        if(b) print(s);
        else  printGray(s);
    }

    public static void printInLine(String s) {
        System.out.print(ConsoleColors.RESET+s+" "+ConsoleColors.GREEN);
    }

    public static boolean isNumber(String s){
        if(s==null || s.length()==0) return false;
        for (int i = 0; i < s.length(); i++) {
            if (NUMBERS.indexOf(s.charAt(i)) == -1) {
                return false;
            }
        }
        return true;
    }

    public static String inputBody() {
        return text("Body: ");
    }

    public static Area inputArea()  {
        String area = line("Area: ");
        if(area.equals("search")){
            print(Area.getAll().toString());
            area = line("Area: ");
        }
        return new Area(area);
    }

    public static Field inputField()  {
        String field = line("FIELD: ");
        if(field.equals("search")){
            for (Field field1 :
                    Field.values()) {
                print(field1.toString());
            }
            return inputField();
        } else if (field != field.toUpperCase()) {
            ExceptionHandler.handle("Fields are written in uppercase only.");
            return inputField();
        } else {
            try {
                return Field.valueOf(field);
            } catch (IllegalArgumentException e) {
                ExceptionHandler.handle(e);
                return inputField();
            }
        }
    }

    public static String inputAbs() {
        try {
            String abs = text("Abstract: ");
            if (Article.isValidAbstract(abs)) return abs;
            else {
                throw new Exception("Invalid Abstract.");
            }
        }
        catch (Exception e){
            ExceptionHandler.handle(e);
            return inputAbs();
        }
    }

    public static Subject inputSubject() {
        try {
            String string = line("Subject: ");
            if (Subject.isValid(string)) return new Subject(string);
            else {
                throw new Exception("Invalid Subject.");
            }
        }
        catch (Exception e){
            ExceptionHandler.handle("Invalid Subject.");
            return inputSubject();
        }
    }

    public static void printArticlePreview(Article article){
        System.out.println(ConsoleColors.BLUE_BOLD + article.getId() + " " + ConsoleColors.RESET + DataBase.getUser(article.getUserId()).getSignature() + ": " + article.getSubject().toString() +
                "\nAbstract: " + article.getAbs() + "\n" + article.getLiked().size() + " likes    " + article.getComments().size() + " comments");

    }

    public static void printChat(Chat chat) {
        Date date = null;
        String pattern = "dd MMMM";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        for (Message message :
                chat.getMessages()) {
            String messageDate = simpleDateFormat.format(message.getDate());
            if(date== null) {
                if(messageDate.equals(simpleDateFormat.format(Calendar.getInstance().getTime()))) {
                    print("Today");
                }
                else {
                    print(simpleDateFormat.format(message.getDate()));
                }
                date = message.getDate();
            }
            else if(!simpleDateFormat.format(date).equals(simpleDateFormat.format(message.getDate()))){
                if(messageDate.equals(simpleDateFormat.format(Calendar.getInstance().getTime()))) {
                    print("Today");
                }
                else {
                    print(simpleDateFormat.format(message.getDate()));
                }
                date = message.getDate();
            }
            printMessage(message);
        }
    }

    public static void printMessage(Message message) {
        String pattern = "hh:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        System.out.println(ConsoleColors.RESET+simpleDateFormat.format(message.getDate())+" "+message.getUsername()+": "+ConsoleColors.GREEN+message.getText());
    }

    public static void printComments(ArticleOrComment article) {
        for (int i :
                article.getComments()) {
            printComment(DataBase.getComment(i));
        }
    }

    public static void printComment(Comment comment) {
        System.out.println(ConsoleColors.BLUE_BOLD+comment.getId()+" "
                +ConsoleColors.RESET+ DataBase.getUser(comment.getUserId()).getSignature()+": "
                +"\n"+comment.getText()+
                "\n"+comment.getLiked().size()+" likes    "+comment.getComments().size()+" comments"+ConsoleColors.GREEN);
    }

    public static User enterUsername() throws Throwable {
        try {
            String username = line();
            if(username.equals("back"))
                throw back;
            return DataBase.getUser(username);
        } catch (Exception e) {
            ExceptionHandler.handle(e);
            return enterUsername();
        }
    }

    public static String stringIt(Article article) {
        return ConsoleColors.RESET+
                DataBase.getUser(article.getUserId()).getSignature()+": "+
                article.getSubject().toString()+
                "\nAbstract: "+article.getAbs()+
                "\nField: "+article.getField().toString()+
                "\nArea: "+article.getArea().toString()
                +"\n"+article.getBody()
                +"\n"+article.getLiked().size()+" likes    "+article.getComments().size()+" comments"+ConsoleColors.GREEN;
    }

    public static void printArticle(Article article){
        System.out.println(ConsoleColors.BLUE_BOLD+article.getId()+" "+ConsoleColors.RESET+
                DataBase.getUser(article.getUserId()).getSignature()+": "+
                article.getSubject().toString()+
                "\nAbstract: "+article.getAbs()+
                "\nField: "+article.getField().toString()+
                "\nArea: "+article.getArea().toString()
                +"\n"+article.getBody()
                +"\n"+article.getLiked().size()+" likes    "+article.getComments().size()+" comments"+ConsoleColors.GREEN);
    }

    public static void printUsers(HashSet<Integer> users) throws Throwable {
        if(users.size()==0){
            print("No such users.");
            throw back;
        }
        HashSet<User> usersAsUser = DataBase.getUsers(users);
        for (User user :
                usersAsUser) {
            print(ConsoleColors.BLUE + user.getUsername());
        }
    }
}

