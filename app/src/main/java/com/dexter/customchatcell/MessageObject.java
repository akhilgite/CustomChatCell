package com.dexter.customchatcell;

/**
 * Created by 10644291 on 06-10-2017.
 */

public class MessageObject {
    public interface SenderType{
        int SELF=0;
        int OTHERS=1;
    }

    public interface MediaType{

    }

    private String message;
    private int sender;

    public MessageObject(String message, int sender) {
        this.message = message;
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }
}
