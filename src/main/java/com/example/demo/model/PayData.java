package com.example.demo.model;

public class PayData {
    private Integer payDataId;
    private String orderId; //商户订单号
    private String prePayId; //预支付订单号
    private String transaction_id; //微信订单号
    private String total_fee; //支付总金额
    private String refund_fee; //退款金额
    private String opendId; //微信公众号用户id
    private String isRefund; //是否退款  Y 代表此交易已退款，N 或者null代表没有退款
    private String isPay; //是否支付

    public String getIsPay() {
        return isPay;
    }

    public void setIsPay(String isPay) {
        this.isPay = isPay;
    }

    public String getIsRefund() {
        return isRefund;
    }

    public void setIsRefund(String isRefund) {
        this.isRefund = isRefund;
    }

    public String getOpendId() {
        return opendId;
    }

    public void setOpendId(String opendId) {
        this.opendId = opendId;
    }

    public Integer getPayDataId() {
        return payDataId;
    }

    public void setPayDataId(Integer payDataId) {
        this.payDataId = payDataId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPrePayId() {
        return prePayId;
    }

    public void setPrePayId(String prePayId) {
        this.prePayId = prePayId;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public String getRefund_fee() {
        return refund_fee;
    }

    public void setRefund_fee(String refund_fee) {
        this.refund_fee = refund_fee;
    }
}
