package com.mediateam.dao;

import com.mediateam.model.Equipment;
import com.mediateam.util.DBUtil;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class EquipmentDao {

    public List<Equipment> findAll(String keyword, String category, String status) throws SQLException {
        StringBuilder sql = new StringBuilder(
                "SELECT id, asset_code, name, category, price, status, borrower_id, borrower_name, borrowed_at, remark " +
                "FROM team_equipment WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (StringUtils.isNotBlank(keyword)) {
            sql.append(" AND (name LIKE ? OR asset_code LIKE ? OR borrower_name LIKE ?)");
            String like = "%" + keyword.trim() + "%";
            params.add(like);
            params.add(like);
            params.add(like);
        }
        if (StringUtils.isNotBlank(category)) {
            sql.append(" AND category = ?");
            params.add(category.trim());
        }
        if (StringUtils.isNotBlank(status)) {
            sql.append(" AND status = ?");
            params.add(status.trim());
        }
        sql.append(" ORDER BY id DESC");

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Equipment> list = new ArrayList<>();
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

    public void insert(Equipment eq) throws SQLException {
        String sql = "INSERT INTO team_equipment (asset_code, name, category, price, status, borrower_id, borrower_name, borrowed_at, remark) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, eq.getAssetCode());
            ps.setString(2, eq.getName());
            ps.setString(3, eq.getCategory());
            ps.setBigDecimal(4, eq.getPrice());
            ps.setString(5, eq.getStatus());
            if (eq.getBorrowerId() != null) {
                ps.setLong(6, eq.getBorrowerId());
            } else {
                ps.setNull(6, Types.BIGINT);
            }
            ps.setString(7, StringUtils.defaultString(eq.getBorrowerName()));
            ps.setString(8, StringUtils.defaultString(eq.getBorrowedAt()));
            ps.setString(9, StringUtils.defaultString(eq.getRemark()));
            ps.executeUpdate();
        } finally {
            DBUtil.close(conn, ps, null);
        }
    }

    public void borrow(Long id, Long borrowerId, String borrowerName, String borrowedAt) throws SQLException {
        String sql = "UPDATE team_equipment SET status='借出', borrower_id=?, borrower_name=?, borrowed_at=? WHERE id=?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setLong(1, borrowerId);
            ps.setString(2, borrowerName);
            ps.setString(3, borrowedAt);
            ps.setLong(4, id);
            ps.executeUpdate();
        } finally {
            DBUtil.close(conn, ps, null);
        }
    }

    public void updateStatus(Long id, String status) throws SQLException {
        String sql = "UPDATE team_equipment SET status=?, borrower_id=NULL, borrower_name='', borrowed_at='' WHERE id=?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setLong(2, id);
            ps.executeUpdate();
        } finally {
            DBUtil.close(conn, ps, null);
        }
    }

    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM team_equipment WHERE id = ?";
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

    public int countByStatus(String status) throws SQLException {
        String sql = "SELECT COUNT(*) FROM team_equipment WHERE status = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        } finally {
            DBUtil.close(conn, ps, rs);
        }
    }

    private Equipment mapRow(ResultSet rs) throws SQLException {
        Equipment eq = new Equipment();
        eq.setId(rs.getLong("id"));
        eq.setAssetCode(rs.getString("asset_code"));
        eq.setName(rs.getString("name"));
        eq.setCategory(rs.getString("category"));
        eq.setPrice(rs.getBigDecimal("price"));
        eq.setStatus(rs.getString("status"));
        long bid = rs.getLong("borrower_id");
        eq.setBorrowerId(rs.wasNull() ? null : bid);
        eq.setBorrowerName(rs.getString("borrower_name"));
        eq.setBorrowedAt(rs.getString("borrowed_at"));
        eq.setRemark(rs.getString("remark"));
        return eq;
    }
}
