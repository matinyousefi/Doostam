package Doostam.ArticleAndComment;
import Doostam.InputOutput;

public class Subject {

    
    private final String subject;

    /*
     to capitalize the first letter of a word
    */
    private static String caps (String string){
        return string.toUpperCase().charAt(0)+string.toLowerCase().substring(1);
    }

    /*
    a subject is a string capitalized properly and consisting of only alphabets
     */
    public Subject(String subject) {
        String[] subjectWords = subject.split(" ");
        for (int i = 0; i < subjectWords.length ; i++) {
            subjectWords[i] = subjectWords[i].toLowerCase();
        }
        this.subject = subject;
    }

    public static boolean isValid(String subject) {
        if(subject==null || subject.equals("")) return false;
        subject=subject.toLowerCase();
        for (int i = 0; i < subject.length(); i++) {
            if((InputOutput.ALPHABET+" "+"'").indexOf(subject.charAt(i))==-1) return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return subject;
    }
}
