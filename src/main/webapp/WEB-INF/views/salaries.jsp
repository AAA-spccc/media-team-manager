<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="common/header.jsp" %>

<div class="page-header">
    <h2>薪资核算与发放</h2>
    <span class="badge badge-blue">当前列表实发合计：¥${filteredTotal}</span>
</div>

<div class="panel">
    <div class="panel-title">新增月度薪资核算（实发 = 基本工资 + 绩效奖金 + 补贴 - 扣款）</div>
    <form method="post" action="${pageContext.request.contextPath}/salaries" class="form-grid">
        <input type="hidden" name="action" value="add">
        <div class="form-group">
            <label>团队成员</label>
            <select name="memberId" required>
                <c:forEach var="m" items="${members}">
                    <option value="${m.id}"><c:out value="${m.name}"/> (底薪 ¥${m.baseSalary})</option>
                </c:forEach>
            </select>
        </div>
        <div class="form-group">
            <label>发放月份</label>
            <input type="month" name="payMonth" required value="${defaultMonth}">
        </div>
        <div class="form-group">
            <label>基本工资 (留空取成员底薪)</label>
            <input type="number" step="0.01" name="baseAmount" placeholder="自动读取底薪">
        </div>
        <div class="form-group">
            <label>项目绩效奖金</label>
            <input type="number" step="0.01" name="bonusAmount" value="0">
        </div>
        <div class="form-group">
            <label>外拍/全勤补贴</label>
            <input type="number" step="0.01" name="allowanceAmount" value="0">
        </div>
        <div class="form-group">
            <label>扣款</label>
            <input type="number" step="0.01" name="deductionAmount" value="0">
        </div>
        <div class="form-group">
            <label>备注</label>
            <input type="text" name="remark" placeholder="如：爆款激励">
        </div>
        <div class="form-group">
            <button type="submit" class="btn">生成工资单</button>
        </div>
    </form>
</div>

<div class="panel">
    <form method="get" action="${pageContext.request.contextPath}/salaries" class="filter-bar">
        <input type="month" name="payMonth" value="<c:out value='${payMonth}'/>">
        <select name="status">
            <option value="">全部发放状态</option>
            <option value="待发放" ${status == '待发放' ? 'selected' : ''}>待发放</option>
            <option value="已发放" ${status == '已发放' ? 'selected' : ''}>已发放</option>
        </select>
        <button type="submit" class="btn">查询</button>
        <a href="${pageContext.request.contextPath}/salaries" class="btn btn-ghost">重置</a>
    </form>

    <table>
        <thead>
        <tr>
            <th>月份</th>
            <th>成员</th>
            <th>基本工资</th>
            <th>绩效奖金</th>
            <th>补贴</th>
            <th>扣款</th>
            <th>实发工资</th>
            <th>状态</th>
            <th>备注</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="s" items="${salaries}">
            <tr>
                <td>${s.payMonth}</td>
                <td><strong><c:out value="${s.memberName}"/></strong></td>
                <td>¥${s.baseAmount}</td>
                <td>¥${s.bonusAmount}</td>
                <td>¥${s.allowanceAmount}</td>
                <td>¥${s.deductionAmount}</td>
                <td><strong>¥${s.totalAmount}</strong></td>
                <td>
                    <c:choose>
                        <c:when test="${s.status == '已发放'}"><span class="badge badge-green">已发放</span></c:when>
                        <c:otherwise><span class="badge badge-amber">待发放</span></c:otherwise>
                    </c:choose>
                </td>
                <td><c:out value="${s.remark}"/></td>
                <td>
                    <c:if test="${s.status == '待发放'}">
                        <form method="post" action="${pageContext.request.contextPath}/salaries" class="inline-form">
                            <input type="hidden" name="action" value="pay">
                            <input type="hidden" name="id" value="${s.id}">
                            <button type="submit" class="btn btn-sm btn-success">确认发放</button>
                        </form>
                    </c:if>
                    <form method="post" action="${pageContext.request.contextPath}/salaries" class="inline-form" onsubmit="return confirm('确认删除该工资记录？');">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="id" value="${s.id}">
                        <button type="submit" class="btn btn-sm btn-danger">删除</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<%@ include file="common/footer.jsp" %>
