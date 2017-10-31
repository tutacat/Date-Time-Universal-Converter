package com.company;

import com.company.Operations.Menu;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

import static com.company.Operations.Menu.ActiveMenu;
import static java.lang.System.in;
import static java.lang.System.out;

public class App {

    public static App This;

    static final String Decorator = "________________________________________________________________";
    public static final String OtherDecorator = "███████████████████████████████████";

    public static String AppName;

    public boolean IsRunning = false;

    private Menu mainMenu = new Menu();
    private Menu dateMenu = new Menu();
    private Menu TemporalUnitsMenu = new Menu();
    private Menu ExitMenu = new Menu();

    public App(String Name){
        AppName = Name;
    }

    private void Start()
    {
        This = this;

        mainMenu.SetHeader("This is the main menu");
        mainMenu.AddOption("Get Current Date", () -> {
            out.println(LocalDateTime.now());
            return mainMenu;
        });
        mainMenu.AddOption("Sum to Current LocalDateTime", () -> TemporalUnitsMenu);
        mainMenu.AddOption("Exit", () ->  ExitMenu);

        ExitMenu.AddOption("Press Enter", () -> {
            SetRunning();
            return ExitMenu;
        });
        ExitMenu.SetHeader("!GoodBye!");

        for (ChronoUnit unit : ChronoUnit.values())
        {
            if(unit.isSupportedBy(LocalDateTime.now()))
            {
                TemporalUnitsMenu.AddOption(unit.toString(), () ->
                {
                    out.println("How many " + unit.toString() + " you want to add?");
                    Scanner s = new Scanner(in);
                    int cmd = s.nextInt();
                    LocalDateTime time = LocalDateTime.now().plus((long) cmd, unit);
                    out.println("Time now: " + LocalDateTime.now());
                    out.println("Time plus " + cmd + " " + unit.toString() + ":" + time);
                    return mainMenu;
                });
            }
        }
        TemporalUnitsMenu.SetHeader("Choose what Chronological part you want to Sum");

        dateMenu.AddOption("Back", () -> mainMenu);

        ActiveMenu = mainMenu;
    }

    private void Update() throws InvocationTargetException, IllegalAccessException {
        while(true){
            if(!IsRunning)
                break;
            Menu.ActiveMenu.Show(Decorator);
            Menu.ActiveMenu.ProcessInput();
        }
    }

    public void SetRunning(){
        IsRunning = !IsRunning;
    }

    public void Run() throws InvocationTargetException, IllegalAccessException {
        SetRunning();
        Start();
        Update();
    }
}
