package com.company.models;

import com.company.Book;

import java.util.ArrayList;
import java.util.List;

public class BookDataModel {
    private Book book;
    private List<Book> books = new ArrayList<>();

    public BookDataModel(){
        Book java = new Book("java", "img/java.jpg", true);
        Book js = new Book("javascript", "img/js.jpg", true);
        Book python = new Book("python", "img/python.jpg", true);
        Book csharp = new Book("csharp", "img/csharp.jpg", true);

        books.add(java);
        books.add(js);
        books.add(python);
        books.add(csharp);

        java.setArticle("Книга о Java");
        js.setArticle("Книга про знакомство с JavaScript");
        python.setArticle("Книга про знакомство с Python");
        csharp.setArticle("Книга про знакомство с C#");
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
