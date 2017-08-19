package com.tegeltech.codegeneratormonkey.domain;

import lombok.Data;

import java.util.List;

@Data
public class Test {

    private Clazz clazz;
    private List<TestCase> testCases;
}
