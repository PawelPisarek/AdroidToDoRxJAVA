package com.example.pawel.todo2.dao;

import java.util.Objects;

public class Message extends MessageNewDao {
    private String id;

    public Message(String sender, String content, String receiver, String id) {
        super(sender, content, receiver);
        this.id = id;
    }

    public boolean equalEmail(String email2, String email){
        return  Objects.equals(email2.trim(), email.trim());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
