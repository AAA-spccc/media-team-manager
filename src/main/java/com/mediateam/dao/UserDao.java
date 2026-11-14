package com.mediateam.dao;

import com.mediateam.model.SysUser;
import com.mediateam.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    public SysUser findByUsernameAndPassword(String username, String password) throws SQLException {
        String sql = "SELECT id, username, password, display_name, role FROM sys_user WHERE username = ? AND password = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            rs = ps.executeQuery();
            if (rs.next()) {
                SysUser user = new SysUser();
                user.setId(rs.getLong("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setDisplayName(rs.getString("display_name"));
                user.setRole(rs.getString("role"));
                return user;
            }
            return null;
        } finally {
            DBUtil.close(conn, ps, rs);
        }
    }
}
