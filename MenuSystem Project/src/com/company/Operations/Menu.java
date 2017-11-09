package com.company.Operations;

import com.company.App;
import com.company.Utilities.ColorfulConsole;
import com.company.Utilities.ConsoleColors;
import com.company.Utilities.ConsoleColors.AnsiColor.Modifier;

import java.io.Console;
import java.util.*;
import java.util.function.Predicate;

import static com.company.Main.EMPTY_STRING;
import static com.company.Utilities.ConsoleColors.AnsiColor.Blue;
import static com.company.Utilities.ConsoleColors.AnsiColor.Modifier.*;
import static com.company.Utilities.ConsoleColors.AnsiColor.Red;
import static com.company.Utilities.ConsoleColors.AnsiColor.Yellow;
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
        ColorfulConsole.Write(Yellow(Regular), ">");
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
        //out.println(decorator);
        ColorfulConsole.WriteLine(Blue(Bold), decorator);
        if(menuHeader != EMPTY_STRING) {
            ColorfulConsole.WriteLine(Red(Bold), menuHeader);
        }
        for (int i = 1; i <= Options.size(); i++) {
            String f = String.format("{0}[%1$s] {1}%2$s", i, Options.get(i));
            ColorfulConsole.WriteLineFormatted(f, Blue(Bold), Red(Underline));
        }
        ColorfulConsole.WriteLine(Blue(Bold), decorator);
    }

    public void AddOption(String op, Event<IMenu> o) {
        Size++;
        functions.add(o);
        Options.put(Size, op);
    }
}
