package com.company.Operations;

import java.util.function.Function;

@FunctionalInterface
public interface ParametrizedEvent<T, R>{
    T Run(R... params) throws Exception;
    default T Execute(R... params) throws Exception {
        T result = Run(params);
        if(result == null)
            throw new Exception();
        return result;
    }
}
