package com.example.ioclearn.utils;

import android.app.Activity;
import android.view.View;

import com.example.ioclearn.bean.bind.FindView;
import com.example.ioclearn.bean.bind.OnClick;
import com.example.ioclearn.bean.bind.ViewFinder;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by ddup on 2020/9/17.
 */
public class ViewInject {

    public static void bind(Activity activity) {
        inject(new ViewFinder(activity), activity);
    }

    public static void bind(View view) {
        inject(new ViewFinder(view), view);
    }

    public static void bind(View view, Object obj) {
        inject(new ViewFinder(view), obj);
    }

    private static void inject(ViewFinder finder, Object obj) {
        injectFields(finder, obj);
        injectMethods(finder, obj);
    }

    /**
     * 反射获取属性
     *
     * @param finder
     * @param obj
     */
    private static void injectFields(ViewFinder finder, Object obj) {
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields(); // 获取所有属性
        for (Field field : fields) {
            if (field.isAnnotationPresent(FindView.class)) {
                // 获取注解
                FindView findView = field.getAnnotation(FindView.class);
                if (findView.value() < 0) {
                    throw new IllegalArgumentException("The id can't be -1.");
                } else {
                    View view = finder.findViewById(findView.value());
                    field.setAccessible(true);
                    try {
                        field.set(obj, view); // 设置属性
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }


    }

    /**
     * 反射获取方法
     *
     * @param finder
     * @param obj
     */
    private static void injectMethods(ViewFinder finder, final Object obj) {
        Method[] methods = obj.getClass().getDeclaredMethods();
        for (final Method method : methods) {
            if (method.isAnnotationPresent(OnClick.class)) {
                OnClick onClick = method.getAnnotation(OnClick.class);
                if (onClick.id().length != 0) {
                    for (int i : onClick.id()) {
                        View view = finder.findViewById(i);
                        method.setAccessible(true);
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    method.invoke(obj, v);
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                } catch (InvocationTargetException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }
                }
            }
        }
    }

}
