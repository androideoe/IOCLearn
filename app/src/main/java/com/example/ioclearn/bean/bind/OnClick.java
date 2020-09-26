package com.example.ioclearn.bean.bind;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by ddup on 2020/9/17.
 * <p>
 * 绑定onClick事件注解
 */
@Retention(RetentionPolicy.RUNTIME) // 运行时注解
@Target(ElementType.METHOD) // 注解类型是方法
public @interface OnClick {
    int[] id();
}
