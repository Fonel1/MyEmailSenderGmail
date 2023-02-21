package com.example.senderemailaut.EventBus;

import com.example.senderemailaut.Model.Receiver;

import java.util.List;

public class EmailsDbLoadEvent {
    private boolean success;
    private String message;
    private List<Receiver> receiverList;

    public EmailsDbLoadEvent(boolean success) {
        this.success = success;
    }

    public EmailsDbLoadEvent(boolean success, List<Receiver> receiverList) {
        this.success = success;
        this.receiverList = receiverList;
    }

    public EmailsDbLoadEvent(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Receiver> getReceiverList() {
        return receiverList;
    }

    public void setReceiverList(List<Receiver> receiverList) {
        this.receiverList = receiverList;
    }
}
