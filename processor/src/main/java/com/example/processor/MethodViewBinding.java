package com.example.processor;

/**
 * Created by ddup on 2020/9/20.
 */
final class MethodViewBinding {
    private String methodName;
    private int value;
    private boolean hasParam;

    public MethodViewBinding(String methodName, int value, boolean hasParam) {
        this.methodName = methodName;
        this.hasParam = hasParam;
        this.value = value;
    }

    public String getMethodName() {
        return methodName;
    }

    public int getValue() {
        return value;
    }

    public boolean hasParam() {
        return hasParam;
    }
}
