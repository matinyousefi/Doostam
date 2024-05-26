package Doostam.ArticleAndComment;

import Doostam.User;

import java.util.HashSet;

public class Article extends ArticleOrComment {
    private final String body;
    private final Subject subject;
    private final String abs; //abstract
    private final Field field;
    private final Area area;
    private final HashSet<Integer> seen;

    public Article(Subject subject, int user, String abs, Field field, Area area, String body, int id) {
        super(user, id);
        this.body = body;
        this.subject = subject;
        this.abs = abs;
        this.field = field;
        this.area = area;
        this.seen = new HashSet<>();
        seen.add(user);
    }

    public static boolean isValidAbstract(String s) {
        int length = s.split(" ").length;
        return length >= 5 && length <= 100;
    }

    public String getBody() {
        return body;
    }

    public Subject getSubject() {
        return subject;
    }

    public String getAbs() {
        return abs;
    }

    public Field getField() {
        return field;
    }

    public Area getArea() {
        return area;
    }

    public void addSeen(User user) {
        this.seen.add(user.getId());
    }

    public HashSet<Integer> getSeen() {
        return this.seen;
    }
}
