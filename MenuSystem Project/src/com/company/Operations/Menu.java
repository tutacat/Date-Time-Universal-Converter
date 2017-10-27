package com.company.Operations;

import com.company.App;
import com.company.Main;

import java.io.IOException;
import java.util.*;

import static com.company.Main.EMPTY_STRING;
import static com.company.Main.main;
import static java.lang.Integer.parseInt;
import static java.lang.System.in;
import static java.lang.System.out;

public class Menu implements IMenu {

    public static IMenu ActiveMenu;

    protected Dictionary<Integer, String> Options;
    protected List<Event> functions;

    public int Size = 0;

    private String menuHeader = EMPTY_STRING;

    public void SetHeader(String headerString){

        this.menuHeader = headerString;
    }

    public Menu()
    {
        Options = new Hashtable<>();
        functions = new ArrayList<>();
    }

    public boolean RemoveOption(Integer option){
        Size--;
        return functions.remove(option);
    }

    public void ProcessInput() {
        Scanner s = new Scanner(in);
        int cmd = 1;
        String line = s.nextLine();
        if(Size > 1 && line.length() == 0) {
            return;
        }
        if(Size > 1 && line.length() >= 1){
            cmd = parseInt(line);
        }
        if(line.length() > String.valueOf(Size).length() && Integer.parseInt(line) > Size){
            System.out.println("Input not recognized");
            return;
        }
        ActiveMenu = (IMenu) functions.get(cmd - 1).Run();
    }

    public void Show(String decorator) {
        out.println(App.AppName);
        out.println(decorator);
        if(menuHeader != EMPTY_STRING)
            out.println(menuHeader);
        for (int i = 1; i <= Options.size(); i++) {
            out.printf("[%d] %s\n", i, Options.get(i));
        }
        out.println(decorator);
    }

    public void AddOption(String op, Event<IMenu> o) {
        Size++;
        functions.add(o);
        Options.put(Size, op);
    }
}
