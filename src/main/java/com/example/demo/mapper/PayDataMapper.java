package com.example.demo.mapper;


import com.example.demo.model.PayData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PayDataMapper {

    List<PayData> findRefundPayData(String openId);

    void insertPayData(PayData payData);

//    PayData findPayDataByOrderId(String orderId);

    void updateRefund(@Param("orderId") String orderId, @Param("isRefund") String isRefund);

    String getPaymoneyByOrderId(String orderId);

    void updatePayData(@Param("transaction_id") String transaction_id, @Param("isPay") String isPay, @Param("orderId") String orderId);
}

