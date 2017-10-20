<!DOCTYPE html>
<html lang="en">
<head>
    <title>退款结果</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
</head>
<body>
<div>
<#if refundCode == "ok">
    <p align="center">退款成功！</p>
</#if>

<#if refundCode == "fail">
    <p align="center">退款失败！</p>
</#if>
</div>
</body>
</html>