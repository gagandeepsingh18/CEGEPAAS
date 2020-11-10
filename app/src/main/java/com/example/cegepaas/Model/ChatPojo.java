package com.example.cegepaas.Model;

/**
 * Chat POJO Class
 */
public class ChatPojo {
    String sender;
    String receiver;
    String message;

    /**
     * Parameterized Constructor
     * @param sender : sender
     * @param receiver : receiver
     * @param message : message
     */
    public ChatPojo(String sender, String receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    public ChatPojo() {
    }

    /**
     * get Sender
     * @return : sender
     */
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * get receiver
     * @return : receiver
     */
    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    /**
     * get message
     * @return : message
     */
    public String getMessage() {
        return message;
    }

    /**
     * set message
     * @param message : message
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
