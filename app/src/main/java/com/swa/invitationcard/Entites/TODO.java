package com.swa.invitationcard.Entites;

import java.io.Serializable;

public class TODO implements Serializable {
    private String task;
    private boolean status = false;

    public TODO() {
    }

    public TODO(String task, boolean status) {
        this.task = task;
        this.status = status;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
