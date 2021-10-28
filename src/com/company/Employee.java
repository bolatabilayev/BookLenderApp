package com.company;

import java.util.ArrayList;
import java.util.List;

public class Employee {
    private static int id;
    private String username;
    private String password;
    private String email;
    private String imgPath;
    private final int maxCapacity = 2;
    private List<Book> booksOnHand = new ArrayList<>();
    private List<Book> booksTakenPreviously = new ArrayList<>();

    public Employee(String username, String email) {
        this.username = username;
        this.email = email;
        this.id++;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Book> getBooksOnHand() {
        return booksOnHand;
    }

    public void setBooksOnHand(List<Book> booksOnHand) {
        this.booksOnHand = booksOnHand;
    }

    public List<Book> getBooksTakenPreviously() {
        return booksTakenPreviously;
    }

    public void setBooksTakenPreviously(List<Book> booksTakenPreviously) {
        this.booksTakenPreviously = booksTakenPreviously;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void takeABook(Book book){
        booksOnHand.add(book);
        book.setAvailable(false);
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", maxCapacity=" + maxCapacity +
                ", booksOnHand=" + booksOnHand +
                ", booksTakenPreviously=" + booksTakenPreviously +
                '}';
    }
}
