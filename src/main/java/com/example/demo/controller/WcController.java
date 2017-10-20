package com.example.demo.controller;

import com.example.demo.model.PayData;
import com.example.demo.service.ActivityService;
import com.example.demo.util.creatOrderNum.FileEveryDaySerialNumber;
import com.example.demo.util.creatOrderNum.SerialNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping(value = "/wc")
public class WcController {
    @Autowired
    private ActivityService activityService;
//test test1
    @RequestMapping("/toPay")
    public String hello(Map<String, Object> map) {
        SerialNumber serial = new FileEveryDaySerialNumber(5, "EveryDaySerialNumber.dat");
        while (true) {
            map.put("state", serial.getSerialNumber());
            try {
                TimeUnit.SECONDS.sleep(2);
                return "toPay";
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping("/paySuccess")
    public String wxPaySuccess(String orderId, String money, Map<String, Object> map) {
        map.put("orderId", orderId);
        map.put("money", money);
        return "paySuccess";
    }

    @RequestMapping("/refund")
    public String view() {
        return "refund";
    }

    @RequestMapping("/applyRefund")
    public String applyRefund(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) {
        //获取用户的code
        String openid = null;
        String code = request.getParameter("code");
        try {
            List<Object> list = activityService.accessToken(code);
            openid = list.get(1).toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<PayData> payDataList = activityService.getRefundDataList(openid);
        map.put("payDataList", payDataList);
        return "applyRefund";
    }

    @RequestMapping("/test")
    public String test() {
        return "test";
    }
}
