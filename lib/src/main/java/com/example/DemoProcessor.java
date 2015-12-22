package com.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * Created by John on 2015/12/21.
 */
@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class DemoProcessor extends AbstractProcessor {
    int i = 0;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement type : annotations) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "type:" + type.getQualifiedName());
            for (Element e : roundEnv.getElementsAnnotatedWith(type)) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "Element:" + e.getSimpleName());
            }

        }
//        try {
//            writeFile("" + (i++));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return true;
    }

    private void writeFile(String name) throws IOException {
        JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile("ProcessTest" + name);
        PrintWriter out = new PrintWriter(sourceFile.openWriter());
        int i = name.lastIndexOf(".");
        if (i > 0) {
            out.print("package ");
            out.print(name.substring(0, i));
            out.print(";");
        }
        out.print("public class ");
        out.print("ProcessTest" + name);
        out.print("{");
        out.print("   public void test(){System.out.println(\"WTF\");}");
        out.print("}");
        out.close();
    }
}
