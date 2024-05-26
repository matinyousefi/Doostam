package Doostam;

import java.util.Arrays;

public class ExceptionHandler{

    public static void handle(Exception e){
        if(e.getMessage().endsWith(".")){
            System.out.println(ConsoleColors.RED+e.getMessage().substring(0,e.getMessage().length()-1).toUpperCase()+ConsoleColors.RESET);
        }
        else{
            System.out.println(ConsoleColors.RED+e.getMessage().toUpperCase()+ConsoleColors.RESET);
        }
    }

    public static void handle(String s) {
        handle(new Exception(s));
    }

    public static void invalidSyntax() {
        handle("Invalid Syntax");
        Helper.help();
    }

}
