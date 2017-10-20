package com.example.demo.config;

public class AlipayConfig {
    // 商户appid
    public static String APPID = "2017100409118999";
    // 私钥 pkcs8格式的
    public static String RSA_PRIVATE_KEY = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQC30KS1icUzMsjjyigaGDhjeA8YU5a5BCadsnOTL72PeGZucpT5qtVooYOjlqr8LpRvbgWF2KQHzR5wzqSvvohcH6pJO7TYKazQuekMHdXzHY8XJyxFIem7vHzlragBEQDnI3/PCxSPepFKRjWimHS7UziOFNAe4Utb1rBOSuLgDQksZPB4guTw53LbTsuimPg3oGmGTaBZPaZAxk8fIG9bmdWDcX8Aqc+Fdd2UDcw0MjmL7sYOSb3NNHhrcNZLGcvDS0RnOPjF4bSe/cE+Ima1GhvwrMInXjjTltpcKbdYHjnqLTCdijqMjsYKe9lkq1HyVglrq75JY6A6roRG4Ds/AgMBAAECggEAY9lin3qHNJiRp6tYvVQKzX2aNYyygy5OC7u0LG/yeMzeV4DGjBjQdFXIPETL/NEQsLcDKQHVmKdDseOOcij9yY0p4z4brpB0360dewd6AOY9pEYTNjqFzQVGkz0R1gJ2ixgthTpvoMDjy4T3/arzPg6m2gdURnH7jZfBjcYAF9QQznDppJD8hJjeNi96sQyo9hKSNM8+NBcRxzMmOQ8QUEh+ZxNqrzlB6OsSErEbXZ2MmyaXMvELr/DRgIUhEHnyhbWQBRESPwbtIGAE61jTeXC22Rb5BrZ+pv/ySeOd8uOC7ReX+xGY47xkykwJ0Vec6TiJuoIdpKbfFly2hrySAQKBgQDnVUqBAbXbXShYdrkuHMON6WaCsSFNWCjIijzIR0opb/6cAX2cl4J31hBWvMS3IrAZDRhaOEUVL4Ar8U0B2vSI6udgIMrvAmyokm7yHdaalb6MPX4BtQlMsa/SzDAkDvDMeatVerGwKWr+LLtPxDsdbeL+wWF+r5pxHPXpNQiDLwKBgQDLaj9s5Lfb+YhQ77Q2xhjhmWPWcRBYCvA8aq4EoHT7trtzJ2PvvQBUkB9+V8fF6z7cUdPfg5QkwSquYVruK5uh4leKHBTRJ7hh8YpIqKdj+vhECZsBH/R3e8V7PPoYIQMo+gnuVNOQj9vPLoP9DAFrKb71ILZ1KxHB8prxkTAE8QKBgQCKVx8u9u7iOFugS5+CTAFGJIYBMhxQPKBnDQQ8YSWgkjEY6mtL8e876u/qbwjU3BGjaYleqrfla3qizLLYoTALkS689d9l/3UreiaO8/kaiOJiy4NHUhQ1oS0cSq+fYK/wF/K2M6F/T8WEKwv5L8iKWOSepNbBBBLAAimtROHKUQKBgQDGA66sXVXRsG6Pg1hmPZ749e3nUXb8jeFgnuS5ok1ev3poVb8GIHFS4F2YCvFw7Fjr9kgO/DJZOSVITYNL+G1vQDvBOSbWmBj4Vz4rDuqr/CJzcPpQNQAw0+7g0fIPTQubdxP1yuOMhU4PuBNKYMZ8wE2gp/CVzJBjwbwtTg11sQKBgQDRD+UWWiLo+KePcvVpoTb3ZKQ/vzLvGjQu9epIeHG6bXMub445Ua6J508bADpxpGcSb9qSgOD4uIJuFElUJUV/w5sKGIc1yu+P5+qz3sxoKWYYO8vUxTuwz9RvKJDn2whkCvbimPCngghhWniiXKuiJparqLDT+f76U21OMFUg4A==";
    // 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://www.ifindu.top/alipay/notify";
    // 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
    public static String return_url = "http://www.ifindu.top/alipay/return";
    // 请求网关地址
    public static String URL = "https://openapi.alipay.com/gateway.do";
    // 编码
    public static String CHARSET = "UTF-8";
    // 返回格式
    public static String FORMAT = "json";
    // 支付宝公钥
    public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAp9y9L8beyPhSMuLCph88qSOoI5lC8rQ96XbKXvjOibtJv4r7vbM6ApvOxLoJhyoEaTaL0BfHT8oraGTrWzHu32Lfp0hwZsI+F8QnZ9k1TVj3hZR6gPaMxb5xozOJyLRR1oPEREE7nxhnDUJ0BvtgjUmQpn3egnt+bjvgytdsqel5+S2/KmuiEbCjQbWh6Tztte0YIPmNa5p2qRuWj9eM1QXcFZqwHxepfbpQIVlOY6ls6FVKyxLdAVnGfzFI9nKbvn1VHAOdH2QQHlTq1/+NjEMIg8IjaI5iHXemd9APztQKRn5MxkAW4M9/W+Z25A98gaeGmDSwlmAn1Zp4b19FDwIDAQAB";
    // 日志记录目录
    public static String log_path = "/log";
    // RSA2
    public static String SIGNTYPE = "RSA2";
    // 调用的接口名，无需修改
    public static String service = "alipay.wap.create.direct.pay.by.user";
    // 支付类型 ，无需修改
    public static String payment_type = "1";
}

