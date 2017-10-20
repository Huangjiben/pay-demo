package com.example.demo.controller;


import com.example.demo.config.WxPayConfig;
import com.example.demo.model.PayData;
import com.example.demo.service.ActivityService;
import com.example.demo.service.GetWxOrderno;
import com.example.demo.util.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.util.*;

@Controller
@RequestMapping(value = "/wechat")
public class WechatActivityController {
    private static Logger logger = Logger.getLogger(WechatActivityController.class);
    @Autowired
    private ActivityService activityService;

    /**
     * 用户提交支付，获取微信支付订单接口
     */
    @RequestMapping(value = "/pay")
    public String pay(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) {

	/*------1.获取参数信息------- */
        //商户订单号
        String out_trade_no = request.getParameter("state");
        //价格
        String money = request.getParameter("money");
        //金额转化为分为单位
        String finalmoney = WeChat.getMoney(money);
        //获取用户的code
        String code = request.getParameter("code");

	/*------2.根据code获取微信用户的openId和access_token------- */
        //注： 如果后台程序之前已经得到了用户的openId 可以不需要这一步，直接从存放openId的位置或session中获取就可以。
        //提交的url路径也就不需要再经过微信重定向。写成：http://localhost:8080/项目名/wechat/pay?money=${sumPrice}&state=${orderId}
        String openid = null;
        try {
            List<Object> list = activityService.accessToken(code);
            openid = list.get(1).toString();
        } catch (IOException e) {
            logger.error("根据code获取微信用户的openId出现错误", e);

            return "error";
        }

	/*------3.生成预支付订单需要的的package数据------- */
        //随机数
        String nonce_str = MD5.getMessageDigest(String.valueOf(new Random().nextInt(10000)).getBytes());
        //订单生成的机器 IP
        String spbill_create_ip = request.getRemoteAddr();
        //交易类型 ：jsapi代表微信公众号支付
        String trade_type = "JSAPI";
        //这里notify_url是 微信处理完支付后的回调的应用系统接口url。
        String notify_url = "http://www.ifindu.top/wechat/weixinNotify";

        SortedMap<String, String> packageParams = new TreeMap<String, String>();
        packageParams.put("appid", WxPayConfig.appid);
        packageParams.put("mch_id", WxPayConfig.partner);
        packageParams.put("nonce_str", nonce_str);
        packageParams.put("body", "费用");
        packageParams.put("out_trade_no", out_trade_no);
        packageParams.put("total_fee", finalmoney);
        packageParams.put("spbill_create_ip", spbill_create_ip);
        packageParams.put("notify_url", notify_url);
        packageParams.put("trade_type", trade_type);
        packageParams.put("openid", openid);

	/*------4.根据package数据生成预支付订单号的签名sign------- */
        RequestHandler reqHandler = new RequestHandler(request, response);
        reqHandler.init(WxPayConfig.appid, WxPayConfig.appsecret, WxPayConfig.partnerkey);
        String sign = reqHandler.createSign(packageParams);

	/*------5.生成需要提交给统一支付接口https://api.mch.weixin.qq.com/pay/unifiedorder 的xml数据-------*/
        String xml = "<xml>" +
                "<appid>" + WxPayConfig.appid + "</appid>" +
                "<mch_id>" + WxPayConfig.partner + "</mch_id>" +
                "<nonce_str>" + nonce_str + "</nonce_str>" +
                "<sign>" + sign + "</sign>" +
                "<body><![CDATA[" + "费用" + "]]></body>" +
                "<out_trade_no>" + out_trade_no + "</out_trade_no>" +
                "<total_fee>" + finalmoney + "</total_fee>" +
                "<spbill_create_ip>" + spbill_create_ip + "</spbill_create_ip>" +
                "<notify_url>" + notify_url + "</notify_url>" +
                "<trade_type>" + trade_type + "</trade_type>" +
                "<openid>" + openid + "</openid>" +
                "</xml>";

	/*------6.调用统一支付接口https://api.mch.weixin.qq.com/pay/unifiedorder 生产预支付订单----------*/
        String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        String prepay_id = "";
        try {
            prepay_id = GetWxOrderno.getPayNo(createOrderURL, xml);
            if (prepay_id.equals("")) {
                logger.error("支付错误");
                return "error";
            }
        } catch (Exception e) {
            logger.error("统一支付接口获取预支付订单出错", e);
            return "error";
        }


	/*------7.将预支付订单的id和其他信息生成签名并一起返回到页面 ------- */
        nonce_str = MD5.getMessageDigest(String.valueOf(new Random().nextInt(10000)).getBytes());
        SortedMap<String, String> finalpackage = new TreeMap<String, String>();
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String packages = "prepay_id=" + prepay_id;
        finalpackage.put("appId", WxPayConfig.appid);
        finalpackage.put("timeStamp", timestamp);
        finalpackage.put("nonceStr", nonce_str);
        finalpackage.put("package", packages);
        finalpackage.put("signType", "MD5");
        String finalsign = reqHandler.createSign(finalpackage);
        //保存预支付数据
        PayData payData = new PayData();
        payData.setOpendId(openid);
        payData.setOrderId(out_trade_no);
        payData.setTotal_fee(money);
        payData.setPrePayId(prepay_id);
        activityService.insertPayData(payData);
        map.put("appid", WxPayConfig.appid);
        map.put("timeStamp", timestamp);
        map.put("nonceStr", nonce_str);
        map.put("packageValue", packages);
        map.put("paySign", finalsign);
        map.put("success", "ok");
        map.put("orderId", out_trade_no);
        map.put("total_fee", money);
        return "pay";
    }

    /**
     * 提交支付后的微信异步返回接口
     */
    @RequestMapping(value = "/weixinNotify")
    public void weixinNotify(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) {

        try {
            String resultStr = WeChat.getXmlRequest(request);
            logger.info("支付成功的回调：" + resultStr);
            Map<String, String> resultMap = WeChat.parseXmlToList(resultStr);
            String result_code = (String) resultMap.get("result_code");
            String transaction_id = (String) resultMap.get("transaction_id");
            String total_fee = (String) resultMap.get("total_fee");
            String money = WeChat.changeF2Y(total_fee); //把金额分转为元
            String out_trade_no = (String) resultMap.get("out_trade_no");
            String return_code = (String) resultMap.get("return_code");
            String sign_receive = (String) resultMap.get("sign");
            String return_msg = (String) resultMap.get("return_msg");
            resultMap.remove("sign");
            String checkSign = WeChat.getSign(resultMap, WxPayConfig.partnerkey);
            /* -----------------------          校验签名 --------------------------------             */
            //签名校验正确
            if (checkSign != null && sign_receive != null &&
                    checkSign.equals(sign_receive.trim())) {
                String checkMoney = activityService.getPayMonyByOrderId(out_trade_no);
                //验证支付金额
                if (checkMoney.equals(money)) {
                    //通知微信.异步确认成功.必写.不然微信会一直通知后台.八次之后就认为交易失败了.
                    response.getWriter().write(RequestHandler.setXML("SUCCESS", "OK"));
                    //支付成功保存数据
                    if (return_code.equals("SUCCESS") && result_code.equals("SUCCESS")) {

                        //支付成功后保存支付数据信息
                        activityService.updatePayData(transaction_id, "Y", out_trade_no);

                    }
                    //支付失败
                    else {
                        logger.error("支付失败：" + return_msg);
                    }
                } else {
                    response.getWriter().write(RequestHandler.setXML("FAIL", "check total_fee fail"));
                }
            }
            //签名校验失败
            else {
                //通知微信签名失败
                response.getWriter().write(RequestHandler.setXML("FAIL", "check sign fail"));
            }

        } catch (Exception e) {
            logger.error("微信回调接口出现错误：", e);
        }

    }

    /**
     * 微信公众号申请退款
     *
     * @param orderId   订单id
     * @param total_fee 退款金额
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/refundWXPay", method = {RequestMethod.GET})
    private String refundwx(HttpServletRequest request, HttpServletResponse response, String orderId, String total_fee, Map<String, Object> map) throws Exception {

    	/*--------1.初始化数据参数----------*/
        String appId = WxPayConfig.appid;
        String secret = WxPayConfig.appsecret;
        String shh = WxPayConfig.partner;
        String key = WxPayConfig.partnerkey;
        String filePath = "/home/huang.jiben/cert/apiclient_cert.p12"; //退款需要提供证书数据，所以需要根据证书路径读取证书
        //需要退款的商户订单号，对应提交订单中的out_trade_no

        String refund_fee = total_fee;//这里的可退款金额和总金额一样

        Map<String, String> result = (Map<String, String>) wxRefund(request, response, appId, secret, shh, key, orderId, total_fee, refund_fee, filePath);

        map.put("returncode", result.get("returncode"));
        map.put("returninfo", result.get("returninfo"));
        return "refundResult";
    }

    private Object wxRefund(HttpServletRequest request, HttpServletResponse response, String appId,
                            String secret, String shh, String key, String orderId, String total_fee, String refund_fee, String path) {
        Map<String, String> result = new HashMap<String, String>();
        String refundid = UUIDUtils.get32UUID();
        String nonce_str = MD5.getMessageDigest(String.valueOf(new Random().nextInt(10000)).getBytes());
        String final_total_fee = WeChat.getMoney(total_fee);
        String final_refund_fee = final_total_fee;
        /*----------  1.生成签名需要的的package数据-----------*/
        SortedMap<String, String> packageParams = new TreeMap<String, String>();
        packageParams.put("appid", appId);
        packageParams.put("mch_id", shh);
        packageParams.put("nonce_str", nonce_str);
        packageParams.put("op_user_id", shh);
        packageParams.put("out_trade_no", orderId);
        packageParams.put("out_refund_no", refundid);
        packageParams.put("total_fee", final_total_fee);
        packageParams.put("refund_fee", final_refund_fee);
        /*------2.根据package生成签名sign------- */
        RequestHandler reqHandler = new RequestHandler(request, response);
        reqHandler.init(appId, secret, key);
        String sign = reqHandler.createSign(packageParams);

		/*------3.拼装需要提交到微信的数据xml------- */
        String xml = "<xml>"
                + "<appid>" + appId + "</appid>"
                + "<mch_id>" + shh + "</mch_id>"
                + "<nonce_str>" + nonce_str + "</nonce_str>"
                + "<op_user_id>" + shh + "</op_user_id>"
                + "<out_trade_no>" + orderId + "</out_trade_no>"
                + "<out_refund_no>" + refundid + "</out_refund_no>"
                + "<refund_fee>" + final_refund_fee + "</refund_fee>"
                + "<total_fee>" + final_total_fee + "</total_fee>"
                + "<sign>" + sign + "</sign>"
                + "</xml>";
        try {
             /*--------4.读取证书文件,这一段是直接从微信支付平台提供的demo中copy的，所以一般不需要修改------*/
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            FileInputStream instream = new FileInputStream(new File(path));
            try {
                keyStore.load(instream, shh.toCharArray());
            } finally {
                instream.close();
            }
            // Trust own CA and all self-signed certs
            SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, shh.toCharArray()).build();
            // Allow TLSv1 protocol only
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"}, null,
                    SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();

	    	 /*------5.发送数据到微信的退款接口------- */
            String url = "https://api.mch.weixin.qq.com/secapi/pay/refund";
            HttpPost httpost = HttpClientConnectionManager.getPostMethod(url);
            httpost.setEntity(new StringEntity(xml, "UTF-8"));
            HttpResponse weixinResponse = httpClient.execute(httpost);
            String jsonStr = EntityUtils.toString(weixinResponse.getEntity(), "UTF-8");
            logger.info(jsonStr);

            Map map = GetWxOrderno.doXMLParse(jsonStr);
            if ("success".equalsIgnoreCase((String) map.get("result_code"))) {
                activityService.updateRefund(orderId, "Y");
                logger.info("退款成功");
                result.put("returncode", "ok");
                result.put("returninfo", "退款成功");
            } else {
                logger.info("退款失败");
                result.put("returncode", "error");
                result.put("returninfo", "退款失败");
            }
        } catch (Exception e) {
            logger.info("退款失败");
            result.put("returncode", "error");
            result.put("returninfo", "退款失败");
        }
        return result;

    }


}