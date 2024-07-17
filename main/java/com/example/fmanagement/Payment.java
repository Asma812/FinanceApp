package com.example.fmanagement;

public class Payment {

    private int id;
    private String name;
    private String deadline;

    public Payment() {
    }

    public Payment(String name, String deadline) {
        this.name = name;
        this.deadline = deadline;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
}

