package com.example.demo.util;

import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * 腾讯相关支付配置类
 * @author liang_ts
 *
 */
public class TenpayConfig {
	
	private static Properties p = null;
	//初始化支付宝资源文件
	private static Properties instancePro(){
		if(p==null){
			try {
				p = PropertiesLoaderUtils.loadAllProperties("pay/ten.properties");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return p;
	}
	
	//应用id
	public final static String appid = instancePro().getProperty("appId");
	//应用密钥
	public final static String appsecret = instancePro().getProperty("appsecret");	
	//微信支付分配的商户号
	public final static String partner = instancePro().getProperty("partner");
	//自定义key用户，签名使用
	public final static String paternerKey = instancePro().getProperty("paternerKey");
	//微信支付完成后通知平台接口地址
	public final static String notify_url = instancePro().getProperty("notify_url");
}
