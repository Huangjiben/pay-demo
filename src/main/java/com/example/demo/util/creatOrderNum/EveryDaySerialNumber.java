package com.example.demo.util.creatOrderNum;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


abstract class EveryDaySerialNumber extends SerialNumber {

    protected final static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    protected DecimalFormat df = null;

    public EveryDaySerialNumber(int width) {
        if (width < 1) {
            throw new IllegalArgumentException("Parameter length must be great than 1!");
        }
        char[] chs = new char[width];
        for (int i = 0; i < width; i++) {
            chs[i] = '0';
        }
        df = new DecimalFormat(new String(chs));
    }

    protected String process() {
        Date date = new Date();
        int n = getOrUpdateNumber(date, 1);
        return format(date) + format(n);
    }

    protected String format(Date date) {
        return sdf.format(date);
    }

    protected String format(int num) {
        return df.format(num);
    }

    protected abstract int getOrUpdateNumber(Date current, int start);
}