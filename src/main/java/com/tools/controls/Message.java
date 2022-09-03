package com.tools.controls;

public class Message {

    public final static int SUCCESS_TYPE = 1;
    public final static int NORMAL_TYPE = 0;
    public final static int ERROR_TYPE = -1;
    public final static String SUCCESS_COLOR = "GREEN";
    public final static String NORMAL_COLOR = "BLACK";
    public final static String ERROR_COLOR = "RED";
    private final int type;
    private final String message;
    private final String color;

    public Message(int type, String message, String color) {
        this.type = type;
        this.message = message;
        this.color = color;
    }

    public int getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public String getColor() {
        return color;
    }

}