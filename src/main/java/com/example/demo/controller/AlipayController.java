package com.example.demo.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.example.demo.config.AlipayConfig;
import com.example.demo.model.AlipayData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Controller
@RequestMapping(value = "/alipay")
public class AlipayController {

    @RequestMapping(value = "/index")
    public String entrance() {
        return "./alipay/index";
    }

    @RequestMapping(value = "/applyPay")
    public String viewPay() {
        return "./alipay/pay";
    }

    @RequestMapping("/gotoRefund")
    public String refundView() {
        return "./alipay/refund";
    }

    @RequestMapping(value = "/doPay", method = RequestMethod.POST)
    public String doPost(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("form") AlipayData alipayData) throws IOException {
        if (alipayData.getOut_trade_no() != null) {
            // 商户订单号，商户网站订单系统中唯一订单号，必填
            String out_trade_no = null;
            out_trade_no = new String(alipayData.getOut_trade_no().getBytes("ISO-8859-1"), "UTF-8");

            // 订单名称，必填
            String subject = alipayData.getSubject();
            System.out.println(subject);
            // 付款金额，必填
            String total_amount = new String(alipayData.getTotal_amount().getBytes("ISO-8859-1"), "UTF-8");
            // 商品描述，可空
            String body = alipayData.getBody();

            // 超时时间 可空
            String timeout_express = "2m";
            // 销售产品码 必填
            String product_code = "QUICK_WAP_PAY";

            /**********************/
            // SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
            //调用RSA签名方式
            AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE);
            AlipayTradeWapPayRequest alipay_request = new AlipayTradeWapPayRequest();

            // 封装请求支付信息
            AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
            model.setOutTradeNo(out_trade_no);
            model.setSubject(subject);
            model.setTotalAmount(total_amount);
            model.setBody(body);
            model.setTimeoutExpress(timeout_express);
            model.setProductCode(product_code);
            alipay_request.setBizModel(model);
            // 设置异步通知地址
            alipay_request.setNotifyUrl(AlipayConfig.notify_url);
            // 设置同步地址
            alipay_request.setReturnUrl(AlipayConfig.return_url);

            // form表单生产
            String form = "";
            // 调用SDK生成表单
            try {
                form = client.pageExecute(alipay_request).getBody();
                String newForm = form.replace("<script>document.forms[0].submit();</script>", "");//删除alipay sdk生成的script，方便可在微信上处理
                System.out.println(newForm);
                response.setContentType("text/html;charset=" + AlipayConfig.CHARSET);
                response.getWriter().println(newForm);//直接将完整的表单html输出到页面
//                response.getWriter().flush();
//                response.getWriter().close();
            } catch (AlipayApiException e) {
                e.printStackTrace();
            }
        }
        return "./alipay/onload";
    }

    @RequestMapping(value = "/notify", method = RequestMethod.POST)
    public void alipayNotify(HttpServletRequest request, HttpServletResponse response) throws IOException, AlipayApiException {
        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        }
        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
        //商户订单号

        String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
        //支付宝交易号

        String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

        //交易状态
        String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");

        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
        //计算得出通知验证结果
        //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
        boolean verify_result = AlipaySignature.rsaCheckV1(params, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.CHARSET, "RSA2");

        if (verify_result) {//验证成功
            //////////////////////////////////////////////////////////////////////////////////////////
            //请在这里加上商户的业务逻辑程序代码

            //——请根据您的业务逻辑来编写程序（以下代码仅作参考）——

            if (trade_status.equals("TRADE_FINISHED")) {
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //如果签约的是可退款协议，退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
                //如果没有签约可退款协议，那么付款完成后，支付宝系统发送该交易状态通知。
            } else if (trade_status.equals("TRADE_SUCCESS")) {
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //如果签约的是可退款协议，那么付款完成后，支付宝系统发送该交易状态通知。
            }

            //——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
//            out.clear();
//            out.println("success");	//请不要修改或删除
            response.getWriter().println("success");

            //////////////////////////////////////////////////////////////////////////////////////////
        } else {//验证失败
//            out.println("fail");
            response.getWriter().println("fail");
        }
    }

    //支付宝页面跳转同步通知
    @RequestMapping(value = "/return", method = RequestMethod.GET)
    public String transfer(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) throws AlipayApiException, IOException {
        //获取支付宝GET过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
        //商户订单号

        String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");

        //支付宝交易号

        String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
        //计算得出通知验证结果
        //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
        boolean verify_result = AlipaySignature.rsaCheckV1(params, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.CHARSET, "RSA2");

        if (verify_result) {//验证成功
            //////////////////////////////////////////////////////////////////////////////////////////
            //请在这里加上商户的业务逻辑程序代码
            //该页面可做页面美工编辑
//            out.clear();
//            out.println("验证成功<br />");
//            response.getWriter().println("验证成功<br />");
            //——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
            map.put("resultCode", "ok");
            //////////////////////////////////////////////////////////////////////////////////////////
        } else {
            //该页面可做页面美工编辑
//            out.clear();
//            out.println("验证失败");
//            response.getWriter().println("验证失败");
            map.put("resultCode", "fail");
        }
        return "./alipay/alipayResult";
    }

    @RequestMapping(value = "/weixinLead")
    public String weixinLead(HttpServletRequest request, Map<String, Object> map) {
        map.put("goto", request.getParameter("goto"));
        return "./alipay/weixinLead";
    }

    @RequestMapping(value = "/refund", method = RequestMethod.POST)
    public String refund(@ModelAttribute("form") AlipayData alipayData, Map<String, Object> map) throws AlipayApiException {
        if (alipayData.getOut_trade_no() != null || alipayData.getTrade_no() != null) {
//商户订单号和支付宝交易号不能同时为空。 trade_no、  out_trade_no如果同时存在优先取trade_no
//商户订单号，和支付宝交易号二选一
            String out_trade_no = alipayData.getOut_trade_no();
//支付宝交易号，和商户订单号二选一
            String trade_no = alipayData.getTrade_no();
//退款金额，不能大于订单总金额
            String refund_amount = alipayData.getRefund_amount();
//退款的原因说明
            String refund_reason = alipayData.getRefund_reason();
//标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传。
            String out_request_no = alipayData.getOut_request_no();
/**********************/
// SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
            AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE);
            AlipayTradeRefundRequest alipay_request = new AlipayTradeRefundRequest();

            AlipayTradeRefundModel model = new AlipayTradeRefundModel();
            model.setOutTradeNo(out_trade_no);
            model.setTradeNo(trade_no);
            model.setRefundAmount(refund_amount);
            model.setRefundReason(refund_reason);
            model.setOutRequestNo(out_request_no);
            alipay_request.setBizModel(model);

            AlipayTradeRefundResponse alipay_response = client.execute(alipay_request);
            System.out.println(alipay_response.getBody());
            map.put("refundCode", "ok");
        } else {
            map.put("refundCode", "fail");
        }
        return "./alipay/refundResult";
    }
}
