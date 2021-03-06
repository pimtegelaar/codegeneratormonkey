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
        if (args.length < 4) {
            System.out.println("Expecting these arguments:");
            System.out.println("- package name");
            System.out.println("- class name");
            System.out.println("- project root location");
            System.out.println("- code source folder");
            System.out.println("- test source folder");
            return;
        }
        String packageName = args[0];
        String className = args[1];

        String root = args[2];
        String codeSourceFolder = root + args[3];
        String testSourceFolder = root + args[4];
        new Poetry().compose(packageName, className, codeSourceFolder, testSourceFolder);
    }

    private void compose(String packageName, String className, String codeSourceFolder, String testSourceFolder) throws IOException {
        for (int i = 10; i < 20; i++) {
            details(packageName, className + String.format("%03d", i), codeSourceFolder, testSourceFolder);
        }
    }

    private void details(String packageName, String className, String codeSourceFolder, String testSourceFolder) throws IOException {

        ClassName helloWorldClass = ClassName.get(packageName, className);

        FieldSpec helloWorldField = FieldSpec.builder(helloWorldClass, Introspector.decapitalize(className), Modifier.PRIVATE).build();

        MethodSpec add = MethodSpec.methodBuilder("isPrime")
                .addModifiers(Modifier.PUBLIC)
                .returns(boolean.class)
                .addParameter(double.class, "n")
                .beginControlFlow("for (int i = 2; 2 * i < n; i++)")
                .beginControlFlow("if (n % i == 0)")
                .addStatement("return false")
                .endControlFlow()
                .endControlFlow()
                .addStatement("return true")
                .build();

        MethodSpec testAdd = MethodSpec.methodBuilder("test104395303isPrime")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Test.class)
                .returns(void.class)
                .addStatement("boolean result = $N.$N(104395303D)", helloWorldField, add)
                .addStatement("assertThat(result, is(true))")
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

        javaFile.writeTo(Paths.get(codeSourceFolder));
        javaTestFile.writeTo(Paths.get(testSourceFolder));
    }
}
