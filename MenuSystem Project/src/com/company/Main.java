package com.company;

import java.lang.reflect.InvocationTargetException;

public class Main {

    public static final String EMPTY_STRING = "";

    static App app;

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        app = new App("Time Calculator");
        app.Run();
    }
}
