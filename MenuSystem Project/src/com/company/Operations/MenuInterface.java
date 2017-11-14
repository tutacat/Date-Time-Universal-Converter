package com.company.Operations;

public interface MenuInterface {
    boolean RemoveOption(Integer Option);
    void ProcessInput();
    void AddOption(String op, Event<MenuInterface> o);
    void Show(String decorator);
}
