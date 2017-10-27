package com.company.Operations;

@FunctionalInterface
public interface ParametarizedEvent<T, R>{
    T Run(R... params);
    default T Execute(R... params) throws Exception {
        T result = Run(params);
        if(result == null)
            throw new Exception();
        return result;
    }
}
