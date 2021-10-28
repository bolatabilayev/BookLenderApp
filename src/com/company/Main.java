package com.company;

import com.company.server.LibraryServer;
import com.company.context.DataContext;
import com.company.context.JsonContext;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        DataContext dataContext = new JsonContext();
        try {
            new LibraryServer("localhost", 5000, dataContext).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
