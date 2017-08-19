package com.tegeltech.codegeneratormonkey.domain;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class Clazz {
    private String packageName;
    private String fileName;

    private List<Member> members = Collections.emptyList();
    private List<Method> methods = Collections.emptyList();
}
