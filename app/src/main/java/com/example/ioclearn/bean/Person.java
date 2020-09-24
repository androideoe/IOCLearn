package com.example.ioclearn.bean;

/**
 * Created by ddup on 2020/9/16.
 */
public class Person {
    @Student.Name(value = "li si")
    @Student.Gender(value = "man")
    public String name;

    @Student.SaveMoney(money = 20000, term = 8, platform = "ios")
    public void saveMoney(int money, String payType) {
        System.out.println("ping spent " + money + " in foods" + " by " + payType);
    }
}
