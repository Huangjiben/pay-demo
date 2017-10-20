package com.example.demo.controller;

import com.example.demo.config.BestpayConfig;
import com.example.demo.model.BestpayData;
import com.example.demo.util.CommonUtil;
import com.example.demo.util.RequestHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

@Controller
@RequestMapping(value = "/bestpay")
public class BestpayController {

    @RequestMapping(value = "doPost", method = RequestMethod.POST)
    public String createForm(HttpServletRequest request, HttpServletResponse response, @ModelAttribute BestpayData bestpayData) throws IOException {
        String orderSeq = bestpayData.getOrderSeq();
        String orderAmount = bestpayData.getOrderAmount();
        String productDes = bestpayData.getProductDes();
        SimpleDateFormat df = new SimpleDateFormat("YYYYMMDD");
        String orderDate = df.format(new Date());
        SortedMap<String, String> packageMap = new TreeMap<String, String>();
        packageMap.put("MERCHANTID",BestpayConfig.MERCHANTID);
        packageMap.put("ORDERSEQ",orderSeq);
        packageMap.put("ORDERDATE",orderDate);
        packageMap.put("ORDERAMOUNT",orderAmount);
        packageMap.put("KEY",BestpayConfig.KEY);
        RequestHandler reqHandler = new RequestHandler(request, response);
        //获取MD5签名摘要
        String digest = reqHandler.createSign(packageMap);
        String mac = CommonUtil.toHexString(digest); //摘要转化为16进制
        StringBuffer stb = new StringBuffer();
        stb.append("<html>");
        stb.append("<head><title>你的Title</title></head>");
        stb.append("<body onload=\"document.form1.submit()\">");
        stb.append("<form name=\"form1\" method=\"post\" action=\"https://webpaywg.bestpay.com.cn/payWeb.do\" >");
        stb.append(String.format("<input name=\"MERCHANTID\" type=\"hidden\" value=\"%s\">", BestpayConfig.MERCHANTID)); //商戶号
        stb.append(String.format("<input name=\"ORDERSEQ\" type=\"hidden\" value=\"%s\">", orderSeq)); //订单号
        stb.append(String.format("<input name=\"ORDERREQTRANSEQ\" type=\"hidden\" value=\"%s\">", orderSeq)); //订单请求交易流水号
        stb.append(String.format("<input name=\"ORDERDATE\" type=\"hidden\" value=\"%s\">", orderDate)); //订单日期
        stb.append(String.format("<input name=\"ORDERAMOUNT\" type=\"hidden\" value=\"%s\">", orderAmount)); //订单总金额
        stb.append(String.format("<input name=\"PRODUCTAMOUNT\" type=\"hidden\" value=\"%s\">", orderAmount)); //产品金额
        stb.append("<input name=\"ATTACHAMOUNT\" type=\"hidden\" value=\"0\">"); //附加金额
        stb.append("<input name=\"CURTYPE\" type=\"hidden\" value=\"RMB\">"); //币种
        stb.append(String.format("<input name=\"ENCODETYPE\" type=\"hidden\" value=\"%s\">", BestpayConfig.ENCODETYPE_MD5)); //加密方式 MD5
        stb.append(String.format("<input name=\"MERCHANTURL\" type=\"hidden\" value=\"%s\">", BestpayConfig.MERCHANTURL)); //前台返回地址
        stb.append(String.format("<input name=\"BACKMERCHANTURL\" type=\"hidden\" value=\"%s\">", BestpayConfig.BACKMERCHANTURL)); //后台返回地址
//        stb.append(String.format("<input name=\"ATTACH\" type=\"hidden\" value=\"%s\">", attach)); //商户附加信息
        stb.append("<input name=\"BUSICODE\" type=\"hidden\" value=\"0001\">"); //业务类型
        stb.append("<input name=\"PRODUCTID\" type=\"hidden\" value=\"08\">"); // 业务标识
//        stb.append(String.format("<input name=\"TMNUM\" type=\"hidden\" value=\"%s\">", tmNum)); //终端号码
//        stb.append(String.format("<input name=\"CUSTOMERID\" type=\"hidden\" value=\"%s\">", customerId)); //客户标识
        stb.append(String.format("<input name=\"PRODUCTDESC\" type=\"hidden\" value=\"%s\">", productDes)); //产品描述
        stb.append(String.format("<input name=\"MAC\" type=\"hidden\" value=\"%s\">", mac)); //MAC校验域
//        stb.append(String.format("<input name=\"CLIENTIP\" type=\"hidden\" value=\"%s\">", clientIp)); //客户端IP
        stb.append("</form></body>");
        stb.append("</html>");
        //入库、记录日志等
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.write(stb.toString());
        return "";
    }
}
