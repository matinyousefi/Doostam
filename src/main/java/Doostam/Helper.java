package Doostam;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

import static Doostam.ExceptionHandler.handle;

/*
this class does the help messages
 */
public class Helper {

    private static String location;
    private static final Logger logger = LogManager.getLogger(Signin.class);

    private static final String[] locations = new String[]{
            "signin",
            "signup",
            "Doostam:\\> ",
            "Doostam:\\My Page> ",
            "Doostam:\\Explore> ",
            "Doostam:\\My Page\\Followers> ",
            "Doostam:\\My Page\\Following> ",
            "Doostam:\\My Page\\Blocked> ",
            "Doostam:\\My Page\\Blocked> ",
            "Doostam:\\My Page\\Notifications> ",
            "Doostam:\\My Page\\Privacy Setting> ",
            "Doostam:\\My Page\\Profile Edit> ",
            "Doostam:\\My Page\\Me> ",
            "Doostam:\\Timeline> ",
            "Doostam:\\My Page\\Write> ",
            "Doostam:\\Chat Room> ",
            "Doostam:\\Users\\",
            "Doostam:\\Users\\\\Articles> ",
            "Doostam:\\Articles\\\\comments> ",
            "Doostam:\\Chat Room\\>",
            "Doostam:\\Articles\\>",
            "Doostam:\\Comments\\>"
    };

    public static void setLocation(String s){
        if(!Arrays.asList(locations).contains(s)){
            handle("Invalid location.");
            return;
        }
        Helper.location = s;
    }

    private static void print(String message){
        System.out.println(ConsoleColors.YELLOW +
                "help: lists possible syntaxes in your current location in app.\n" +
                "back: goes to the last location you were in the app. \n" +
                "logoff: you will be logged off.\n"  +
                "exit: exits the program.\n" +
                message
                + ConsoleColors.GREEN);
    }

    private static void printNaked(String message) {
        System.out.println(ConsoleColors.YELLOW +
                message
                + ConsoleColors.GREEN);
    }

    public static void help(){
        switch (location){
            case "signin":
                printNaked("Enter your username and password to login.");
                break;
            case "signup":
                print("Enter the information being asked, you can edit your personal information later in the program.");
                break;
            case "Doostam:\\> ":
                print("mypage: this is your page actions related to your account are designed here.\n" +
                        "timeline: the articles people you follow appear in your timeline.\n" +
                        "explore: you can explore articles you have not seen before in the explore page.\n" +
                        "chatroom: you can chat with other users in chatroom and all of your chats are archived in your chatroom.");
                break;
            case "articles options":
            case "Doostam:\\Explore> ":
            case "Doostam:\\My Page\\Me> ":
            case "Doostam:\\Users\\\\Articles>":
                print("print: prints all of the article previews again.\n" +
                        ConsoleColors.BLUE+"ARTICLE ID"+ConsoleColors.YELLOW+": goes to that article." +
                        " Note that you can only choose from the articles listed.");
                break;
            case "users options":
            case "Doostam:\\My Page\\Followers> ":
            case "Doostam:\\My Page\\Following> ":
            case "Doostam:\\My Page\\Blocked> ":
                print("print: prints the usernames of the user-list you are in.\n" +
                        "group: a process of making a group of contacts form the users listed will start.\n" +
                        ConsoleColors.BLUE+"USERNAME"+ConsoleColors.YELLOW+": enters the page of that user.");
                break;
            case "Doostam:\\My Page\\Notifications> ":
                print("print: print the notifications in your notification list.\n" +
                        "clearnotifications: clears your notification list.\n" +
                        "acceptallrequests: accepts all follow requests.\n" +
                        "reject "+ConsoleColors.BLUE+"USERNAME"+ConsoleColors.YELLOW+": rejects the follow request of that user.\n" +
                        "accept "+ConsoleColors.BLUE+"USERNAME"+ConsoleColors.YELLOW+": accepts the follow request of that user.\n" +
                        "rejectallrequests: rejects all follow requests.\n" +
                        "acceptallrequests: accepts all follow requests.");
                break;
            case "Doostam:\\My Page\\Privacy Setting> ":
                printNaked("Type y if your answer is yes and n if your answer is no.\n"+
                        "You will be redirected to your page when you have answered all 5 questions.");
                break;
            case "Doostam:\\My Page\\Profile Edit> ":
                printNaked("Enter the data asked."+
                        "You will be redirected to your page when you have answered all 5 questions.");
                break;
            case "Doostam:\\Timeline> ":
                print("cleartimeline: all the articles in your timeline will never appear in your timeline again.\n"+
                        "dislike"+ ConsoleColors.BLUE+"ARTICLE ID"+ConsoleColors.YELLOW+"    that article will never " +
                        "appear in your timeline again."+
                        "print: prints all of the articles again.\n" +
                        ConsoleColors.BLUE+"ARTICLE ID"+ConsoleColors.YELLOW+": prints that article." +
                        " Note that you can only choose from the articles listed.");
                break;
            case "Doostam:\\My Page\\Write> ":
                printNaked(
                        "Enter the required data for the article. You can abort the whole submission in the last step.\n"+
                        "search: using this syntax when you are asked to enter the area of your article you can see" +
                        "all the areas in which any article is written.\n" +
                        "search: using this syntax when you are asked to enter the field of your article you can see" +
                                " all the available fields.\n"+
                        "Note that the field of the article must be written in bold.");
                break;
            case "Doostam:\\Chat Room> ":
                print("listchats: lists all your chats.\n" +
                        "newchat: use this command when you want to chat with some user whose username you know.\n" +
                        ConsoleColors.BLUE+"USERNAME"+ConsoleColors.YELLOW+"    enters the chat with that user.");
                break;
            case "Doostam:\\Users\\":
                print("articles: prints all the articles of this user.\n" +
                        "follow: to start following this user.\n" +
                        "unfollow: to stop following this user.\n" +
                        "unblock: to unblock this user.\n" +
                        "block: to block this user. No article from a blocked user will appear in your timeline. " +
                        "This user's chat will no longer be in your chatroom.\n" +
                        "mute: to mute this user. No articles from a muted user will appear in your timeline.\n" +
                        "chat: to start a chat with this user.\n" +
                        "addtogroup: to add this user to one of your contacts group.");
                break;
            case "Doostam:\\Articles\\\\comments> ":
                print("print: prints all of comments again.\n" +
                        ConsoleColors.BLUE+"COMMENT ID"+ConsoleColors.YELLOW+": goes to that comment.");
                break;
            case "Doostam:\\Chat Room\\>":
                print("y: if you want to sent the message.\n" +
                        "n: if you do not want to send the message and write another message instead.");
                break;
            case "Doostam:\\Comments\\>":
                print("like: like or unlike.\n" +
                        "comment: to leave a comment.\n" +
                        "comments: see what other people commented about this.\n" +
                        "user: goes to the personal page of the user who wrote this.\n");
            case "Doostam:\\Articles\\>":
                print("forward: to forward this article to a user or a group of users (in their chat).\n");
                print("save: forwards this article to your saved messages (the chat with yourself in the chatroom).");
                break;
            case "Doostam:\\My Page> ":
                print("me: prints your personal page.\n" +
                        "write: to write a new article.\n" +
                        "privacy: to configure your privacy setting.\n" +
                        "editprofile: to edit your personal data.\n" +
                        "notifications: displays notifications about your account.\n" +
                        "followers: prints the list of all your followers.\n" +
                        "following: prints the list of users you follow.\n" +
                        "blocked: prints the list of users you have blocked.\n" +
                        "muted: prints the list of users you have muted.+\n" +
                        "deleteaccount: deletes your account.");
                break;
            default:
                logger.fatal("{Invalid location is given to helper so no help message can be produced.}"
                        +"{location: "+location+"}");
        }
    }
}
