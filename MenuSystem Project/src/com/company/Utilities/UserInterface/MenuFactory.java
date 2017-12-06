package com.company.Utilities.UserInterface;

import com.company.Operations.Application;
import com.company.Operations.MenuInterface;

import java.util.*;

public class MenuFactory {

    static Dictionary<String, MenuInterface> menuFactory = new Hashtable <>();

    public static ArrayList <String> getMenuNames(){
        return Collections.list(menuFactory.keys());
    }

    public static ArrayList <MenuInterface> getMenus(){
        return Collections.list(menuFactory.elements());
    }

    public static MenuInterface getExistingMenu(String menuName){

        final MenuInterface[] requiredMenu = new MenuInterface[1];
        getMenus().forEach((MenuInterface menu) -> {
            if(Objects.equals(menu.GetMenuName(), menuName)){
                requiredMenu[0] = menu;
            }
        });

        if(requiredMenu[0] == null)
            throw new NullPointerException();

        return requiredMenu[0];
    }

    public static MenuInterface getMenu(Application app, String name){
        return createMenu(app, name);
    }

    static private MenuInterface createMenu(Application app, String menuName){
        if(app == null) {
            throw new NullPointerException();
        }

        MenuInterface newMenu = new Menu();
        newMenu.SetMenuName(menuName);
        newMenu.SetApplication(app);
        menuFactory.put(menuName, newMenu);
        return newMenu;
    }
}
