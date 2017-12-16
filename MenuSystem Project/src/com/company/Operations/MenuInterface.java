package com.company.Operations;

import com.company.Utilities.Tuple;

public interface MenuInterface {
    boolean RemoveOption(Integer Option);
    void ProcessInput();

    void AddOption(Executor<Tuple<MenuInterface, Object>> o, String op);
    void AddOption(String op, Executor<MenuInterface> o);

    void AddExitOption(MenuInterface landingMenu);

    void Show(String decorator);

    void SetHeader(String header);
    void SetRows(int n);

    Object getArg();
    void setArg(Object o);

    void SetApplication(Application application);

    void SetMenuName(String name);
    String GetMenuName();
}
