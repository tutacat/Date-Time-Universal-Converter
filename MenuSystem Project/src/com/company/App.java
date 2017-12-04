package com.company;

import com.company.Operations.Application;
import com.company.Operations.MenuInterface;
import com.company.Utilities.*;

import static com.company.Utilities.ConsoleColors.AnsiColor.Modifier.Underline;
import static com.company.Utilities.ConsoleColors.AnsiColor.Red;

public class App implements Application, EventListener  {

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

        OnStateChangedEvent.RegisterListener(this);
        OnApplicationClose.RegisterListener(this);

        Menus.CreateMenus(this);

        setActiveMenu(MenuFactory.getExistingMenu("Main Menu"));
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
