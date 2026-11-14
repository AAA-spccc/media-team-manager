<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="common/header.jsp" %>

<div class="page-header">
    <h2>团队总览看板</h2>
    <span class="badge badge-blue">7A 自媒体团队管理</span>
</div>

<div class="stats-grid">
    <div class="stat-card">
        <div class="label">在职团队成员</div>
        <div class="value">${activeMembers} 人</div>
        <div class="hint"><a href="${pageContext.request.contextPath}/members">进入人员管理 →</a></div>
    </div>
    <div class="stat-card">
        <div class="label">设备在库 / 借出</div>
        <div class="value">${availableEquip} / ${borrowedEquip}</div>
        <div class="hint"><a href="${pageContext.request.contextPath}/equipment">进入设备台账 →</a></div>
    </div>
    <div class="stat-card">
        <div class="label">累计薪资核算总额</div>
        <div class="value">¥${totalSalary}</div>
        <div class="hint"><a href="${pageContext.request.contextPath}/salaries">进入薪资核算 →</a></div>
    </div>
    <div class="stat-card">
        <div class="label">进行中内容项目</div>
        <div class="value">${inProgressProjects} 个</div>
        <div class="hint"><a href="${pageContext.request.contextPath}/projects">进入项目进度 →</a></div>
    </div>
</div>

<div class="panel">
    <div class="panel-title">近期内容项目进度概览</div>
    <table>
        <thead>
        <tr>
            <th>项目选题</th>
            <th>发布平台</th>
            <th>负责人</th>
            <th>当前阶段</th>
            <th>完成进度</th>
            <th>计划发布日期</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="p" items="${recentProjects}">
            <tr>
                <td><strong><c:out value="${p.title}"/></strong></td>
                <td><span class="badge badge-blue"><c:out value="${p.platform}"/></span></td>
                <td><c:out value="${p.ownerName}"/></td>
                <td>
                    <c:choose>
                        <c:when test="${p.stage == '已发布'}"><span class="badge badge-green">已发布</span></c:when>
                        <c:when test="${p.stage == '审片待发'}"><span class="badge badge-amber">审片待发</span></c:when>
                        <c:otherwise><span class="badge badge-gray"><c:out value="${p.stage}"/></span></c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <div class="progress-wrap"><div class="progress-bar" style="width:${p.progress}%"></div></div>
                    <span>${p.progress}%</span>
                </td>
                <td>${p.dueDate}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<%@ include file="common/footer.jsp" %>
