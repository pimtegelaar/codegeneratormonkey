package com.tegeltech.codegeneratormonkey.domain;

import lombok.Data;

import java.util.List;

@Data
public class Method {
    private String name;
    private Class<?> returnType;
    private Class<?>[] parameterTypes;
    private Class<?>[] exceptionTypes;
    private int modifiers;
    private List<Statement> statements;
}
