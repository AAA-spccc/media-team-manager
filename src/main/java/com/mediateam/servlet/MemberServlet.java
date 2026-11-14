package com.mediateam.servlet;

import com.mediateam.dao.MemberDao;
import com.mediateam.model.TeamMember;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/members")
public class MemberServlet extends HttpServlet {

    private final MemberDao memberDao = new MemberDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String keyword = req.getParameter("keyword");
        String status = req.getParameter("status");
        String editId = req.getParameter("editId");

        try {
            List<TeamMember> members = memberDao.findAll(keyword, status);
            if (StringUtils.isNotBlank(editId)) {
                req.setAttribute("editMember", memberDao.findById(Long.parseLong(editId)));
            }
            req.setAttribute("members", members);
            req.setAttribute("keyword", keyword);
            req.setAttribute("status", status);
            req.setAttribute("nav", "members");
            req.getRequestDispatcher("/WEB-INF/views/members.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("查询成员列表失败", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = StringUtils.defaultString(req.getParameter("action"), "save");
        try {
            if ("delete".equals(action)) {
                long id = Long.parseLong(req.getParameter("id"));
                memberDao.delete(id);
            } else {
                TeamMember m = new TeamMember();
                String idStr = req.getParameter("id");
                m.setName(StringUtils.trimToEmpty(req.getParameter("name")));
                m.setPosition(StringUtils.trimToEmpty(req.getParameter("position")));
                m.setDepartment(StringUtils.trimToEmpty(req.getParameter("department")));
                m.setPhone(StringUtils.trimToEmpty(req.getParameter("phone")));
                String salaryStr = StringUtils.defaultIfBlank(req.getParameter("baseSalary"), "0");
                m.setBaseSalary(new BigDecimal(salaryStr.trim()));
                m.setStatus(StringUtils.defaultIfBlank(req.getParameter("status"), "在职"));
                m.setJoinDate(StringUtils.defaultIfBlank(req.getParameter("joinDate"), LocalDate.now().toString()));

                if (StringUtils.isNotBlank(idStr)) {
                    m.setId(Long.parseLong(idStr));
                    memberDao.update(m);
                } else {
                    memberDao.insert(m);
                }
            }
            resp.sendRedirect(req.getContextPath() + "/members");
        } catch (SQLException e) {
            throw new ServletException("保存成员数据失败", e);
        }
    }
}
