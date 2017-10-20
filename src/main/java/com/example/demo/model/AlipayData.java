package com.example.demo.model;

public class AlipayData {
    private String out_trade_no; //商户订单号
    private String subject;       //订单名称
    private String total_amount; //订单金额
    private String body;          //订单描述
    private String is_weixin;       //是否是微信浏览器
    private String trade_no;        //支付宝订单号
    private String refund_amount;        //退款金额
    private String refund_reason;        //退款原因
    private String out_request_no;        //退款单号

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getIs_weixin() {
        return is_weixin;
    }

    public void setIs_weixin(String is_weixin) {
        this.is_weixin = is_weixin;
    }

    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

    public String getRefund_amount() {
        return refund_amount;
    }

    public void setRefund_amount(String refund_amount) {
        this.refund_amount = refund_amount;
    }

    public String getRefund_reason() {
        return refund_reason;
    }

    public void setRefund_reason(String refund_reason) {
        this.refund_reason = refund_reason;
    }

    public String getOut_request_no() {
        return out_request_no;
    }

    public void setOut_request_no(String out_request_no) {
        this.out_request_no = out_request_no;
    }
}
