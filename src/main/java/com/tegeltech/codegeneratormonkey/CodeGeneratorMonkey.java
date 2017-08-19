package com.tegeltech.codegeneratormonkey;


import com.tegeltech.codegeneratormonkey.domain.Clazz;
import com.tegeltech.codegeneratormonkey.domain.Member;
import com.tegeltech.codegeneratormonkey.domain.Method;
import com.tegeltech.codegeneratormonkey.domain.Statement;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.String.format;

public class CodeGeneratorMonkey {

    public static final String EXTENSION = ".java";

    public static void main(String[] args) {
        CodeGeneratorMonkey codeGeneratorMonkey = new CodeGeneratorMonkey();
        codeGeneratorMonkey.generateCode();
    }

    private void generateCode() {
        try {
            String sourceFolder = "src/main/java/";
            String packageName = "com.tegeltech.codegeneratormonkey.tmp";
            String fileName = "MyFile";
            String extension = ".java";

            Clazz clazz = new Clazz();
            clazz.setFileName(fileName);
            clazz.setPackageName(packageName);
            Method method = new Method();
            method.setModifiers(Modifier.PUBLIC);
            method.setName("add");
            method.setReturnType(int.class);

            method.setParameterTypes(new Class[]{int.class, int.class});
            method.setStatements(Arrays.asList(new Statement("return arg0+arg1;")));
            clazz.setMethods(Arrays.asList(method));

            String fullName = sourceFolder + packageName.replace(".", "/") + "/" + fileName + extension;
            Path file = Paths.get(fullName);
            List<String> lines =// new ArrayList<>();
                    classLines(clazz);
//            lines.add(format("package %s;", packageName));
//            lines.add(format("public class %s {", fileName));
//            lines.add("}");
            Files.write(file, lines, Charset.forName("UTF-8"));
        } catch (IOException e) {
            // do something
        }
    }

    private void generateCode(Clazz clazz, String sourceFolder) {
        try {
            String packageName = clazz.getPackageName();
            String fileName = clazz.getFileName();
            String fullName = sourceFolder + packageName.replace(".", "/") + "/" + fileName + EXTENSION;
            Path file = Paths.get(fullName);
            List<String> lines = classLines(clazz);
            Files.write(file, lines, Charset.forName("UTF-8"));
        } catch (IOException e) {
            // do something
        }
    }

    private List<String> classLines(Clazz clazz) {
        List<String> lines = new ArrayList<>();
        lines.add(format("package %s;", clazz.getPackageName()));
        lines.add(format("public class %s {", clazz.getFileName()));
        for (Member member : clazz.getMembers()) {
            lines.addAll(memberLines(member));
        }
        for (Method method : clazz.getMethods()) {
            lines.addAll(methodLines(method));
        }
        lines.add("}");
        return lines;
    }

    private List<String> memberLines(Member member) {
        List<String> lines = new ArrayList<>();
        return lines;
    }

    private List<String> methodLines(Method method) {
        List<String> lines = new ArrayList<>();
        String modifiers = Modifier.toString(method.getModifiers());
        Class<?> returnType = method.getReturnType();
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        StringBuilder signature = new StringBuilder();
        signature.append(modifiers);
        signature.append(" ");
        signature.append(returnType);
        signature.append(" ");
        signature.append(methodName);
        signature.append("(");
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> param = parameterTypes[i];
            signature.append(param.getName());
            signature.append(" arg" + i);
            if (i < parameterTypes.length - 1) {
                signature.append(", ");
            }
        }
        signature.append(") {");
        lines.add(signature.toString());
        for (Statement statement : method.getStatements()) {
            lines.add(statement.getStatement());
        }
        lines.add("}");
        return lines;
    }
}
