<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="common/header.jsp" %>

<div class="page-header">
    <h2>设备台账与借还管理</h2>
</div>

<div class="panel">
    <div class="panel-title">新购设备入库</div>
    <form method="post" action="${pageContext.request.contextPath}/equipment" class="form-grid">
        <input type="hidden" name="action" value="add">
        <div class="form-group">
            <label>资产编号</label>
            <input type="text" name="assetCode" required placeholder="如：EQ-CAM-007">
        </div>
        <div class="form-group">
            <label>设备名称</label>
            <input type="text" name="name" required placeholder="如：Sony FX3 电影机">
        </div>
        <div class="form-group">
            <label>设备分类</label>
            <select name="category">
                <option value="相机">相机</option>
                <option value="镜头">镜头</option>
                <option value="灯光">灯光</option>
                <option value="收音">收音</option>
                <option value="稳定器">稳定器</option>
                <option value="电脑">电脑</option>
            </select>
        </div>
        <div class="form-group">
            <label>购入价格 (元)</label>
            <input type="number" step="0.01" name="price" required value="0">
        </div>
        <div class="form-group">
            <label>备注</label>
            <input type="text" name="remark" placeholder="存放位置或配件说明">
        </div>
        <div class="form-group">
            <button type="submit" class="btn">入库登记</button>
        </div>
    </form>
</div>

<div class="panel">
    <form method="get" action="${pageContext.request.contextPath}/equipment" class="filter-bar">
        <input type="text" name="keyword" value="<c:out value='${keyword}'/>" placeholder="搜索设备名/编号/借用人">
        <select name="category">
            <option value="">全部分类</option>
            <option value="相机" ${category == '相机' ? 'selected' : ''}>相机</option>
            <option value="镜头" ${category == '镜头' ? 'selected' : ''}>镜头</option>
            <option value="灯光" ${category == '灯光' ? 'selected' : ''}>灯光</option>
            <option value="收音" ${category == '收音' ? 'selected' : ''}>收音</option>
            <option value="稳定器" ${category == '稳定器' ? 'selected' : ''}>稳定器</option>
            <option value="电脑" ${category == '电脑' ? 'selected' : ''}>电脑</option>
        </select>
        <select name="status">
            <option value="">全部状态</option>
            <option value="在库" ${status == '在库' ? 'selected' : ''}>在库</option>
            <option value="借出" ${status == '借出' ? 'selected' : ''}>借出</option>
            <option value="维修" ${status == '维修' ? 'selected' : ''}>维修</option>
        </select>
        <button type="submit" class="btn">筛选</button>
        <a href="${pageContext.request.contextPath}/equipment" class="btn btn-ghost">重置</a>
    </form>

    <table>
        <thead>
        <tr>
            <th>资产编号</th>
            <th>设备名称</th>
            <th>分类</th>
            <th>价格</th>
            <th>状态</th>
            <th>当前借用人 / 日期</th>
            <th>备注</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="eq" items="${equipments}">
            <tr>
                <td><code><c:out value="${eq.assetCode}"/></code></td>
                <td><strong><c:out value="${eq.name}"/></strong></td>
                <td><c:out value="${eq.category}"/></td>
                <td>¥${eq.price}</td>
                <td>
                    <c:choose>
                        <c:when test="${eq.status == '在库'}"><span class="badge badge-green">在库</span></c:when>
                        <c:when test="${eq.status == '借出'}"><span class="badge badge-amber">借出</span></c:when>
                        <c:otherwise><span class="badge badge-red">维修</span></c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <c:if test="${not empty eq.borrowerName}">
                        <c:out value="${eq.borrowerName}"/> (${eq.borrowedAt})
                    </c:if>
                    <c:if test="${empty eq.borrowerName}">-</c:if>
                </td>
                <td><c:out value="${eq.remark}"/></td>
                <td>
                    <c:if test="${eq.status == '在库'}">
                        <form method="post" action="${pageContext.request.contextPath}/equipment" class="inline-form">
                            <input type="hidden" name="action" value="borrow">
                            <input type="hidden" name="id" value="${eq.id}">
                            <select name="borrowerId" style="width:95px; padding:4px;">
                                <c:forEach var="m" items="${members}">
                                    <option value="${m.id}"><c:out value="${m.name}"/></option>
                                </c:forEach>
                            </select>
                            <button type="submit" class="btn btn-sm">借出</button>
                        </form>
                        <form method="post" action="${pageContext.request.contextPath}/equipment" class="inline-form">
                            <input type="hidden" name="action" value="repair">
                            <input type="hidden" name="id" value="${eq.id}">
                            <button type="submit" class="btn btn-sm btn-warning">报修</button>
                        </form>
                    </c:if>
                    <c:if test="${eq.status != '在库'}">
                        <form method="post" action="${pageContext.request.contextPath}/equipment" class="inline-form">
                            <input type="hidden" name="action" value="return">
                            <input type="hidden" name="id" value="${eq.id}">
                            <button type="submit" class="btn btn-sm btn-success">归还/恢复在库</button>
                        </form>
                    </c:if>
                    <form method="post" action="${pageContext.request.contextPath}/equipment" class="inline-form" onsubmit="return confirm('确认删除该设备？');">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="id" value="${eq.id}">
                        <button type="submit" class="btn btn-sm btn-danger">删除</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<%@ include file="common/footer.jsp" %>
