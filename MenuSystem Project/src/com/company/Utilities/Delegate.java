package com.company.Utilities;

public class Delegate {

    private final Class returnable;
    private final Class[] parameters;

    public Delegate(Class returnable, Class ...parameters){
        this.returnable = returnable;
        this.parameters = parameters;
    }

    public Class getReturnable() {
        return returnable;
    }

    public Class[] getParameters() {
        return parameters;
    }

}
