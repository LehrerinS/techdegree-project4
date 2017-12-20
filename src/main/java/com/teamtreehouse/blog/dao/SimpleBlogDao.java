package com.teamtreehouse.blog.dao;

import com.teamtreehouse.blog.model.BlogEntry;

import java.util.ArrayList;
import java.util.List;

public class SimpleBlogDao implements BlogDao {

    private List<BlogEntry> blogs;

    public SimpleBlogDao() {
        blogs = new ArrayList<>();
    }

    @Override
    public boolean addEntry(BlogEntry blogEntry) {
        return blogs.add(blogEntry);
    }

    @Override
    public List<BlogEntry> findAllEntries() {
        return blogs;
    }

    @Override
    public BlogEntry findEntryBySlug(String slug) {
        return blogs.stream()
                .filter(blog -> blog.getSlug().equals(slug))
                .findFirst()
                .orElseThrow(NotFoundException::new);
    }
}
