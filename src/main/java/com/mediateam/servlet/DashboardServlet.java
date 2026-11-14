package com.mediateam.servlet;

import com.mediateam.dao.EquipmentDao;
import com.mediateam.dao.MemberDao;
import com.mediateam.dao.ProjectDao;
import com.mediateam.dao.SalaryDao;
import com.mediateam.model.ProjectItem;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private final MemberDao memberDao = new MemberDao();
    private final EquipmentDao equipmentDao = new EquipmentDao();
    private final SalaryDao salaryDao = new SalaryDao();
    private final ProjectDao projectDao = new ProjectDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int activeMembers = memberDao.countActive();
            int availableEquip = equipmentDao.countByStatus("在库");
            int borrowedEquip = equipmentDao.countByStatus("借出");
            BigDecimal totalSalary = salaryDao.sumTotalAmount();
            int inProgressProjects = projectDao.countInProgress();
            List<ProjectItem> recentProjects = projectDao.findAll(null, null);

            req.setAttribute("activeMembers", activeMembers);
            req.setAttribute("availableEquip", availableEquip);
            req.setAttribute("borrowedEquip", borrowedEquip);
            req.setAttribute("totalSalary", totalSalary);
            req.setAttribute("inProgressProjects", inProgressProjects);
            req.setAttribute("recentProjects", recentProjects);
            req.setAttribute("nav", "dashboard");

            req.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("加载看板数据失败", e);
        }
    }
}
