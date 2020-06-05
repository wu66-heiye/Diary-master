package android.mainactivity;

import org.litepal.crud.DataSupport;

/**
 * Created by 15711 on 2018/10/16.
 */

public class diary extends DataSupport{
    private String content;
    private String time;
    private String username;

    public diary(String content, String time) {
        this.content = content;
        this.time = time;
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){
        return username;
    }

}
