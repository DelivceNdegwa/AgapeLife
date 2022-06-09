package com.example.agapelife.models;


// Notification object
public class Notification {
    String notificationMessage;
    long recepientId;
    int recepientCategory;

    public Notification() {
    }

    public Notification(String notificationMessage, long recepientId, int recepientCategory) {
        this.notificationMessage = notificationMessage;
        this.recepientId = recepientId;
        this.recepientCategory = recepientCategory;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public long getRecepientId() {
        return recepientId;
    }

    public void setRecepientId(long recepientId) {
        this.recepientId = recepientId;
    }

    public int getRecepientCategory() {
        return recepientCategory;
    }

    public void setRecepientCategory(int recepientCategory) {
        this.recepientCategory = recepientCategory;
    }
}
