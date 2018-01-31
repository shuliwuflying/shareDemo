package com.flying.libprocessor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * Created by shuliwu on 2018/1/17.
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes("com.flying.libprocessor.CustomAnnotation")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class MyProcessor extends AbstractProcessor {
    Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
//        StringBuilder builder = new StringBuilder();
//        builder.append("package com.flying.libprocessor.generated; \n\n")
//                .append("public class GeneratedClass {\n\n")
//                .append("\t public String getMessage() {\n")
//                .append("\t\treturn \"");
//        for(Element element:roundEnvironment.getElementsAnnotatedWith(CustomAnnotation.class)) {
//            String objectType = element.getSimpleName().toString();
//            builder.append(objectType).append(" says hello!");
//        }
//        builder.append("\";\n");
//        builder.append("\t}\n");
//        builder.append("}\n");
//
//        try{
//            JavaFileObject source = processingEnv.getFiler().createSourceFile("com.flying.libprocessor.generated.GeneratedClass");
//            Writer writer = source.openWriter();
//            writer.write(builder.toString());
//            writer.flush();
//            writer.close();
//        }catch (IOException e) {
//            e.printStackTrace();
//        }
//        return false;

        //添加方法的名称
        MethodSpec main = MethodSpec
                .methodBuilder("getMessage")
                .addModifiers(Modifier.PUBLIC) //方法修饰的关键字
                .returns(String.class)
                .addCode("return \"hello java poet\" ;")
                .build();
        //添加类的名称
        TypeSpec hello = TypeSpec.classBuilder("GeneratedClass")
                .addModifiers(Modifier.PUBLIC)
                .addMethod(main)
                .build();

        //生成类的包名
        String packageName = "com.flying.libprocessor.generated";
        //在控制台输出
        JavaFile file = JavaFile.builder(packageName, hello).build();
        try {
            file.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
