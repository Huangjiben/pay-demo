<!DOCTYPE html>
<head>
    <script src="http://code.jquery.com/jquery-1.8.0.min.js" type="text/javascript"></script>
    <script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
</head>
<body onload="javascript:pay();">
<script type="text/javascript">
    function pay(){
        if (typeof WeixinJSBridge == "undefined"){
            if( document.addEventListener ){
                document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
            }else if (document.attachEvent){
                document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
                document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
            }
        }else{
            onBridgeReady();
        }
    }
    function onBridgeReady(){
        WeixinJSBridge.invoke(
                'getBrandWCPayRequest', {
                    "appId" : "${appid}",     //公众号名称，由商户传入
                    "timeStamp": "${timeStamp}",         //时间戳，自1970年以来的秒数
                    "nonceStr" : "${nonceStr}", //随机串
                    "package" : "${packageValue}",
                    "signType" : "MD5",         //微信签名方式:
                    "paySign" : "${paySign}"    //微信签名
                },function(res){
                    //alert(JSON.stringify(res));
                    //alert("appId:"+"${appid}"+"timeStamp:"+"${timeStamp}"+"nonceStr:"+"${nonceStr}"+"package:"+"${packageValue}"+"paySign:"+"${paySign}");
                    if(res.err_msg == "get_brand_wcpay_request:ok"){
//                        alert("微信支付成功!");
                        returnSuccess(${orderId},${total_fee});
                    }else if(res.err_msg == "get_brand_wcpay_request:cancel"){
                        alert("用户取消支付!");
                    }else{
                        alert("支付失败!");
                    }
                });
    }

    function returnSuccess(orderId, money) {
        var orderId = orderId;
        var money = money;
        $.ajax({
            async: true,
            cache: false,
            type: 'GET',
            contentType: "application/json",
            url: "../wc/paySuccess",
            data: {orderId: orderId, money: money},
            error: function () {//请求失败处理函数
                alert('请求失败');
            },
            success: function (data) { //请求成功后处理函数。
                $(document.body).html(data);
            }
        });
    }
</script>
</body>
</html>