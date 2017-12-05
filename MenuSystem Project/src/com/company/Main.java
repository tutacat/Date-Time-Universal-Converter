package com.company;

import com.company.Operations.Application;

public class Main {

    public static final String EMPTY_STRING = "";

    private static Application app;

    public static void main(String[] args) {
        app = new App();
        app.setName("Time Calculator");
        app.Run();
    }
}
