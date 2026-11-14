package com.mediateam.servlet;

import com.mediateam.dao.EquipmentDao;
import com.mediateam.dao.MemberDao;
import com.mediateam.model.Equipment;
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

@WebServlet("/equipment")
public class EquipmentServlet extends HttpServlet {

    private final EquipmentDao equipmentDao = new EquipmentDao();
    private final MemberDao memberDao = new MemberDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String keyword = req.getParameter("keyword");
        String category = req.getParameter("category");
        String status = req.getParameter("status");

        try {
            List<Equipment> list = equipmentDao.findAll(keyword, category, status);
            List<TeamMember> members = memberDao.findAll(null, "在职");
            req.setAttribute("equipments", list);
            req.setAttribute("members", members);
            req.setAttribute("keyword", keyword);
            req.setAttribute("category", category);
            req.setAttribute("status", status);
            req.setAttribute("nav", "equipment");
            req.getRequestDispatcher("/WEB-INF/views/equipment.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("查询设备台账失败", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = StringUtils.defaultString(req.getParameter("action"), "add");
        try {
            if ("add".equals(action)) {
                Equipment eq = new Equipment();
                eq.setAssetCode(StringUtils.trimToEmpty(req.getParameter("assetCode")));
                eq.setName(StringUtils.trimToEmpty(req.getParameter("name")));
                eq.setCategory(StringUtils.defaultIfBlank(req.getParameter("category"), "相机"));
                String priceStr = StringUtils.defaultIfBlank(req.getParameter("price"), "0");
                eq.setPrice(new BigDecimal(priceStr.trim()));
                eq.setStatus("在库");
                eq.setRemark(StringUtils.trimToEmpty(req.getParameter("remark")));
                equipmentDao.insert(eq);
            } else if ("borrow".equals(action)) {
                long id = Long.parseLong(req.getParameter("id"));
                long borrowerId = Long.parseLong(req.getParameter("borrowerId"));
                TeamMember borrower = memberDao.findById(borrowerId);
                String borrowerName = borrower != null ? borrower.getName() : "未知成员";
                equipmentDao.borrow(id, borrowerId, borrowerName, LocalDate.now().toString());
            } else if ("return".equals(action)) {
                long id = Long.parseLong(req.getParameter("id"));
                equipmentDao.updateStatus(id, "在库");
            } else if ("repair".equals(action)) {
                long id = Long.parseLong(req.getParameter("id"));
                equipmentDao.updateStatus(id, "维修");
            } else if ("delete".equals(action)) {
                long id = Long.parseLong(req.getParameter("id"));
                equipmentDao.delete(id);
            }
            resp.sendRedirect(req.getContextPath() + "/equipment");
        } catch (SQLException e) {
            throw new ServletException("设备操作失败", e);
        }
    }
}
