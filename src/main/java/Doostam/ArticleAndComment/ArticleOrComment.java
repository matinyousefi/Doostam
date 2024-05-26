package Doostam.ArticleAndComment;

import Doostam.User;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

public abstract class ArticleOrComment {
    private final HashSet<Integer> liked;
    private final HashSet<Integer> comments;
    private final int userId;
    private final Date date;
    private final int id;

    public Date getDate() {
        return date;
    }

    public ArticleOrComment(int user, int id) {
        this.id = id;
        this.userId = user;
        this.date = Calendar.getInstance().getTime();
        this.liked = new HashSet<>();
        this.comments = new HashSet<>();
    }


    /*
    liking something
     */
    public boolean like(User user){
        if(liked.contains(user.getId())){
            /*
            arraylist.remove() has two remove methods one with Object argument one with int argument which removes specific index
             */
            liked.remove((Object) user.getId());
            return false;
        }
        else{
            liked.add(user.getId());
            return true;
        }
    }

    public HashSet<Integer> getLiked() {
        return liked;
    }

    public int getUserId() {
        return userId;
    }

    public HashSet<Integer> getComments() {
        return comments;
    }

    public boolean hasComment(){
        return comments.size() != 0;
    }

    public int commentCount(){return comments.size();}

    public int getId(){
        return this.id;
    }

    public void addComment(Comment comment){
        this.comments.add(comment.getId());
    };

}
