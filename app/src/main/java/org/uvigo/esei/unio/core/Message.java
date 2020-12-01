package org.uvigo.esei.unio.core;

public class Message {

    public enum Type {SENT_BY_USER, SENT_BY_SYSTEM};
    private Type type;
    private String text;

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
}
