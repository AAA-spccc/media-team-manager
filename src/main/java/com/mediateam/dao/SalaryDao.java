package com.mediateam.dao;

import com.mediateam.model.SalaryRecord;
import com.mediateam.util.DBUtil;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SalaryDao {

    public List<SalaryRecord> findAll(String payMonth, String status) throws SQLException {
        StringBuilder sql = new StringBuilder(
                "SELECT id, member_id, member_name, pay_month, base_amount, bonus_amount, allowance_amount, " +
                "deduction_amount, total_amount, status, remark FROM team_salary WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (StringUtils.isNotBlank(payMonth)) {
            sql.append(" AND pay_month = ?");
            params.add(payMonth.trim());
        }
        if (StringUtils.isNotBlank(status)) {
            sql.append(" AND status = ?");
            params.add(status.trim());
        }
        sql.append(" ORDER BY pay_month DESC, id DESC");

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<SalaryRecord> list = new ArrayList<>();
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        } finally {
            DBUtil.close(conn, ps, rs);
        }
    }

    public void insert(SalaryRecord record) throws SQLException {
        String sql = "INSERT INTO team_salary (member_id, member_name, pay_month, base_amount, bonus_amount, " +
                     "allowance_amount, deduction_amount, total_amount, status, remark) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setLong(1, record.getMemberId());
            ps.setString(2, record.getMemberName());
            ps.setString(3, record.getPayMonth());
            ps.setBigDecimal(4, record.getBaseAmount());
            ps.setBigDecimal(5, record.getBonusAmount());
            ps.setBigDecimal(6, record.getAllowanceAmount());
            ps.setBigDecimal(7, record.getDeductionAmount());
            ps.setBigDecimal(8, record.getTotalAmount());
            ps.setString(9, record.getStatus());
            ps.setString(10, StringUtils.defaultString(record.getRemark()));
            ps.executeUpdate();
        } finally {
            DBUtil.close(conn, ps, null);
        }
    }

    public void markPaid(Long id) throws SQLException {
        String sql = "UPDATE team_salary SET status = '已发放' WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setLong(1, id);
            ps.executeUpdate();
        } finally {
            DBUtil.close(conn, ps, null);
        }
    }

    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM team_salary WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setLong(1, id);
            ps.executeUpdate();
        } finally {
            DBUtil.close(conn, ps, null);
        }
    }

    public BigDecimal sumTotalAmount() throws SQLException {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) FROM team_salary";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            return rs.next() ? rs.getBigDecimal(1) : BigDecimal.ZERO;
        } finally {
            DBUtil.close(conn, ps, rs);
        }
    }

    private SalaryRecord mapRow(ResultSet rs) throws SQLException {
        SalaryRecord r = new SalaryRecord();
        r.setId(rs.getLong("id"));
        r.setMemberId(rs.getLong("member_id"));
        r.setMemberName(rs.getString("member_name"));
        r.setPayMonth(rs.getString("pay_month"));
        r.setBaseAmount(rs.getBigDecimal("base_amount"));
        r.setBonusAmount(rs.getBigDecimal("bonus_amount"));
        r.setAllowanceAmount(rs.getBigDecimal("allowance_amount"));
        r.setDeductionAmount(rs.getBigDecimal("deduction_amount"));
        r.setTotalAmount(rs.getBigDecimal("total_amount"));
        r.setStatus(rs.getString("status"));
        r.setRemark(rs.getString("remark"));
        return r;
    }
}
