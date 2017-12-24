package com.teamtreehouse.blog.model;

import java.util.Date;

public class CommentEntry {
    private String name;
    private Date date;
    private String comment;

    public CommentEntry(String name, Date date, String comment) {
        this.name = name;
        this.date = date;
        this.comment = comment;
    }

    public Date getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }
}
