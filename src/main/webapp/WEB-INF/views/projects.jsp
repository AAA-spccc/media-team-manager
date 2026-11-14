<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="common/header.jsp" %>

<div class="page-header">
    <h2>内容项目进度管理</h2>
</div>

<div class="panel">
    <div class="panel-title">新建内容/商单项目</div>
    <form method="post" action="${pageContext.request.contextPath}/projects" class="form-grid">
        <input type="hidden" name="action" value="add">
        <div class="form-group">
            <label>项目选题名称</label>
            <input type="text" name="title" required placeholder="如：冬季数码好物开箱">
        </div>
        <div class="form-group">
            <label>发布平台</label>
            <select name="platform">
                <option value="抖音">抖音</option>
                <option value="B站">B站</option>
                <option value="小红书">小红书</option>
                <option value="视频号">视频号</option>
            </select>
        </div>
        <div class="form-group">
            <label>负责人</label>
            <select name="ownerId">
                <c:forEach var="m" items="${members}">
                    <option value="${m.id}"><c:out value="${m.name}"/> (<c:out value="${m.position}"/>)</option>
                </c:forEach>
            </select>
        </div>
        <div class="form-group">
            <label>初始阶段</label>
            <select name="stage">
                <option value="选题策划">选题策划</option>
                <option value="脚本撰写">脚本撰写</option>
                <option value="拍摄录制">拍摄录制</option>
                <option value="后期剪辑">后期剪辑</option>
                <option value="审片待发">审片待发</option>
                <option value="已发布">已发布</option>
            </select>
        </div>
        <div class="form-group">
            <label>计划发布日期</label>
            <input type="date" name="dueDate" value="2026-11-30">
        </div>
        <div class="form-group">
            <label>备注</label>
            <input type="text" name="remark" placeholder="拍摄场地/商单客户要求">
        </div>
        <div class="form-group">
            <button type="submit" class="btn">创建项目</button>
        </div>
    </form>
</div>

<div class="panel">
    <form method="get" action="${pageContext.request.contextPath}/projects" class="filter-bar">
        <select name="platform">
            <option value="">全部平台</option>
            <option value="抖音" ${platform == '抖音' ? 'selected' : ''}>抖音</option>
            <option value="B站" ${platform == 'B站' ? 'selected' : ''}>B站</option>
            <option value="小红书" ${platform == '小红书' ? 'selected' : ''}>小红书</option>
            <option value="视频号" ${platform == '视频号' ? 'selected' : ''}>视频号</option>
        </select>
        <select name="stage">
            <option value="">全部阶段</option>
            <option value="选题策划" ${stage == '选题策划' ? 'selected' : ''}>选题策划</option>
            <option value="脚本撰写" ${stage == '脚本撰写' ? 'selected' : ''}>脚本撰写</option>
            <option value="拍摄录制" ${stage == '拍摄录制' ? 'selected' : ''}>拍摄录制</option>
            <option value="后期剪辑" ${stage == '后期剪辑' ? 'selected' : ''}>后期剪辑</option>
            <option value="审片待发" ${stage == '审片待发' ? 'selected' : ''}>审片待发</option>
            <option value="已发布" ${stage == '已发布' ? 'selected' : ''}>已发布</option>
        </select>
        <button type="submit" class="btn">筛选</button>
        <a href="${pageContext.request.contextPath}/projects" class="btn btn-ghost">重置</a>
    </form>

    <table>
        <thead>
        <tr>
            <th>项目选题</th>
            <th>平台</th>
            <th>负责人</th>
            <th>当前阶段 &amp; 进度</th>
            <th>计划发布日期</th>
            <th>备注</th>
            <th>更新进度 / 操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="p" items="${projects}">
            <tr>
                <td><strong><c:out value="${p.title}"/></strong></td>
                <td><span class="badge badge-blue"><c:out value="${p.platform}"/></span></td>
                <td><c:out value="${p.ownerName}"/></td>
                <td>
                    <span class="badge ${p.stage == '已发布' ? 'badge-green' : 'badge-amber'}"><c:out value="${p.stage}"/></span>
                    <div class="progress-wrap" style="margin-left:6px;"><div class="progress-bar" style="width:${p.progress}%"></div></div>
                    <span>${p.progress}%</span>
                </td>
                <td>${p.dueDate}</td>
                <td><c:out value="${p.remark}"/></td>
                <td>
                    <form method="post" action="${pageContext.request.contextPath}/projects" class="inline-form">
                        <input type="hidden" name="action" value="progress">
                        <input type="hidden" name="id" value="${p.id}">
                        <select name="stage" style="width:98px; padding:4px;">
                            <option value="选题策划" ${p.stage == '选题策划' ? 'selected' : ''}>选题策划</option>
                            <option value="脚本撰写" ${p.stage == '脚本撰写' ? 'selected' : ''}>脚本撰写</option>
                            <option value="拍摄录制" ${p.stage == '拍摄录制' ? 'selected' : ''}>拍摄录制</option>
                            <option value="后期剪辑" ${p.stage == '后期剪辑' ? 'selected' : ''}>后期剪辑</option>
                            <option value="审片待发" ${p.stage == '审片待发' ? 'selected' : ''}>审片待发</option>
                            <option value="已发布" ${p.stage == '已发布' ? 'selected' : ''}>已发布</option>
                        </select>
                        <input type="number" name="progress" min="0" max="100" value="${p.progress}" style="width:62px; padding:4px;">
                        <button type="submit" class="btn btn-sm">更新</button>
                    </form>
                    <form method="post" action="${pageContext.request.contextPath}/projects" class="inline-form" onsubmit="return confirm('确认删除该项目？');">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="id" value="${p.id}">
                        <button type="submit" class="btn btn-sm btn-danger">删除</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<%@ include file="common/footer.jsp" %>
