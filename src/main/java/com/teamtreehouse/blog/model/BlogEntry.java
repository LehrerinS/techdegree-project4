package com.teamtreehouse.blog.model;

import com.github.slugify.Slugify;

import java.io.IOException;
import java.util.Date;

public class BlogEntry {

    private String title;
    private Date createdDate;
    private String slug;
    private String body;

    public BlogEntry(String title, Date date, String body){
        this.title=title;
        this.createdDate =date;
        this.body = body;
        try {
            Slugify slugify = new Slugify();
            slug = slugify.slugify(title);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getBody() {
        return body;
    }

    public String getTitle() {
        return title;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public String getSlug() {
        return slug;
    }

    public boolean addComment(Comment comment) {
        // Store these comments!
        return false;
    }
}
