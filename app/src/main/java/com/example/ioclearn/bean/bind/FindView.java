package com.example.ioclearn.bean.bind;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by ddup on 2020/9/17.
 * <p>
 * 绑定view注解
 */
@Retention(RetentionPolicy.RUNTIME) // 运行时注解
@Target(ElementType.FIELD) // 注解类型是属性
public @interface FindView {
    int value() default -1;
}
