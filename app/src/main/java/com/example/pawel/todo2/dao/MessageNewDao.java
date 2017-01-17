package com.example.pawel.todo2.dao;



public class MessageNewDao {

    private String sender;

    private String content;

    private String receiver;


    public MessageNewDao(String sender, String content, String receiver) {
        this.sender = sender;
        this.content = content;
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }


    @Override
    public int hashCode() {
        return content.hashCode();
    }

    @Override
    public String toString() {
        return "MessageNewDao{" +
                "sender='" + sender + '\'' + ", content='" + content + '\'' +  ", receiver='" + receiver + '}';
    }
}
