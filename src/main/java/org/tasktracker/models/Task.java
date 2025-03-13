package org.tasktracker.models;

import java.time.LocalDateTime;


public class Task {
    private  int id;
    private String description;
    private Status status;
    private  LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    //Jackson needs a no-argument construct to call it while Deserialization is happening
    public Task() {}
    public Task(int id, String description, Status status){
    this.id=id;
    this.description=description;
    this.status=status;
    this.createdAt=LocalDateTime.now();
    this.updatedAt=LocalDateTime.now();
    }
    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

