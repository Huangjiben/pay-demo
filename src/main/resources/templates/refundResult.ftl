<!DOCTYPE html>
<html lang="en">
<head>
    <title>退款结果</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
</head>
<body>
<div>
<#if returncode == "ok">
    <p align="center">${returninfo}</p>
</#if>

<#if returncode == "error">
    <p align="center">${returninfo}</p>
</#if>
</div>
</body>
</html>