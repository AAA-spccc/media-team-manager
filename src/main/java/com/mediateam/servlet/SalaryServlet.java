package com.mediateam.servlet;

import com.mediateam.dao.MemberDao;
import com.mediateam.dao.SalaryDao;
import com.mediateam.model.SalaryRecord;
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
import java.time.YearMonth;
import java.util.List;

@WebServlet("/salaries")
public class SalaryServlet extends HttpServlet {

    private final SalaryDao salaryDao = new SalaryDao();
    private final MemberDao memberDao = new MemberDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String payMonth = req.getParameter("payMonth");
        String status = req.getParameter("status");

        try {
            List<SalaryRecord> records = salaryDao.findAll(payMonth, status);
            List<TeamMember> members = memberDao.findAll(null, null);

            BigDecimal filteredTotal = BigDecimal.ZERO;
            for (SalaryRecord r : records) {
                filteredTotal = filteredTotal.add(r.getTotalAmount());
            }

            req.setAttribute("salaries", records);
            req.setAttribute("members", members);
            req.setAttribute("payMonth", payMonth);
            req.setAttribute("status", status);
            req.setAttribute("filteredTotal", filteredTotal);
            req.setAttribute("defaultMonth", YearMonth.now().toString());
            req.setAttribute("nav", "salaries");
            req.getRequestDispatcher("/WEB-INF/views/salaries.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("查询薪资记录失败", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = StringUtils.defaultString(req.getParameter("action"), "add");
        try {
            if ("add".equals(action)) {
                long memberId = Long.parseLong(req.getParameter("memberId"));
                TeamMember member = memberDao.findById(memberId);

                BigDecimal base = parseAmount(req.getParameter("baseAmount"),
                        member != null ? member.getBaseSalary() : BigDecimal.ZERO);
                BigDecimal bonus = parseAmount(req.getParameter("bonusAmount"), BigDecimal.ZERO);
                BigDecimal allowance = parseAmount(req.getParameter("allowanceAmount"), BigDecimal.ZERO);
                BigDecimal deduction = parseAmount(req.getParameter("deductionAmount"), BigDecimal.ZERO);
                BigDecimal total = base.add(bonus).add(allowance).subtract(deduction);

                SalaryRecord r = new SalaryRecord();
                r.setMemberId(memberId);
                r.setMemberName(member != null ? member.getName() : "未知成员");
                r.setPayMonth(StringUtils.defaultIfBlank(req.getParameter("payMonth"), YearMonth.now().toString()));
                r.setBaseAmount(base);
                r.setBonusAmount(bonus);
                r.setAllowanceAmount(allowance);
                r.setDeductionAmount(deduction);
                r.setTotalAmount(total.max(BigDecimal.ZERO));
                r.setStatus("待发放");
                r.setRemark(StringUtils.trimToEmpty(req.getParameter("remark")));
                salaryDao.insert(r);
            } else if ("pay".equals(action)) {
                salaryDao.markPaid(Long.parseLong(req.getParameter("id")));
            } else if ("delete".equals(action)) {
                salaryDao.delete(Long.parseLong(req.getParameter("id")));
            }
            resp.sendRedirect(req.getContextPath() + "/salaries");
        } catch (SQLException e) {
            throw new ServletException("薪资操作失败", e);
        }
    }

    private BigDecimal parseAmount(String val, BigDecimal fallback) {
        if (StringUtils.isBlank(val)) {
            return fallback;
        }
        return new BigDecimal(val.trim());
    }
}
