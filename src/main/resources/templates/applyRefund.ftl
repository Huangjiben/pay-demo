<html>
<base id="base" href="${base}">
<body>
-----------------------------------退款-----------------------------------------
<div>
<#list payDataList as payData>
    <table>
        <tr>
            <td>商户订单号：${payData.orderId} </td>
            <td>支付总金额:${payData.total_fee}</td>
            <td>
                <button onclick="refund(${payData.orderId},${payData.total_fee})">申请退款</button>
            </td>

        </tr>
    </table>
</#list>
</div>
</body>
<script src="http://code.jquery.com/jquery-1.8.0.min.js" type="text/javascript"></script>
<script type="text/javascript">

    function refund(orderId, total_fee) {
        var orderId = orderId;
        var total_fee = total_fee;
        $.ajax({
            async: false,
            cache: false,
            type: 'GET',
            contentType: "application/json",
            url: "../wechat/refundWXPay",
            data: {orderId: orderId, total_fee: total_fee},
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

