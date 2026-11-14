<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>登录 - 自媒体团队管理系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
</head>
<body class="login-page">
<div class="login-card">
    <h1>自媒体团队管理系统</h1>
    <p class="subtitle">人员 · 设备 · 薪资 · 项目进度一体化工作台</p>

    <c:if test="${not empty error}">
        <div class="alert-error">${error}</div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/login">
        <div class="form-group">
            <label for="username">登录账号</label>
            <input id="username" type="text" name="username" value="admin" required placeholder="请输入账号">
        </div>
        <div class="form-group">
            <label for="password">登录密码</label>
            <input id="password" type="password" name="password" value="123456" required placeholder="请输入密码">
        </div>
        <button type="submit" class="btn" style="width: 100%; margin-top: 6px; padding: 10px;">立即登录</button>
    </form>

    <div class="demo-tip">
        演示账号：管理员 <code>admin / 123456</code> ｜ 普通成员 <code>member / 123456</code>
    </div>
</div>
</body>
</html>
