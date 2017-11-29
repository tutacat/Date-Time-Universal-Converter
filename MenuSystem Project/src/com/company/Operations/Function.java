package com.company.Operations;

@FunctionalInterface
public interface Function<T, R>{
    T Run(R... params) throws Exception;
    default T Execute(R... params) throws Exception {
        T result = Run(params);
        if(result == null)
            throw new Exception();
        return result;
    }
}
