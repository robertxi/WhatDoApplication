package hello;

import java.util.Date;

/**
 * Created by RobertXi on 9/30/15.
 */
public class ToDoListGet {

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    private String time="";
    private String item="";
    private String date="";
    private boolean complete=false;

}
