<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>支付宝手机网站支付接口</title>
</head>
<body>

<script src="/js/ap.js"></script>
<script>
    //该js用于微信上使用支付宝支付
    window.onload = function () {
        var queryParam = '&';
        Array.prototype.slice.call(document.querySelectorAll('input[type=hidden]')).forEach(function (ele) {
            queryParam += ele.name + '=' + encodeURIComponent(ele.value) + '&';
        });
        var gotoUrl = document.forms[0].getAttribute('action') + queryParam;
        _AP.pay(gotoUrl);
    };

</script>
</body>
</html>