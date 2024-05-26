package Doostam.ArticleAndComment;

public class Comment extends ArticleOrComment {
    private final String string;
    public Comment(int id,int user, String string) {
        super(user, id);
        this.string=string;
    }

    public String getText() {
        return string;
    }
}
