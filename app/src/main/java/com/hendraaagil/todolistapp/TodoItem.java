package com.hendraaagil.todolistapp;

public class TodoItem {
    private int id;
    private String name, completedAt;
    private boolean isComplete;

    public TodoItem(int id, String name, String completedAt, boolean isComplete) {
        this.id = id;
        this.name = name;
        this.completedAt = completedAt;
        this.isComplete = isComplete;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCompletedAt() {
        return completedAt;
    }

    public boolean isComplete() {
        return isComplete;
    }
}
