package com.example.processor;

import com.example.annotations.BindView;
import com.example.annotations.OnClick;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

// 使用@AutoService注解,避免了在resource文件夹中注册的步骤
@AutoService(Processor.class)
// 允许/支持的注解类型，让注解处理器处理（新增annotation module）
@SupportedAnnotationTypes({"com.example.annotations.*"})
// 指定JDK编译版本
//@SupportedSourceVersion(SourceVersion.RELEASE_8)

public class InjectProcessor extends AbstractProcessor {

    // 操作Element工具类 (类、函数、属性都是Element)
    private Elements elementUtils;
    // type(类信息)工具类，包含用于操作TypeMirror的工具方法
    private Types typeUtils;
    // Messager用来报告错误，警告和其他提示信息
    private Messager messager;
    // 文件生成器 类/资源，Filter用来创建新的源文件，class文件以及辅助文件
    private Filer filer;
    // 文件名后缀
    public static final String SUFFIX = "$$ViewBinder";

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        elementUtils = processingEnvironment.getElementUtils();
        messager = processingEnvironment.getMessager();
        filer = processingEnvironment.getFiler();

        // 通过ProcessingEnvironment去获取build.gradle(app module)传过来的参数
//        String content = processingEnvironment.getOptions().get("content");
//        messager.printMessage(Diagnostic.Kind.NOTE, content);
    }

    /**
     * 注册感兴趣的注解类
     */
//    @Override
//    public Set<String> getSupportedAnnotationTypes() {
//        Set<String> annotations = new LinkedHashSet<>();
//        annotations.add(BindView.class.getCanonicalName());
//        annotations.add(OnClick.class.getCanonicalName());
//        return annotations;
//    }

    /**
     * 返回使用的java版本
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (set.isEmpty()) return false;

        messager.printMessage(Diagnostic.Kind.NOTE, "开始process：");

        Map<TypeElement, ViewBinding> targetClassMap = new LinkedHashMap<TypeElement, ViewBinding>();
        parseFindView(roundEnvironment, targetClassMap);
        messager.printMessage(Diagnostic.Kind.NOTE, "parseFindView...end");
        parseMethodView(roundEnvironment, targetClassMap);
        messager.printMessage(Diagnostic.Kind.NOTE, "parseMethodView...end");

        // 为每一个类生成 Bind 工具类
        for (Map.Entry<TypeElement, ViewBinding> entry : targetClassMap.entrySet()) {
            TypeElement typeElement = entry.getKey();
            ViewBinding viewBinding = entry.getValue();
            try {
                // APT方式
//                JavaFileObject jfo = filer.createSourceFile(viewBinding.getFqcn(), typeElement);
//                Writer writer = jfo.openWriter();
//                writer.write(viewBinding.brewJava());
//                writer.flush();
//                writer.close();
                // JavaPoet方式
                JavaFile file = JavaFile
                        .builder(getPackageName(typeElement), viewBinding.generateFinder(typeElement))
                        .build();
                file.writeTo(filer);
                return true;
            } catch (IOException e) {
                messager.printMessage(Diagnostic.Kind.ERROR, e.toString());
                e.printStackTrace();
            }
        }


        return true;
    }

    private void parseMethodView(RoundEnvironment roundEnvironment, Map<TypeElement, ViewBinding> targetClassMap) {
        // 获取所有OnClick注解的类节点
        for (Element element : roundEnvironment.getElementsAnnotatedWith(OnClick.class)) {
            // 判断注解是否在方法上
            if (!(element instanceof ExecutableElement) || element.getKind() != ElementKind.METHOD) {
                throw new IllegalStateException("@OnClick annotation must be on a method.");
            }

            messager.printMessage(Diagnostic.Kind.NOTE, "被注解的parseMethodView类有："
                    + element.getSimpleName().toString());

            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
            // 添加方法绑定
            ViewBinding viewBinding = targetClassMap.get(enclosingElement);
            if (viewBinding == null) {
                String classPackage = getPackageName(element);
                String className = enclosingElement.getSimpleName().toString() + SUFFIX;
                String targetType = enclosingElement.getQualifiedName().toString();
                viewBinding = new ViewBinding(classPackage, className, targetType);
                targetClassMap.put(enclosingElement, viewBinding);
            }

            // 获取方法的参数列表
            ExecutableElement executableElement = (ExecutableElement) element;
            List<? extends VariableElement> methodParameters = executableElement.getParameters();
            // 获取方法参数的个数
            int methodParameterSize = methodParameters.size();

            int[] values = element.getAnnotation(OnClick.class).value();
            if (values.length == 0) continue;

            // 获取注解上的方法名
            String methodName = element.getSimpleName().toString();

            // 添加方法绑定
            for (int value : values) {
                MethodViewBinding method = new MethodViewBinding(methodName, value, methodParameterSize == 1);
                viewBinding.addMethod(method);
            }

        }
    }

    private void parseFindView(RoundEnvironment roundEnvironment, Map<TypeElement, ViewBinding> targetClassMap) {
        // 获取所有BindView注解的类节点
        for (Element element : roundEnvironment.getElementsAnnotatedWith(BindView.class)) {
            // 判断注释 是否在 字段上
            if (!(element instanceof VariableElement) || element.getKind() != ElementKind.FIELD) {
                throw new IllegalStateException("@BindView annotation must be on a field.");
            }

            messager.printMessage(Diagnostic.Kind.NOTE, "被注解的parseFindView类有："
                    + element.getSimpleName().toString());

            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
            ViewBinding viewBinding = targetClassMap.get(enclosingElement);

            if (viewBinding == null) {
                String classPackage = getPackageName(enclosingElement);
                String className = enclosingElement.getSimpleName().toString() + SUFFIX;
                String targetType = enclosingElement.getQualifiedName().toString();
                viewBinding = new ViewBinding(classPackage, className, targetType);
                targetClassMap.put(enclosingElement, viewBinding);
            }

            // 获取viewId
            int id = element.getAnnotation(BindView.class).value();
            // 字段名称
            String name = element.getSimpleName().toString();
            // 字段类型
            String type = element.asType().toString();

            FieldViewBinding binding = new FieldViewBinding(name, type, id, element);

            // 将绑定信息添加到对应的类中
            viewBinding.addField(binding);
        }
    }

    // 动态生成的 ViewBinder 包名
    private String getPackageName(Element element) {
        return elementUtils.getPackageOf(element).getQualifiedName().toString();
    }

    // 动态生成的 ViewBinder 所在类名
    private static String getClassName(TypeElement type, String packageName) {
        int packageLen = packageName.length() + 1;
        return type.getQualifiedName().toString().substring(packageLen).replace('.', '$');
    }
}