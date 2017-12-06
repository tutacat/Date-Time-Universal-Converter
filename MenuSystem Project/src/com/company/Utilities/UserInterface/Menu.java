package com.company.Utilities.UserInterface;

import com.company.App;
import com.company.Operations.Application;
import com.company.Operations.Executor;
import com.company.Operations.MenuInterface;
import com.company.Utilities.Colorfull_Console.ColorfulConsole;

import java.util.*;

import static com.company.Main.EMPTY_STRING;
import static com.company.Utilities.Colorfull_Console.ConsoleColors.AnsiColor.*;
import static com.company.Utilities.Colorfull_Console.ConsoleColors.AnsiColor.Modifier.Bold;
import static com.company.Utilities.Colorfull_Console.ConsoleColors.AnsiColor.Modifier.Regular;
import static java.lang.System.in;
import static java.lang.System.out;

public class Menu implements MenuInterface {

    private Application app;

    private Dictionary<Integer, String> Options;
    private List<Executor> functions;

    public int GetSize(){
        return Options.size();
    }

    private String menuHeader = EMPTY_STRING;
    private String menuName = "DefaultMenu";

    public void SetMenuName(String menuName) {
        this.menuName = menuName;
    }

    @Override
    public String GetMenuName() {
        return this.menuName;
    }

    private int rows = 5;
    public void SetRows(int n){
        rows = n;
        if(rows <= 0)
            rows = 1;
        else if(rows > GetSize()) {
            if(GetSize() != 0)
                rows = GetSize();
        }
    }

    public void SetApplication(Application application) {
        if(application != null)
            app = application;
    }

    public void SetHeader(String headerString){
        this.menuHeader = headerString;
    }

    public Menu()
    {
        Options = new Hashtable<>();
        functions = new ArrayList<>();
    }

    public boolean RemoveOption(Integer option) {
        return functions.contains(option) && functions.remove(option);
    }

    public void ProcessInput() {
        ColorfulConsole.Write(Yellow(Regular), ">");

        Scanner s = new Scanner(in);
        int cmd = 1;
        String line = s.nextLine();

        if(Objects.equals(line, "") && GetSize() > 1) {
            return;
        }
        else if(GetSize() >= 1 && line.length() >= 1){
            try {
               cmd = Integer.parseInt(line);
            }
            catch (NumberFormatException e) {
                out.println("Input not recognized");
                return;
            }
        }
        else if(GetSize() > 1){
            out.println("Input not recognized");
            return;
        }

        //LogSystem.WriteLog("Current Menu -> " + this.menuName + " | User Input -> " + cmd);
        app.setActiveMenu((MenuInterface) functions.get(cmd - 1).Run());
    }

    public void AddOption(String op, Executor <MenuInterface> o) {
        functions.add(o);
        Options.put(GetSize(), op);
    }

    @Override
    public void AddExitOption(MenuInterface landingMenu) {
        AddOption("Exit", () -> landingMenu);
    }

    public void Show(String decorator) {
        out.println(App.This.getName());
        //out.println(decorator);
        ColorfulConsole.WriteLine(Blue(Bold), decorator);
        if(!Objects.equals(menuHeader, EMPTY_STRING)) {
            ColorfulConsole.WriteLine(Red(Bold), menuHeader);
        }

        boolean b = false;
        if(Options.size() > rows)
            b=true;

        StringBuilder f = new StringBuilder();
        int i = 1, aux = 0;
        if(b){
            int res = Options.size() / rows;
            for (int j = 0; j <= res; j++) {
                for (; i <= Options.size();) {
                    f.append(String.format("{0}[%d] {1}%-10s\t", i, Options.get(i - 1)));
                    aux++;
                    if(aux == rows) {
                        i++;
                        aux = 0;
                        if(i <= Options.size()) {
                            f.append("\n");
                        }
                        break;
                    }
                    else i++;
                }
            }
            ColorfulConsole.WriteLineFormatted(f.toString(), Blue(Bold), Red(Regular));
        }else {
            for (int r = 1; r <= Options.size(); r++) {
                f = new StringBuilder(String.format("{0}[%d] {1}%s", r, Options.get(r - 1)));
                ColorfulConsole.WriteLineFormatted(f.toString(), Blue(Bold), Red(Regular));
            }
        }
        ColorfulConsole.WriteLine(Blue(Bold), decorator);
    }
}
