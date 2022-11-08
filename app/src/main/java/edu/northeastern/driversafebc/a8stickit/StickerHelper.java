package edu.northeastern.driversafebc.a8stickit;

public class StickerHelper {
    String sender, receiver, id;

    public StickerHelper() {

    }

    public StickerHelper(String sender, String receiver, String id) {
        this.sender = sender;
        this.receiver = receiver;
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getId() {
        return id;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setId(String id) {
        this.id = id;
    }
}
