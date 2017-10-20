package com.example.demo.util;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.xml.sax.InputSource;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.*;

public class WeChat {
    /**
     * 金额为分的格式 
     */
    public static final String CURRENCY_FEN_REGEX = "\\-?[0-9]+";

    /**
     * 元转换成分
     *
     * @param amount
     * @return
     */
    public static String getMoney(String amount) {
        if (amount == null) {
            return "";
        }
        // 金额转化为分为单位
        String currency = amount.replaceAll("\\$|\\￥|\\,", "");  //处理包含, ￥ 或者$的金额
        int index = currency.indexOf(".");
        int length = currency.length();
        Long amLong = 0l;
        if (index == -1) {
            amLong = Long.valueOf(currency + "00");
        } else if (length - index >= 3) {
            amLong = Long.valueOf((currency.substring(0, index + 3)).replace(".", ""));
        } else if (length - index == 2) {
            amLong = Long.valueOf((currency.substring(0, index + 2)).replace(".", "") + 0);
        } else {
            amLong = Long.valueOf((currency.substring(0, index + 1)).replace(".", "") + "00");
        }
        return amLong.toString();
    }

    /**
     *      * 将分为单位的转换为元 （除100）  
     *      *   
     *      * @param amount  
     *      * @return  
     *      * @throws Exception   
     *      
     */
    public static String changeF2Y(String amount) throws Exception {
        if (!amount.matches(CURRENCY_FEN_REGEX)) {
            throw new Exception("金额格式有误");
        }
        return BigDecimal.valueOf(Long.valueOf(amount)).divide(new BigDecimal(100)).toString();
    }

    /**
     * 获取签名  
     *  @param paramMap  
     *  @param key  
     *  @return  
     */
    public static String getSign(Map<String, String> paramMap, String key) {
        List list = new ArrayList(paramMap.keySet());
        Object[] ary = list.toArray();
        Arrays.sort(ary);
        list = Arrays.asList(ary);
        String str = "";
        for (int i = 0; i < list.size(); i++) {
            str += list.get(i) + "=" + paramMap.get(list.get(i) + "") + "&";
        }
        str += "key=" + key;
        str = MD5Util.MD5Encode(str, "UTF-8").toUpperCase();

        return str;
    }

    /**
     * description: 解析微信通知xml
     *
     * @param xml
     * @return
     * @author ex_yangxiaoyi
     * @see
     */
    @SuppressWarnings({"unused", "rawtypes", "unchecked"})
    public static Map parseXmlToList(String xml) throws Exception {
        Map retMap = new HashMap();
        StringReader read = new StringReader(xml);
        // 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
        InputSource source = new InputSource(read);
        // 创建一个新的SAXBuilder
        SAXBuilder sb = new SAXBuilder();
        // 通过输入源构造一个Document
        Document doc = (Document) sb.build(source);
        Element root = doc.getRootElement();// 指向根节点
        List<Element> es = root.getChildren();
        if (es != null && es.size() != 0) {
            for (Element element : es) {
                retMap.put(element.getName(), element.getValue());
            }
        }
        return retMap;
    }

    /**
     * 处理xml请求信息
     *
     * @param request
     * @return
     */
    public static String getXmlRequest(HttpServletRequest request) {
        java.io.BufferedReader bis = null;
        String result = "";
        try {
            bis = new java.io.BufferedReader(new java.io.InputStreamReader(request.getInputStream()));
            String line = null;
            while ((line = bis.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
