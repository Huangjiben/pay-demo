package com.example.demo.service;

import com.example.demo.mapper.PayDataMapper;
import com.example.demo.model.PayData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ActivityService {
    @Autowired
    private PayDataMapper payDataMapper;
    private final String GZHID = "wxa284cd5f9facc01e";
    private final String GZHSecret = "dd31dc1ce0a35a86d0642acfd5758d25";

    public void insertPayData(PayData payData) {
        payDataMapper.insertPayData(payData);
    }

    public List<PayData> getRefundDataList(String opendId) {
        return payDataMapper.findRefundPayData(opendId);
    }

    public void updateRefund(String orderId, String isRefund) {
        payDataMapper.updateRefund(orderId, isRefund);
    }

    /**
     * 通过微信用户的code换取网页授权access_token
     *
     * @return
     * @throws IOException
     * @throws
     */
    public List<Object> accessToken(String code) throws IOException {
        List<Object> list = new ArrayList<Object>();
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                + GZHID + "&secret=" + GZHSecret + "&code=" + code + "&grant_type=authorization_code";
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        HttpResponse res = client.execute(post);
        if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            HttpEntity entity = res.getEntity();
            String str = EntityUtils.toString(entity, "utf-8");
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> jsonOb = mapper.readValue(str, Map.class);
            list.add(jsonOb.get("access_token"));
            list.add(jsonOb.get("openid"));
        }
        return list;
    }

    public String getPayMonyByOrderId(String orderId) {
        return payDataMapper.getPaymoneyByOrderId(orderId);
    }

    public void updatePayData(String transaction_id, String isPay, String orderId) {
        payDataMapper.updatePayData(transaction_id, isPay, orderId);
    }
}
