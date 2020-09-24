package com.example.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * Created by ddup on 2020/9/20.
 */
final class ViewBinding {
    private final List<FieldViewBinding> fieldViewBindings = new ArrayList<FieldViewBinding>();
    private final List<MethodViewBinding> methodViewBindings = new ArrayList<MethodViewBinding>();
    private final String classPackage; //包名
    private final String className;//类名
    private final String targetClass;//等待绑定的类

    ViewBinding(String classPackage, String className, String targetClass) {
        this.classPackage = classPackage;
        this.className = className;
        this.targetClass = targetClass;
    }

    void addField(FieldViewBinding binding) {
        fieldViewBindings.add(binding);
    }

    void addMethod(MethodViewBinding method) {
        methodViewBindings.add(method);
    }

    // Fully Qualified Class Name
    String getFqcn() {
        return classPackage + "." + className;
    }

    // java源文件内容(APT方式）
    String brewJava() {
        StringBuilder builder = new StringBuilder();
        builder.append("package ").append(classPackage).append(";\n\n");
        builder.append("import android.view.View;\n");
        builder.append("import androidx.appcompat.app.AppCompatActivity;\n");
        builder.append("import com.example.injectknife.Injectknife.ActivityBinder;\n");
        builder.append('\n');
        builder.append("public class ").append(className).append(" implements ActivityBinder<").append(targetClass).append("> {\n\n");
        builder.append("@Override ").append("\n");
        builder.append(String.format("public void bind(final %s target) {\n", targetClass));
        builder.append("    View view;\n");

        // 绑定view
        for (FieldViewBinding fieldViewBinding : fieldViewBindings) {
            builder.append(String.format("    view = target.findViewById(%d);\n", fieldViewBinding.getId()));
            builder.append(String.format("    target.%s = (%s)view;\n", fieldViewBinding.getName(),
                    fieldViewBinding.getType()));
        }

        // 绑定onClick事件
        for (MethodViewBinding methodViewBinding : methodViewBindings) {
            builder.append(String.format("    view = target.findViewById(%d);\n", methodViewBinding.getValue()));
            builder.append("    view.setOnClickListener(new View.OnClickListener()  {\n");
            builder.append("      @Override ").append("\n");
            builder.append("      public void onClick(View v) {\n");
            builder.append(String.format("          target.%s(v);\n", methodViewBinding.getMethodName()));
            builder.append("    }\n");
            builder.append("    });\n");
        }


        builder.append("  }\n");
        builder.append("}\n");
        return builder.toString();
    }

    // JavaPoet方式生成代码
    public TypeSpec generateFinder(TypeElement mClassElement) {

        MethodSpec.Builder binderMethodBuilder = MethodSpec.methodBuilder("bind") // bind方法名
                .addModifiers(Modifier.PUBLIC) // 修饰方法的关键字
                .addAnnotation(Override.class) // Override注解
                .addParameter(TypeName.get(mClassElement.asType()), "target", Modifier.FINAL) // 参数类型 参数名 final修饰
                .addStatement("$T view", TypeUtils.ANDROID_VIEW); // 方法体 View view;


        // 绑定view
        for (FieldViewBinding fieldViewBinding : fieldViewBindings) {
            binderMethodBuilder.addStatement("view  = target.findViewById($L)", fieldViewBinding.getId());
            binderMethodBuilder.addStatement("target.$N  = ($T)view", fieldViewBinding.getName(),
                    ClassName.get(fieldViewBinding.getElement().asType()));
        }

        // 绑定onClick事件
        for (MethodViewBinding methodViewBinding : methodViewBindings) {
            TypeSpec anonymous = brewAnonymousClass(methodViewBinding.getMethodName());
            binderMethodBuilder.addStatement("target.findViewById($L).setOnClickListener($L)",
                    methodViewBinding.getValue(), anonymous);  // target.findViewById(xxx).setOnClickListener(listener);
        }
        // xxx$$ViewBinder是类名，其中xxx是当前引入注解的类名,比如:MainActivity$$ViewBinder
        return TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC) //修饰的关键字，这里是public
                .addSuperinterface(ParameterizedTypeName.get(TypeUtils.INJECT_BINDER,
                        TypeName.get(mClassElement.asType())))
                .addMethod(binderMethodBuilder.build()) // 在类中添加方法
                .build();

    }

    private TypeSpec brewAnonymousClass(String methodName) {
        return TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(TypeUtils.ANDROID_ON_CLICK_LISTENER)
                .addMethod(MethodSpec.methodBuilder("onClick") // onClick代表方法名
                        .addAnnotation(Override.class) // override注解
                        .addModifiers(Modifier.PUBLIC) // 修饰的关键字，这里是public
                        .addParameter(TypeUtils.ANDROID_VIEW, "view") // 名为view的参数
                        .returns(TypeName.VOID) //返回值为void
                        .addStatement("target.$N($N)",
                                methodName, "view").build()) // 添加代码
                .build();
    }
}
