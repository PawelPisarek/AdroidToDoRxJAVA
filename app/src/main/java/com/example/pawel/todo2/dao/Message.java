package com.example.pawel.todo2.dao;

public class Message extends MessageNewDao {
    private String id;

    public Message(String sender, String content, String receiver, String id) {
        super(sender, content, receiver);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
