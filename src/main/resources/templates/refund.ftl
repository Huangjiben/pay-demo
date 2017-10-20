<#assign base = request.contextPath />
<html>
<base id="base" href="${base}">
<body>
<div>
    <button onclick="view()">查看可退款单</button>
</div>
</body>
</html>

<script>
    function view() {
        var url = "http://www.ifindu.top/wc/applyRefund";
        var weixinUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxa284cd5f9facc01e&redirect_uri=" + encodeURI(url) + "&response_type=code&scope=snsapi_userinfo&state=01#wechat_redirect";
        window.location.href = encodeURI(weixinUrl);
    }

</script>