package com.mediateam.servlet;

import com.mediateam.dao.MemberDao;
import com.mediateam.dao.ProjectDao;
import com.mediateam.model.ProjectItem;
import com.mediateam.model.TeamMember;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/projects")
public class ProjectServlet extends HttpServlet {

    private final ProjectDao projectDao = new ProjectDao();
    private final MemberDao memberDao = new MemberDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String platform = req.getParameter("platform");
        String stage = req.getParameter("stage");

        try {
            List<ProjectItem> projects = projectDao.findAll(platform, stage);
            List<TeamMember> members = memberDao.findAll(null, "在职");

            req.setAttribute("projects", projects);
            req.setAttribute("members", members);
            req.setAttribute("platform", platform);
            req.setAttribute("stage", stage);
            req.setAttribute("nav", "projects");
            req.getRequestDispatcher("/WEB-INF/views/projects.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("查询项目进度失败", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = StringUtils.defaultString(req.getParameter("action"), "add");
        try {
            if ("add".equals(action)) {
                ProjectItem p = new ProjectItem();
                p.setTitle(StringUtils.trimToEmpty(req.getParameter("title")));
                p.setPlatform(StringUtils.defaultIfBlank(req.getParameter("platform"), "抖音"));
                String ownerIdStr = req.getParameter("ownerId");
                if (StringUtils.isNotBlank(ownerIdStr)) {
                    long ownerId = Long.parseLong(ownerIdStr);
                    TeamMember owner = memberDao.findById(ownerId);
                    p.setOwnerId(ownerId);
                    p.setOwnerName(owner != null ? owner.getName() : "未指定");
                } else {
                    p.setOwnerName("未指定");
                }
                String stage = StringUtils.defaultIfBlank(req.getParameter("stage"), "选题策划");
                p.setStage(stage);
                p.setProgress(defaultProgressForStage(stage, req.getParameter("progress")));
                p.setDueDate(StringUtils.defaultIfBlank(req.getParameter("dueDate"), LocalDate.now().plusDays(7).toString()));
                p.setRemark(StringUtils.trimToEmpty(req.getParameter("remark")));
                projectDao.insert(p);
            } else if ("progress".equals(action)) {
                long id = Long.parseLong(req.getParameter("id"));
                String stage = StringUtils.defaultIfBlank(req.getParameter("stage"), "选题策划");
                int progress = defaultProgressForStage(stage, req.getParameter("progress"));
                projectDao.updateProgress(id, stage, progress);
            } else if ("delete".equals(action)) {
                projectDao.delete(Long.parseLong(req.getParameter("id")));
            }
            resp.sendRedirect(req.getContextPath() + "/projects");
        } catch (SQLException e) {
            throw new ServletException("项目操作失败", e);
        }
    }

    private int defaultProgressForStage(String stage, String progressParam) {
        if (StringUtils.isNotBlank(progressParam)) {
            return Integer.parseInt(progressParam.trim());
        }
        switch (stage) {
            case "选题策划":
                return 15;
            case "脚本撰写":
                return 30;
            case "拍摄录制":
                return 55;
            case "后期剪辑":
                return 75;
            case "审片待发":
                return 90;
            case "已发布":
                return 100;
            default:
                return 20;
        }
    }
}
