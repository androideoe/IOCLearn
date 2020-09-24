package com.example.processor;

import com.squareup.javapoet.ClassName;


public class TypeUtils {
    public static final ClassName ANDROID_VIEW
            = ClassName.get("android.view", "View");
    public static final ClassName ANDROID_ON_CLICK_LISTENER
            = ClassName.get("android.view", "View", "OnClickListener");
    public static final ClassName INJECT_BINDER = ClassName.get("com.example.injectknife.Injectknife", "ActivityBinder");

}
