package com.example.demo.util.creatOrderNum;

public abstract class SerialNumber {
    public synchronized String getSerialNumber() {
        return process();
    }

    protected abstract String process();
}
