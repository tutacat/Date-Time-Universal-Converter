package com.company.Operations;

import com.company.App;
import com.company.Utilities.ColorfulConsole;

import java.awt.event.KeyEvent;
import java.util.*;

import static com.company.Main.EMPTY_STRING;
import static com.company.Utilities.ConsoleColors.AnsiColor.*;
import static com.company.Utilities.ConsoleColors.AnsiColor.Modifier.Bold;
import static com.company.Utilities.ConsoleColors.AnsiColor.Modifier.Regular;
import static java.lang.Integer.parseInt;
import static java.lang.System.in;
import static java.lang.System.out;

public class Menu implements MenuInterface {

    public static MenuInterface ActiveMenu;

    protected Dictionary<Integer, String> Options;
    protected List<Event> functions;

    public int GetSize(){
        return Options.size();
    }

    private String menuHeader = EMPTY_STRING;

    private int rows = 5;
    public void SetRows(int n){
        rows = n;
        if(rows < 0)
            rows = 1;
        else if(rows > GetSize())
            rows = GetSize() / 2;
    }

    public void SetHeader(String headerString){
        this.menuHeader = headerString;
    }

    public Menu()
    {
        Options = new Hashtable<>();
        functions = new ArrayList<>();
    }

    public boolean RemoveOption(Integer option){
        return functions.remove(option);
    }

    public void ProcessInput() {
        ColorfulConsole.Write(Yellow(Regular), ">");

        Scanner s = new Scanner(in);
        int cmd = 1;
        String line = s.nextLine();
        if(GetSize() > 1 && line.length() == 0) {
            return;
        }
        if(GetSize() > 1 && line.length() >= 1){
            cmd = parseInt(line);
        }
        if(line.length() > String.valueOf(GetSize()).length() && Integer.parseInt(line) > GetSize()){
            out.println("Input not recognized");
            return;
        }
        ActiveMenu = (MenuInterface) functions.get(cmd - 1).Run();
    }

    public void Show(String decorator) {
        out.println(App.AppName);
        //out.println(decorator);
        ColorfulConsole.WriteLine(Blue(Bold), decorator);
        if(menuHeader != EMPTY_STRING) {
            ColorfulConsole.WriteLine(Red(Bold), menuHeader);
        }

        boolean b = false;
        if(Options.size() > rows)
            b=true;

        String f = "";
        int i = 1, aux = 0;
        if(b == true){
            int res = Options.size() / rows;
            for (int j = 0; j < res; j++) {
                for (; i <= Options.size(); i++) {
                    f += String.format("{0}[%d] {1}%-10s\t", i, Options.get(i - 1));
                    aux++;
                    if(aux == rows) {
                        i++;
                        aux = 0;
                        if(i < Options.size()) {
                            f += "\n";
                        }
                        break;
                    }
                }
            }
            ColorfulConsole.WriteLineFormatted(f, Blue(Bold), Red(Regular));
        }else {
            for (int r = 1; r <= Options.size(); r++) {
                f = String.format("{0}[%d] {1}%s", r, Options.get(r - 1));
                ColorfulConsole.WriteLineFormatted(f, Blue(Bold), Red(Regular));
            }
        }
        ColorfulConsole.WriteLine(Blue(Bold), decorator);
    }

    public void AddOption(String op, Event<MenuInterface> o) {
        functions.add(o);
        Options.put(GetSize(), op);
    }
}
