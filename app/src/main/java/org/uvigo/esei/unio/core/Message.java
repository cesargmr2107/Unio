package org.uvigo.esei.unio.core;

public class Message {

    public enum Type {SENT_BY_USER, SENT_BY_SYSTEM}
    private Type type;
    private String text;
    private int id;
    private String tableName;

    public Message(String tableName, int id, Type type, String text) {
        this.id = id;
        this.type = type;
        this.text = text;
        this.tableName = tableName;
    }

    public Message(Type type, String text) {
        this.type = type;
        this.text = text;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public String getTableName() {
        return tableName;
    }
}
