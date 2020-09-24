package com.example.ioclearn.utils;

import com.example.ioclearn.bean.Person;
import com.example.ioclearn.bean.Student;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by ddup on 2020/9/16.
 * <p>
 * 反射例子
 */
public class AnnotationUtils {
    public static void test(Class<?> clazz) {

        for (Field field : clazz.getFields()) {
            if (field.isAnnotationPresent(Student.Name.class)
                    && field.isAnnotationPresent(Student.Gender.class)) {
                Student.Name name = field.getAnnotation(Student.Name.class);
                Student.Gender gender = field.getAnnotation(Student.Gender.class);
                System.out.println("name = " + name.value() + " gender = " + gender.value());

            }
        }

        try {
            Class<Person> personClass = Person.class;
            Method[] methods = personClass.getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(Student.SaveMoney.class)) {
                    Student.SaveMoney saveMoney = method.getAnnotation(Student.SaveMoney.class);
                    System.out.println("saveMoney-> money = " + saveMoney.money()
                            + " term = " + saveMoney.term() + " paltform = " + saveMoney.platform());
                    method.invoke(personClass.newInstance(), 2000, "手机支付");
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

    }
}
