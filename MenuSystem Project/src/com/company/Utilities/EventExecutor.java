package com.company.Utilities;

import java.lang.reflect.Method;
import java.util.*;

import static com.company.Utilities.ConsoleColors.AnsiColor.Modifier.Underline;
import static com.company.Utilities.ConsoleColors.AnsiColor.Red;

public class EventExecutor {

    private final int PRE = 1;
    private final int ALL = 0;
    private final int POST = -1;


    private Map<Class, Collection<EventHandler>> bindings = new HashMap<>();
    private Set<EventListener> registeredListeners = new HashSet <>();

    private final Delegate delegate;
    private final int code;

    public EventExecutor(int code, Delegate delegate){
        this.delegate = delegate;
        this.code = code;
    }

    public Map <Class, Collection <EventHandler>> getBindings() {
        return bindings;
    }

    public Set <EventListener> getRegisteredListeners() {
        return registeredListeners;
    }

    public void RegisterListener(final EventListener listener) {
        if (registeredListeners.contains(listener)) {
            ColorfulConsole.WriteLine(Red(Underline), "Already Registered Listener");
            return;
        }

        Method[] methods = listener.getClass().getDeclaredMethods();
        for (Method m : methods) {
            Event annotation = m.getAnnotation(Event.class);
            if (annotation == null) continue;
            if (annotation.eventExecutorCode() != code) continue;

            boolean parametersRight = true;
            Class <?>[] parameters = m.getParameterTypes();
            if (parameters.length == delegate.getParameters().length) {
                for (int i = 0; i < parameters.length; i++) {
                    if (!Objects.equals(parameters[i].getName(), delegate.getParameters()[i].getName())) {
                        parametersRight = false;
                        break;
                    }
                }
                if (!parametersRight) {
                    this.registeredListeners.remove(listener);
                    break;
                }
            } else {
                ColorfulConsole.WriteLine(Red(Underline), "Parameters do not match on: "
                        + m.getClass().getName());
                this.registeredListeners.remove(listener);
                break;
            }

            if (!m.getReturnType().equals(delegate.getReturnable())) {
                ColorfulConsole.WriteLine(Red(Underline),
                        "Method return type does not match Delegate returnable");
                continue;
            }
            Collection <EventHandler> eventHandlers;
            if (!this.bindings.containsKey(listener.getClass())) {
                eventHandlers = new TreeSet <>();
                bindings.put(listener.getClass(), eventHandlers);
            } else {
                eventHandlers = this.bindings.get(listener.getClass());
            }
            synchronized (this) {
                eventHandlers.add(createEventHandler(listener, m, annotation));
                registeredListeners.add(listener);
            }
        }
    }


    public void removeListener(EventListener listener) {

        synchronized (this) {
            if (!registeredListeners.contains(listener))
                return;

            for (Map.Entry <Class, Collection <EventHandler>> ee : bindings.entrySet()) {
                if (ee.getKey() == listener.getClass()) {
                    bindings.remove(ee.getKey());
                }
            }
            this.registeredListeners.remove(listener);
        }
    }


    public void Invoke(Object ...args){

        Invoke(ALL, args);
    }

    public void Invoke(int priority, Object... args) {

        synchronized (this) {
            for (Map.Entry <Class, Collection <EventHandler>> entry : bindings.entrySet()) {
                for (EventHandler eventHandler : entry.getValue()) {
                    if (priority == PRE && eventHandler.getPriority() >= 0) {
                        continue;
                    }
                    if (priority == POST && eventHandler.getPriority() < 0) {
                        continue;
                    }
                    eventHandler.Execute(args);
                }
            }
        }
    }

    private EventHandler createEventHandler(final EventListener listener, final Method method, final Event annotation) {
        return new EventHandler(listener, method, annotation);
    }
}
