package Doostam;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import static Doostam.InputOutput.*;

public class Signin {
    private static final Logger logger = LogManager.getLogger(Signin.class);

    public static void login()  {
        Helper.setLocation("signin");
        print("Enter your username and password to login; or type signup to create an account.");
        String username = line("Username: ");
        if(username.equals("signup")) signup();
        else {
            String password = line("Password: ");
            try {
                User user = DataBase.loadUser(username, password);
                DataBase.setCurrentUser(user);
                new Doostam(user);
            } catch (Exception e){
                ExceptionHandler.handle(e);
            } catch (Throwable back){}
        }
        login();
    }

    /*
    redesigned from Input to be able to abort the signup process in the middle using "back"
    this method must remain private so the coincidence of it's signature with Input.line(String) does not cause problem
     */
    private static String line(String message) {
        System.out.print(ConsoleColors.RESET + message + ConsoleColors.GREEN);
        String line = scanner.nextLine();
        if (line.equals("help")) {
            Helper.help();
            System.out.print(ConsoleColors.RESET + message + ConsoleColors.GREEN);
            line = scanner.nextLine();
        }
        if (line.equals("back")) login();
        return line;
    }

    public static void signup()  {
        Helper.setLocation("signup");
        print("The information you are about to enter are necessary for your signup. Note that your name, username and biography will be seen by other users other information but these can be modified to be public or not anytime.");
        String name = inputName();
        String username = inputUsername();
        String password = inputPassword();
        String telephoneNumber = inputTelephoneNumber();
        String email = inputEmail();
        Date birthday = inputBirthday();
        String biography = inputBiography();
        User user = new User(DataBase.newUserId(), password, name, username, birthday, email, telephoneNumber, biography);
        user.setLastOnline();
        DataBase.addUsername(username);
        logger.info("{Signed up.} "+"{user: "+user.getId()+"}");
        DataBase.updateUser(user);
        line("Your signup was successful. Press enter to be redirected to login page...");
    }

    public static String inputBiography() {
        return text("Enter a brief biography of yourself: ");
    }

    public static Date inputBirthday() {
        String birthday = line("Birthday (yyyy mm dd): ");
        //https://stackoverflow.com/questions/2735023/convert-string-to-java-util-date
        DateFormat formatter = new SimpleDateFormat("yyyy MM dd");
        try {
            return formatter.parse(birthday);
        } catch (Exception e) {
            ExceptionHandler.handle("Invalid Birthday.");
            return inputBirthday();
        }
    }

    public static String inputEmail() {
        try {
            String email = line("Email: ");
            checkEmail(email);
            return email;
        }
        catch (Exception e){
            ExceptionHandler.handle(e);
            return inputEmail();
        }
    }

    public static void checkEmail(String email) throws Exception {
        Exception invalidEmail = new Exception("Invalid email.");
        for (int i = 0; i < email.length(); i++) {
            if((ALPHANDNUM+"."+"@").indexOf(email.charAt(i))==-1){
                throw invalidEmail;
            }
        }
        boolean condition2 = email.indexOf('@') == email.lastIndexOf('@');
        boolean condition3 = email.indexOf('@') != -1;
        boolean condition4 = email.indexOf('.') != -1;
        boolean condition5 = email.endsWith(".com");
        if(!(condition2 && condition3 && condition4 && condition5)) throw invalidEmail;
    }

    public static String inputTelephoneNumber() {
        try {
            String telephoneNumber = line("Enter your telephone number including your country's prefix which starts with '+'.\nTelephone number: ");
            checkTelephoneNumber(telephoneNumber);
            return telephoneNumber;
        }
        catch (Exception e){
            ExceptionHandler.handle(e);
            return inputTelephoneNumber();
        }
    }

    public static void checkTelephoneNumber(String telephoneNumber) throws Exception {
        try{
            if(telephoneNumber.charAt(0)!='+') throw new Exception("Starting with '+'.");
            telephoneNumber = telephoneNumber.substring(1);
        } catch (IndexOutOfBoundsException e){
            throw new Exception("It's Empty.");
        }
        if(telephoneNumber.equals("")) throw new Exception("Invalid telephone number.");
        for (int i = 1; i < telephoneNumber.length(); i++) {
            if(NUMBERS.indexOf(telephoneNumber.charAt(i))==-1){
                throw new Exception("Invalid telephone number.");
            }
        }
    }

    public static String inputPassword() {
        try {
            String password = line("Password: ");
            checkPassword(password);
            return password;
        }
        catch (Exception e){
            ExceptionHandler.handle(e);
            return inputPassword();
        }
    }

    public static void checkPassword(String password) throws Exception {
        for (int i = 0; i < password.length(); i++) {
            if(ALPHANDNUM.indexOf(password.charAt(i))==-1){
                throw new Exception("Alphabets and numbers only");
            }
        }
        if(password.length()<5) throw new Exception("Too short.");
    }

    public static String inputUsername() {
        try {
            String username = line("Username: ");
            checkUsername(username);
            return username;
        } catch (Exception e){
            ExceptionHandler.handle(e);
            return inputUsername();
        }
    }

    public static void checkUsername(String username) throws Exception {
        for (int i = 0; i < username.length(); i++) {
            if(ALPHANDNUM.indexOf(username.charAt(i))==-1){
                throw new Exception("Usernames can only consist of alphabet and numbers.");
            }
        }
        DataBase.usernameIsNew(username);
        String[] doostamSyntaxes = new String[]{"back", "newchat", "exit", "signup", "group", "logoff",
                "help", "group", "print", "listchats"};
        if(Arrays.asList(doostamSyntaxes).contains(username)){
            throw new Exception("The chosen username coincides with Doostam syntaxes.");
        }
        if(username.length()<5) throw new Exception("Too short.");
        if(username.length()>20) throw  new Exception("Too long.");
    }

    public static String inputName() {
        try {
            String name = line("Full name: ");
            checkName(name);
            return name;
        }
        catch (Exception e){
            ExceptionHandler.handle(e);
            return inputName();
        }
    }

    public static void checkName(String name) throws Exception {
        if(!(name.split(" ").length==2 || name.split(" ").length==3)){
            throw new Exception("Enter a name consisting of two or three parts.");
        }
        for (int i = 0; i < name.length(); i++) {
            if((ALPHABET+" ").indexOf(name.charAt(i))==-1){
                throw new Exception("English alphabet only.");
            }
        }
        for (String s:
             name.split(" ")) {
            if (s.length() == 0) {
                throw new Exception("Be careful with spaces.");
            }
        }
    }


}