//package com.example.ioclearn;
//
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Repeatable;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//
///**
// * Created by ddup on 2020/9/16.
// */
//public class Person {
//    @Target(ElementType.TYPE_PARAMETER)
//    public @interface Animal{};
//
//    public class zoo<@Animal T>{
//
//    }
//
//    @Target(ElementType.TYPE_USE)
//    public interface UserTest{}
//
//    String content;
//
//    String testStr = (@UserTest String)content;
//
//    @Retention(RetentionPolicy.RUNTIME)
//    @Target(ElementType.TYPE)
//    public @interface Student{
//        String name();
//    }
//
//    // 定义一个容器注解
//    @Retention(RetentionPolicy.RUNTIME)
//    @Target(ElementType.TYPE)
//    public @interface Students{
//        Student[] value();
//    }
//
//    // 使用
//    @Students({@Student(name = "jack"),@Student(name = "mark")})
//    public  class StudentTest{
//
//    }
//
//
//    // java8
//    @Retention(RetentionPolicy.RUNTIME)
//    @Target(ElementType.TYPE)
//    @Repeatable(@Student.class)
//    public @interface Student {
//        String name();
//    }
//
//    @Student(name = "jack")
//    @Student(name = "kotlin")
//    public class test {
//
//    }
//
//    // java 反射
//
//    // 1.获取类
//    // a. Class.forName()
//    Class stdClz = Class.forName("com.example.ioclearn.Person");
//    // b. Class.forName()
//    Class stdClz1 = Person.class;
//    // c. new object()
//    Person person = new Person();
//    Class stdClz2 = person.getClass();
//
//    // 2.创建对象
//    stdClz.newInstance();
//
//
//}
//
