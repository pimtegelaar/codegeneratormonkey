package com.tegeltech.codegeneratormonkey;

import com.squareup.javapoet.*;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.lang.model.element.Modifier;
import java.beans.Introspector;
import java.io.IOException;
import java.nio.file.Paths;

public class Poetry {

    public static void main(String[] args) throws IOException {
        new Poetry().compose();
    }


    private void compose() throws IOException {
        String packageName = "com.tegeltech.tmp";
        String className = "Calculator";

        String root = "C:/git/java/MutationTestingExperiment/";
        String codeSourceFolder = root + "src/main/java";
        String testSourceFolder = root + "src/test/java";
        for (int i = 0; i < 10; i++) {
            details(packageName, className + String.format("%03d", i), codeSourceFolder, testSourceFolder);
        }
    }


    private void details(String packageName, String className, String codeSourceFolder, String testSourceFolder) throws IOException {

        ClassName helloWorldClass = ClassName.get(packageName, className);

        FieldSpec helloWorldField = FieldSpec.builder(helloWorldClass, Introspector.decapitalize(className), Modifier.PRIVATE).build();

        MethodSpec add = MethodSpec.methodBuilder("add")
                .addModifiers(Modifier.PUBLIC)
                .returns(int.class)
                .addParameter(int.class, "a")
                .addParameter(int.class, "b")
                .addStatement("return a + b")
                .build();

        MethodSpec testAdd = MethodSpec.methodBuilder("testAdd")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Test.class)
                .returns(void.class)
                .addStatement("int result = $N.add(1,2)", helloWorldField)
                .addStatement("assertThat(result, is(3))")
                .build();

        MethodSpec setUp = MethodSpec.methodBuilder("setUp")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Before.class)
                .returns(void.class)
                .addStatement("$N = new $T()", helloWorldField, helloWorldClass)
                .build();

        TypeSpec helloWorld = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(add)
                .build();
        TypeSpec helloWorldTest = TypeSpec.classBuilder(className + "Test")
                .addModifiers(Modifier.PUBLIC)
                .addMethod(setUp)
                .addMethod(testAdd)
                .addField(helloWorldField)
                .build();


        JavaFile javaFile = JavaFile.builder(packageName, helloWorld)
                .build();
        JavaFile javaTestFile = JavaFile.builder(packageName, helloWorldTest)
                .addStaticImport(CoreMatchers.class, "is")
                .addStaticImport(Assert.class, "assertThat")
                .build();

//        javaFile.writeTo(System.out);
//        javaTestFile.writeTo(System.out);
        javaFile.writeTo(Paths.get(codeSourceFolder));
        javaTestFile.writeTo(Paths.get(testSourceFolder));
    }
}
