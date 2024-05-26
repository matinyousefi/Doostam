import Doostam.ConsoleColors;
import Doostam.DataBase;

import static Doostam.Signin.login;

public class Main {
    public static void main(String[] args) {
        System.out.println(ConsoleColors.YELLOW+"Welcome to Doostam."+
                ConsoleColors.RESET);
        DataBase.load();
        login();
    }
}