package com.example.injectknife;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by ddup on 2020/9/20.
 */
public final class Injectknife {
    public interface ActivityBinder<T extends AppCompatActivity> {
        void bind(T activity);
    }

    public static void bind(AppCompatActivity target) {
        Class<?> targetClass = target.getClass();
        ActivityBinder activityBinder;
        try {
            Class<?> activityBindingClass = Class.forName(targetClass.getName() + "$$ViewBinder");
            activityBinder = (ActivityBinder) activityBindingClass.newInstance();
            activityBinder.bind(target);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
