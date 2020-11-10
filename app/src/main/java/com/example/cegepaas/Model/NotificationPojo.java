package com.example.cegepaas.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Notification Class POJO
 */
public class NotificationPojo {

    /**
     * get Id
     * @return : Id
     */
    public String getId() {
        return id;
    }

    /**
     * set Id
     * @param id : id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * get Message
     * @return : message
     */
    public String getMsg() {
        return msg;
    }


    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * get Title
     * @return : title
     */
    public String getTitle() {
        return title;
    }

    /**
     * set Title
     * @param title : title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * get UName
     * @return : UName
     */
    public String getUname() {
        return uname;
    }

    /**
     * Set name
     * @param uname : uname
     */
    public void setUname(String uname) {
        this.uname = uname;
    }

    @SerializedName("id")
    private String id;

    @SerializedName("msg")
    private String msg;

    @SerializedName("title")
    private String title;

    @SerializedName("uname")
    private String uname;


}