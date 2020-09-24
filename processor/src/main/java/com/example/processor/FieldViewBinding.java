package com.example.processor;

import javax.lang.model.element.Element;

/**
 * 用来记录字段的绑定信息
 */
final class FieldViewBinding {
    private final String name; // 字段名
    private final String type; // 字段类型
    private final int id; // 字段对应的View id
    private  Element element;

    FieldViewBinding(String name, String type, int id) {
        this.name = name;
        this.type = type;
        this.id = id;
    }

    public FieldViewBinding(String name, String type, int id, Element element) {
        this.name = name;
        this.type = type;
        this.id = id;
        this.element = element;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public Element getElement() {
        return element;
    }
}
