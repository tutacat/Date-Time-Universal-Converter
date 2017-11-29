package com.company;

import com.company.Operations.Application;
import com.company.Utilities.Logger.LogSystem;

import java.io.IOException;

public class Main {

    public static final String EMPTY_STRING = "";

    private static Application app;

    public static void main(String[] args) {
        try {
            LogSystem.SetLogFile("UserLog.log");
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        app = new App();
        app.setName("Time Calculator");
        app.Run();
    }
}
