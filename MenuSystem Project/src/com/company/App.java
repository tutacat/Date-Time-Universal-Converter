package com.company;

import com.company.Operations.Application;
import com.company.Operations.Menu;
import com.company.Operations.MenuInterface;
import com.company.Utilities.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.company.Utilities.ConsoleColors.AnsiColor.Green;
import static com.company.Utilities.ConsoleColors.AnsiColor.Modifier.Bold;
import static com.company.Utilities.ConsoleColors.AnsiColor.Modifier.Underline;
import static com.company.Utilities.ConsoleColors.AnsiColor.Red;
import static java.lang.System.out;

public class App implements Application, EventListener  {

    public static App This;

    static final String Decorator = "________________________________________________________________";
    public static final String OtherDecorator = "███████████████████████████████████";

    private String AppName;

    private boolean IsRunning = false;

    private MenuInterface mainMenu = new Menu();
    private MenuInterface dateMenu = new Menu();
    private MenuInterface TemporalUnitsMenu = new Menu();
    private MenuInterface ExitMenu = new Menu();

    public void setName(String name){
        AppName = name;
    }
    public String getName(){
        return AppName;
    }

    private MenuInterface activeMenu;

    State currentState = State.closed;

    public void setActiveMenu(MenuInterface menu){
        if(menu == null)
            throw new NullPointerException();
        activeMenu = menu;
    }

    /**This creates a new Event executor that handles methods
    *   defined by the delegate class
    *   in this case:
    *       public void MethodCalledByEventExecutor(State state){
    *           ...
    *       }
    */
    public EventExecutor OnStateChangedEvent = new EventExecutor(0, new Delegate(void.class, State.class));

    /**
     * Does not return and does not have parameters
     * code set to 1!
     * Eg:
     *      public void CustomMethod(){
     *
     *      }
     */
    public EventExecutor OnApplicationClose = new EventExecutor(1, new Delegate(void.class));

    public MenuInterface getActiveMenu(){
        return activeMenu;
    }

    @Override
    public void SetState(State state) {
        this.currentState = state;

        //The OnStateChangedEvent will call
        //all methods with the Event annotation
        //and the respective code
        OnStateChangedEvent.Invoke(state);
    }

    /**
     * Annotation referente ao event executor com o code = 0!
     *
     * Neste caso isto corre sempre que e a app muda de state
     * */
    @Event(eventExecutorCode = 0)
    public void OnStateChanged(State state){
        ColorfulConsole.WriteLine(Green(Bold), "In App.Java! " + state.toString());
        if(state == State.running)
            IsRunning = true;
        if(state == State.closed)
            IsRunning = false;
    }

    @Event( eventExecutorCode = 1)
    public void OnApplicationClosing(){
        ColorfulConsole.WriteLine(Red(Underline),"!Goodbye!");
    }

    public void Start()
    {
        This = this;

        OnStateChangedEvent.RegisterListener(this);
        OnApplicationClose.RegisterListener(this);

        mainMenu.SetApplication(this);
        mainMenu.SetMenuName("Main Menu");
        mainMenu.SetHeader("This is the main menu");
        mainMenu.AddOption("Get Current Date", () -> {
            out.println(LocalDateTime.now());
            return mainMenu;
        });
        mainMenu.AddOption("Sum to Current LocalDateTime", () -> TemporalUnitsMenu);
        mainMenu.AddOption("Exit", () ->  ExitMenu);

        ExitMenu.SetMenuName("Exit Menu");
        ExitMenu.SetApplication(this);
        ExitMenu.AddOption("Press Enter", () -> {
            SetState(State.closed);
            return ExitMenu;
        });

        for (ChronoUnit unit : ChronoUnit.values())
        {
            if(unit.isSupportedBy(LocalDateTime.now()))
            {
                TemporalUnitsMenu.AddOption(unit.toString(), () ->
                {
                    ColorfulConsole.WriteLine(Green(Underline), "How many " + unit.toString() + " you want to add?");
                    int cmd = ColorfulConsole.ReadNextInt();
                    LocalDateTime time = LocalDateTime.now().plus((long) cmd, unit);
                    ColorfulConsole.WriteLine(Green(Underline),"Time now: " + LocalDateTime.now());
                    ColorfulConsole.WriteLine(Green(Underline),"Time plus " + cmd + " " + unit.toString() + ":" + time);
                    return mainMenu;
                });
            }
        }

        TemporalUnitsMenu.SetMenuName("Temporal Menu Units");
        TemporalUnitsMenu.SetApplication(this);
        TemporalUnitsMenu.SetRows(3);
        TemporalUnitsMenu.AddOption("Back", () -> mainMenu);
        TemporalUnitsMenu.SetHeader("Choose what Chronological part you want to Sum");

        dateMenu.SetMenuName("Date Menu");
        dateMenu.SetApplication(this);
        dateMenu.AddOption("Back", () -> mainMenu);

        setActiveMenu(mainMenu);
    }

    public void Update() {
        while(true){
            getActiveMenu().Show(Decorator);
            SetState(State.stopped);
            getActiveMenu().ProcessInput();
            if(!IsRunning)
                break;
            SetState(State.running);
        }
        OnApplicationClose.Invoke();
    }

    public void Run() {
        Start();
        SetState(State.running);
        Update();
    }
}
