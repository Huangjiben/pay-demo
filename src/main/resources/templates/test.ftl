<html>
<body>
<div>
    测试
    <button onclick="refund('001','002')">test</button>
</div>
</body>
<script src="/js/jquery-1.8.0.min.js" type="text/javascript"></script>
<script type="text/javascript">

    function refund(orderId, total_fee) {
        var orderId = orderId;
        var total_fee = total_fee;
        $.ajax({
            async: true,
            cache: false,
            type: 'GET',
            contentType: "application/json",
            url: "../wc/paySuccess",
            data: {orderId: orderId, money: total_fee},
            error: function () {//请求失败处理函数
                alert('请求失败');
            },
            success: function (data) { //请求成功后处理函数。
                $(document.body).html(data);
            }
        });
    }


</script>
</html>

