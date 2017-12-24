package com.teamtreehouse.blog.model;

import com.github.slugify.Slugify;

import java.io.IOException;
import java.util.*;

public class BlogEntry {

    private String title;
    private Date createdDate;
    private String slug;
    private String body;
    private List<CommentEntry> comments;
    private Set<String> tags;

    public BlogEntry(String title, Date date, String body){
        this.title=title;
        this.createdDate =date;
        this.body = body;
        comments = new ArrayList<>();
        tags = new HashSet<>();
        try {
            Slugify slugify = new Slugify();
            slug = slugify.slugify(title);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BlogEntry(String title, Date date, String body, List<CommentEntry> comments){
        this.title=title;
        this.createdDate =date;
        this.body = body;
        this.comments = comments;
        tags = new HashSet<>();
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

    public List<CommentEntry> getComments() {
        return comments;
    }

    public String getSlug() {
        return slug;
    }

    public List<CommentEntry> addComment(CommentEntry comment) {
        // Store these comments!
        comments.add(comment);
        return comments;
    }

    public Set<String> getTags() {
        return tags;
    }

    public boolean addTags (String tag){
        return tags.add(tag);
    }
}
