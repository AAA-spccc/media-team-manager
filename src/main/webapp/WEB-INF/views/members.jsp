<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="common/header.jsp" %>

<div class="page-header">
    <h2>人员管理</h2>
</div>

<div class="panel">
    <div class="panel-title">${editMember != null ? '编辑成员信息' : '新增团队成员'}</div>
    <form method="post" action="${pageContext.request.contextPath}/members" class="form-grid">
        <input type="hidden" name="action" value="save">
        <c:if test="${editMember != null}">
            <input type="hidden" name="id" value="${editMember.id}">
        </c:if>
        <div class="form-group">
            <label>姓名</label>
            <input type="text" name="name" required value="<c:out value='${editMember.name}'/>" placeholder="如：林小满">
        </div>
        <div class="form-group">
            <label>岗位角色</label>
            <input type="text" name="position" required value="<c:out value='${editMember.position}'/>" placeholder="编导/摄影师/剪辑师/运营">
        </div>
        <div class="form-group">
            <label>所属小组</label>
            <input type="text" name="department" required value="<c:out value='${editMember.department}'/>" placeholder="内容策划组/拍摄制作组">
        </div>
        <div class="form-group">
            <label>联系电话</label>
            <input type="text" name="phone" value="<c:out value='${editMember.phone}'/>" placeholder="138xxxx">
        </div>
        <div class="form-group">
            <label>基本月薪 (元)</label>
            <input type="number" step="0.01" name="baseSalary" required value="${editMember != null ? editMember.baseSalary : '7500'}">
        </div>
        <div class="form-group">
            <label>在职状态</label>
            <select name="status">
                <option value="在职" ${editMember.status == '在职' ? 'selected' : ''}>在职</option>
                <option value="休假" ${editMember.status == '休假' ? 'selected' : ''}>休假</option>
                <option value="离职" ${editMember.status == '离职' ? 'selected' : ''}>离职</option>
            </select>
        </div>
        <div class="form-group">
            <label>入职日期</label>
            <input type="date" name="joinDate" value="${editMember != null ? editMember.joinDate : '2026-11-01'}">
        </div>
        <div class="form-group" style="display:flex; flex-direction:row; gap:8px;">
            <button type="submit" class="btn">${editMember != null ? '保存修改' : '添加成员'}</button>
            <c:if test="${editMember != null}">
                <a href="${pageContext.request.contextPath}/members" class="btn btn-ghost">取消</a>
            </c:if>
        </div>
    </form>
</div>

<div class="panel">
    <form method="get" action="${pageContext.request.contextPath}/members" class="filter-bar">
        <input type="text" name="keyword" value="<c:out value='${keyword}'/>" placeholder="搜索姓名/岗位/小组">
        <select name="status">
            <option value="">全部状态</option>
            <option value="在职" ${status == '在职' ? 'selected' : ''}>在职</option>
            <option value="休假" ${status == '休假' ? 'selected' : ''}>休假</option>
            <option value="离职" ${status == '离职' ? 'selected' : ''}>离职</option>
        </select>
        <button type="submit" class="btn">查询</button>
        <a href="${pageContext.request.contextPath}/members" class="btn btn-ghost">重置</a>
    </form>

    <table>
        <thead>
        <tr>
            <th>ID</th>
            <th>姓名</th>
            <th>岗位角色</th>
            <th>所属小组</th>
            <th>联系电话</th>
            <th>基本月薪</th>
            <th>状态</th>
            <th>入职日期</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="m" items="${members}">
            <tr>
                <td>${m.id}</td>
                <td><strong><c:out value="${m.name}"/></strong></td>
                <td><c:out value="${m.position}"/></td>
                <td><c:out value="${m.department}"/></td>
                <td><c:out value="${m.phone}"/></td>
                <td>¥${m.baseSalary}</td>
                <td>
                    <c:choose>
                        <c:when test="${m.status == '在职'}"><span class="badge badge-green">在职</span></c:when>
                        <c:when test="${m.status == '休假'}"><span class="badge badge-amber">休假</span></c:when>
                        <c:otherwise><span class="badge badge-red">离职</span></c:otherwise>
                    </c:choose>
                </td>
                <td>${m.joinDate}</td>
                <td>
                    <a href="${pageContext.request.contextPath}/members?editId=${m.id}" class="btn btn-sm btn-ghost">编辑</a>
                    <form method="post" action="${pageContext.request.contextPath}/members" class="inline-form" onsubmit="return confirm('确认删除该成员？');">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="id" value="${m.id}">
                        <button type="submit" class="btn btn-sm btn-danger">删除</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<%@ include file="common/footer.jsp" %>
