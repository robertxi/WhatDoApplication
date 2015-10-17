package hello;

/**
 * Created by RobertXi on 10/5/15.
 */
public class Comment {

    private int id;
    private int taskitem_id;
    private String content;
    private String date_created;
    private int user_id;

    public int getUser_id() {return user_id;}

    public void setUser_id(int user_id) {this.user_id = user_id;}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTaskitem_id() {return taskitem_id;}

    public void setTaskitem_id(int taskitem_id) {
        this.taskitem_id = taskitem_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate_created() {return date_created;}

    public void setDate_created(String date_create) {this.date_created = date_create;}

}
