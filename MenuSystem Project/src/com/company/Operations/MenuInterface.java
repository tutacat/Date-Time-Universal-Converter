package com.company.Operations;

public interface MenuInterface {
    boolean RemoveOption(Integer Option);
    void ProcessInput();
    void AddOption(String op, Executor<MenuInterface> o);
    void AddExitOption(MenuInterface landingMenu);
    void Show(String decorator);

    void SetHeader(String header);
    void SetRows(int n);

    void SetApplication(Application application);

    void SetMenuName(String name);
    String GetMenuName();
}
