package com.company.Operations;

public interface Application {

    enum State {
        running,
        stopped,
        closed
    }

    /**
     * Sets the Application name
     *
     * @param name Application name
     */
    void setName(String name);

    /**
     * This method is called before Update
     */
    void Start();


    /**
     * Getters and setters for some important stuff
     * that the application must be capable of handle
     *
     * Getting and setting the current active menu!
     * */
    void setActiveMenu(MenuInterface menu);
    MenuInterface getActiveMenu();

    void SetState(State state);

    void Run();

    /**
     * This method is called every CPU tick
     */
    void Update();
}
