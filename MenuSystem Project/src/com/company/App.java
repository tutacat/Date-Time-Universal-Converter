package com.company;

import com.company.Operations.Application;
import com.company.Operations.MenuInterface;
import com.company.Utilities.Colorfull_Console.ColorfulConsole;
import com.company.Utilities.Events.Delegate;
import com.company.Utilities.Events.Event;
import com.company.Utilities.Events.EventExecutor;
import com.company.Utilities.Events.EventListener;
import com.company.Utilities.Net.HolidaysManager;
import com.company.Utilities.UserInterface.MenuFactory;
import com.company.Utilities.UserInterface.Menus;

import static com.company.Utilities.Colorfull_Console.ConsoleColors.AnsiColor.Modifier.Underline;
import static com.company.Utilities.Colorfull_Console.ConsoleColors.AnsiColor.Red;

public class App implements Application, EventListener {

    public static App This;

    static final String Decorator = "________________________________________________________________";
    public static final String OtherDecorator = "███████████████████████████████████";

    private String AppName;

    private boolean IsRunning = false;

    public void setName(String name){
        AppName = name;
    }
    public String getName(){
        return AppName;
    }

    private MenuInterface activeMenu;

    State currentState = State.closed;

    @Override
    public void setActiveMenu(MenuInterface menu, Object usable) {
        activeMenu = menu;
        activeMenu.setArg(usable);
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
        if(state == State.running)
            IsRunning = true;
        if(state == State.closed)
            IsRunning = false;
    }

    @Event(eventExecutorCode = 1)
    public void OnApplicationClosing(){
        ColorfulConsole.WriteLine(Red(Underline),"!Goodbye!");
    }

    public void Start()
    {
        This = this;

        HolidaysManager holidaysManager = new HolidaysManager();
        holidaysManager.Connect(HolidaysManager.holidaysUrl);
        holidaysManager.loadZones(false);

        this.OnStateChangedEvent.RegisterListener(this);
        this.OnApplicationClose.RegisterListener(this);

        Menus.CreateMenus(this);

        /*
        * Modo Inicial = Main Menu
        * */
        this.setActiveMenu(MenuFactory.getExistingMenu("Main Menu"), null);

        SetState(State.running);
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
        Update();
    }
}
