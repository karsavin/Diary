package com.example.MyFirstProject;

import java.io.Serializable;

public class Note implements Serializable {
    private String time;
    private String name;
    private String date;
    private String description;
    Note(String time, String name, String date, String description){
        this.time = time;
        this.name = name;
        this.date = date;
        this.description = description;
    }
    Note(){

    }

    public String getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }
    public String getDescription() {
        return description;
    }
}
