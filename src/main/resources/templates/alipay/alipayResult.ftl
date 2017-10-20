<!DOCTYPE html>
<html lang="en">
<head>
    <title>alipay resunt</title>
<#--<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>-->
</head>
<body>
<div>
<#if resultCode == "ok">
    <p align="center">支付成功</p>
</#if>

<#if resultCode == "fail">
    <p align="center">支付失败</p>
</#if>
</div>
</body>
</html>