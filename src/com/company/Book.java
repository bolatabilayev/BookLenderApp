package com.company;

public class Book {
    private static int id;
    private String name;
    private String imagePath;
    private boolean isAvailable;
    private String article;

    public Book(String name, String imagePath, boolean isAvailable) {
        this.name = name;
        this.imagePath = imagePath;
        this.isAvailable = isAvailable;
        this.id++;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }
}
