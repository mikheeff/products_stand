package com.Tsystems.product_stand;

public class PushEvent {
    private final String message;

    public PushEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
