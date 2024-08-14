package com.example.furni.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "blog")
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "tag", length = 100)
    private String tag;

    @Column(name = "thumbnail", columnDefinition = "LONGTEXT")
    private String thumbnail;

    @Column(name = "content", columnDefinition = "LONGTEXT")
    private String content;

    @Column(name = "blog_date")
    private LocalDateTime blogDate;


    public Blog(int id, String title, String tag, String thumbnail, String content, LocalDateTime blogDate) {
        this.id = id;
        this.title = title;
        this.tag = tag;
        this.thumbnail = thumbnail;
        this.content = content;
        this.blogDate = blogDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getBlogDate() {
        return blogDate;
    }

    public void setBlogDate(LocalDateTime blogDate) {
        this.blogDate = blogDate;
    }

    public Blog(){
    }
}
