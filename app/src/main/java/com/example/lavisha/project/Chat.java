package com.example.lavisha.project;

public class Chat {

    private String Sender;
    private String Receiver;

    public Chat(String sender, String receiver, String message, Boolean isSeen) {
        Sender = sender;
        Receiver = receiver;
        Message = message;
        this.isSeen = isSeen;
    }

    public Boolean getisSeen() {
        return isSeen;
    }

    public void setisSeen(Boolean seen) {
        isSeen = seen;
    }

    private String Message;
    private Boolean isSeen;

    public Chat() {
    }


    public String getSender() {
        return Sender;
    }

    public void setSender(String Sender) {
        this.Sender = Sender;
    }

    public String getReceiver() {
        return Receiver;
    }

    public void setReceiver(String Receiver) {
        this.Receiver = Receiver;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public Chat(String Sender, String Receiver, String Message) {

        this.Sender = Sender;
        this.Receiver = Receiver;
        this.Message = Message;
    }
}
