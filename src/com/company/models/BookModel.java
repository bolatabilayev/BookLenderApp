package com.company.models;

import com.company.Book;

public class BookModel {
    private Book book;

    public BookModel(Book book) {
        this.book = book;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
