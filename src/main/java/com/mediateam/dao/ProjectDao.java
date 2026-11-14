package com.mediateam.dao;

import com.mediateam.model.ProjectItem;
import com.mediateam.util.DBUtil;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class ProjectDao {

    public List<ProjectItem> findAll(String platform, String stage) throws SQLException {
        StringBuilder sql = new StringBuilder(
                "SELECT id, title, platform, owner_id, owner_name, stage, progress, DATE_FORMAT(due_date, '%Y-%m-%d') AS due_date, remark " +
                "FROM team_project WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (StringUtils.isNotBlank(platform)) {
            sql.append(" AND platform = ?");
            params.add(platform.trim());
        }
        if (StringUtils.isNotBlank(stage)) {
            sql.append(" AND stage = ?");
            params.add(stage.trim());
        }
        sql.append(" ORDER BY due_date ASC, id DESC");

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ProjectItem> list = new ArrayList<>();
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

    public void insert(ProjectItem p) throws SQLException {
        String sql = "INSERT INTO team_project (title, platform, owner_id, owner_name, stage, progress, due_date, remark) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, p.getTitle());
            ps.setString(2, p.getPlatform());
            if (p.getOwnerId() != null) {
                ps.setLong(3, p.getOwnerId());
            } else {
                ps.setNull(3, Types.BIGINT);
            }
            ps.setString(4, p.getOwnerName());
            ps.setString(5, p.getStage());
            ps.setInt(6, p.getProgress());
            ps.setString(7, p.getDueDate());
            ps.setString(8, StringUtils.defaultString(p.getRemark()));
            ps.executeUpdate();
        } finally {
            DBUtil.close(conn, ps, null);
        }
    }

    public void updateProgress(Long id, String stage, int progress) throws SQLException {
        String sql = "UPDATE team_project SET stage = ?, progress = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, stage);
            ps.setInt(2, Math.max(0, Math.min(100, progress)));
            ps.setLong(3, id);
            ps.executeUpdate();
        } finally {
            DBUtil.close(conn, ps, null);
        }
    }

    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM team_project WHERE id = ?";
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

    public int countInProgress() throws SQLException {
        String sql = "SELECT COUNT(*) FROM team_project WHERE stage <> '已发布'";
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

    private ProjectItem mapRow(ResultSet rs) throws SQLException {
        ProjectItem p = new ProjectItem();
        p.setId(rs.getLong("id"));
        p.setTitle(rs.getString("title"));
        p.setPlatform(rs.getString("platform"));
        long oid = rs.getLong("owner_id");
        p.setOwnerId(rs.wasNull() ? null : oid);
        p.setOwnerName(rs.getString("owner_name"));
        p.setStage(rs.getString("stage"));
        p.setProgress(rs.getInt("progress"));
        p.setDueDate(rs.getString("due_date"));
        p.setRemark(rs.getString("remark"));
        return p;
    }
}
