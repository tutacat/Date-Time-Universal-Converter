package com.company.Utilities;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EventHandler implements Comparable<EventHandler> {

    private final EventListener listener;
    private final Method method;
    private final Event annotation;

    public EventHandler(EventListener listener, Method method, Event annotation) {
        this.method = method;
        this.annotation = annotation;
        this.listener = listener;
    }

    public Event getAnnotation() {
        return annotation;
    }

    public Method getMethod() {
        return method;
    }

    public void Execute(Object ...args){
        try {
            method.invoke(listener, args);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public int getPriority() {
        return annotation.priority();
    }

    @Override
    public int compareTo(EventHandler o) {
        int annotation = this.annotation.priority() - o.annotation.priority();
        if (annotation == 0)
            annotation = this.listener.hashCode() - o.listener.hashCode();
        return annotation == 0 ? this.hashCode() - o.hashCode() : annotation;
    }
}
