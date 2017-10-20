<#assign base = request.contextPath />
<html>
<base id="base" href="${base}">
<body>
-----------------------------------订单号：${state}-----------------------------------------
<div><button onclick="pay()">共需支付0.01元&nbsp;&nbsp;确认支付</button></div>
</body>
</html>

<script>
    function pay() {
        var url="http://www.ifindu.top/wechat/pay?money=0.01"; //注意此处的basePath是没有端口号的域名地址。如果包含:80,在提交给微信时有可能会提示 “redirect_uri参数错误” 。
        //money为订单需要支付的金额
        //state中存放的为商品订单号
        var weixinUrl="https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxa284cd5f9facc01e&redirect_uri="+encodeURI(url)+"&response_type=code&scope=snsapi_userinfo&state=${state}#wechat_redirect";
        window.location.href=encodeURI(weixinUrl);
    }

</script>