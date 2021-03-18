package com.hendraaagil.todolistapp;

public class TodoItem {
    private String name, completedAt;
    private boolean isComplete;

    public TodoItem(String name, boolean isComplete, String completedAt) {
        this.name = name;
        this.isComplete = isComplete;
        this.completedAt = completedAt;
    }

    public String getName() {
        return name;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public String getCompletedAt() {
        return completedAt;
    }
}
