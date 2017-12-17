package com.company.Utilities.UserInterface;

import com.company.App;
import com.company.Operations.Application;
import com.company.Operations.Executor;
import com.company.Operations.MenuInterface;
import com.company.Utilities.Colorfull_Console.ColorfulConsole;
import com.company.Utilities.Events.Delegate;
import com.company.Utilities.Events.EventExecutor;
import com.company.Utilities.Events.EventListener;
import com.company.Utilities.Tuple;

import java.util.*;

import static com.company.Main.EMPTY_STRING;
import static com.company.Utilities.Colorfull_Console.ConsoleColors.AnsiColor.*;
import static com.company.Utilities.Colorfull_Console.ConsoleColors.AnsiColor.Modifier.Bold;
import static com.company.Utilities.Colorfull_Console.ConsoleColors.AnsiColor.Modifier.Regular;
import static java.lang.System.in;
import static java.lang.System.out;

public class Menu implements MenuInterface, EventListener {

    private Application app;

    private Dictionary<Integer, String> Options;
    private List<Executor> functions;

    private String menuHeader = EMPTY_STRING;
    private String menuName = "DefaultMenu";

    private Object usable = null;

    private int spacementLength = 0;

    public int GetSize(){
        return Options.size();
    }

    public EventExecutor onMenuChanged = new EventExecutor(0, new Delegate(void.class));

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

    @Override
    public Object getArg() {
        return usable;
    }

    @Override
    public void setArg(Object o) {
        usable = o;
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

        onMenuChanged.RegisterListener(this);
    }

    public void OnMenuChanged(){
        //Menu Has Changed
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
        Executor executor = functions.get(cmd - 1);
        MenuInterface chooseMenu = null;
        Tuple t = null;
        Object run = executor.Run ();
        try{
            t = (Tuple) run;
        }catch (Exception e){
            chooseMenu = (MenuInterface) run;
        }

        if(t != null){
            chooseMenu = (MenuInterface) t.a;
            app.setActiveMenu(chooseMenu, t.b);
            if(chooseMenu != this){
                onMenuChanged.Invoke();
            }
            return;
        }

        if(chooseMenu != null) {
            app.setActiveMenu(chooseMenu, null);
            if(chooseMenu != this){
                onMenuChanged.Invoke();
            }
            return;
        }
        out.println("No MENU could be selected");
    }


    /**
     * Adds a new Option to the Menu
     * that option when run sends some data to the landing Menu
     *
     * @param op String that will represent this option in the screen
     * @param o Tuple that contains the landing Menu and the Data that will go to that Menu
     *          (Can be the same)
     * */
    @Override
    public void AddOption(Executor<Tuple<MenuInterface, Object>> o, String op){
        _AddOption(op, o);
    }

    /**
     * Adds a new Option to the Menu
     *
     * @param op String that will represent this option in the screen
     * @param o The landing Menu (Can be the same)
     */
    @Override
    public void AddOption(String op, Executor <MenuInterface> o) {
        _AddOption(op, o);
    }

    public void _AddOption(String op, Executor o){

        int length = op.length ();
        if(length > this.spacementLength)
            this.spacementLength = length;

        functions.add(o);
        Options.put(GetSize(), op);
    }

    @Override
    public void AddExitOption(MenuInterface landingMenu) {
        AddOption("Exit", () -> {
            /*TODO: Should i call Garbage Collector here?*/
            System.gc();
            return landingMenu;
        });
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
                    f.append(String.format("{0}[%d] {1}%-"+this.spacementLength+"s\t", i, Options.get(i - 1)));
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
