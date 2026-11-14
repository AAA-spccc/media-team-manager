<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>自媒体团队管理系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
</head>
<body>
<div class="layout">
    <aside class="sidebar">
        <div class="brand">
            自媒体团队管家
            <span>7A · JavaEE 期末大作业</span>
        </div>
        <nav class="nav-links">
            <a href="${pageContext.request.contextPath}/dashboard" class="${nav == 'dashboard' ? 'active' : ''}">📊 团队总览看板</a>
            <a href="${pageContext.request.contextPath}/members" class="${nav == 'members' ? 'active' : ''}">👥 人员管理</a>
            <a href="${pageContext.request.contextPath}/equipment" class="${nav == 'equipment' ? 'active' : ''}">📷 设备台账</a>
            <a href="${pageContext.request.contextPath}/salaries" class="${nav == 'salaries' ? 'active' : ''}">💰 薪资核算</a>
            <a href="${pageContext.request.contextPath}/projects" class="${nav == 'projects' ? 'active' : ''}">🎬 项目进度</a>
        </nav>
        <div class="sidebar-footer">
            <div>当前登录：<strong>${sessionScope.currentUser.displayName}</strong></div>
            <a href="${pageContext.request.contextPath}/logout">退出登录</a>
        </div>
    </aside>
    <main class="main">
