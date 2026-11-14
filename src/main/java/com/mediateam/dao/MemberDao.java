package com.mediateam.dao;

import com.mediateam.model.TeamMember;
import com.mediateam.util.DBUtil;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MemberDao {

    public List<TeamMember> findAll(String keyword, String status) throws SQLException {
        StringBuilder sql = new StringBuilder(
                "SELECT id, name, position, department, phone, base_salary, status, DATE_FORMAT(join_date, '%Y-%m-%d') AS join_date " +
                "FROM team_member WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (StringUtils.isNotBlank(keyword)) {
            sql.append(" AND (name LIKE ? OR position LIKE ? OR department LIKE ?)");
            String like = "%" + keyword.trim() + "%";
            params.add(like);
            params.add(like);
            params.add(like);
        }
        if (StringUtils.isNotBlank(status)) {
            sql.append(" AND status = ?");
            params.add(status.trim());
        }
        sql.append(" ORDER BY id DESC");

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<TeamMember> list = new ArrayList<>();
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

    public TeamMember findById(Long id) throws SQLException {
        String sql = "SELECT id, name, position, department, phone, base_salary, status, DATE_FORMAT(join_date, '%Y-%m-%d') AS join_date " +
                     "FROM team_member WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setLong(1, id);
            rs = ps.executeQuery();
            return rs.next() ? mapRow(rs) : null;
        } finally {
            DBUtil.close(conn, ps, rs);
        }
    }

    public void insert(TeamMember member) throws SQLException {
        String sql = "INSERT INTO team_member (name, position, department, phone, base_salary, status, join_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, member.getName());
            ps.setString(2, member.getPosition());
            ps.setString(3, member.getDepartment());
            ps.setString(4, member.getPhone());
            ps.setBigDecimal(5, member.getBaseSalary());
            ps.setString(6, member.getStatus());
            ps.setString(7, member.getJoinDate());
            ps.executeUpdate();
        } finally {
            DBUtil.close(conn, ps, null);
        }
    }

    public void update(TeamMember member) throws SQLException {
        String sql = "UPDATE team_member SET name=?, position=?, department=?, phone=?, base_salary=?, status=?, join_date=? WHERE id=?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, member.getName());
            ps.setString(2, member.getPosition());
            ps.setString(3, member.getDepartment());
            ps.setString(4, member.getPhone());
            ps.setBigDecimal(5, member.getBaseSalary());
            ps.setString(6, member.getStatus());
            ps.setString(7, member.getJoinDate());
            ps.setLong(8, member.getId());
            ps.executeUpdate();
        } finally {
            DBUtil.close(conn, ps, null);
        }
    }

    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM team_member WHERE id = ?";
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

    public int countActive() throws SQLException {
        String sql = "SELECT COUNT(*) FROM team_member WHERE status = '在职'";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        } finally {
            DBUtil.close(conn, ps, rs);
        }
    }

    private TeamMember mapRow(ResultSet rs) throws SQLException {
        TeamMember m = new TeamMember();
        m.setId(rs.getLong("id"));
        m.setName(rs.getString("name"));
        m.setPosition(rs.getString("position"));
        m.setDepartment(rs.getString("department"));
        m.setPhone(rs.getString("phone"));
        m.setBaseSalary(rs.getBigDecimal("base_salary"));
        m.setStatus(rs.getString("status"));
        m.setJoinDate(rs.getString("join_date"));
        return m;
    }
}
