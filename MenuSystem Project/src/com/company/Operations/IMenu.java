package com.company.Operations;

public interface IMenu {
    boolean RemoveOption(Integer Option);
    void ProcessInput();
    void AddOption(String op, Event<IMenu> o);
    void Show(String decorator);
}
