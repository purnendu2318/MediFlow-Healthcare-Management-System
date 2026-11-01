package com.mediflow.repository;

import com.mediflow.config.Database;
import com.mediflow.model.User;
import java.sql.*;

public class UserRepository {
    public User findActiveByEmail(String email) throws Exception {
        String sql = "SELECT id,full_name,email,password_hash,role FROM users WHERE email=? AND status='ACTIVE'";
        try (Connection c=Database.getConnection(); PreparedStatement ps=c.prepareStatement(sql)) {
            ps.setString(1,email);
            try(ResultSet rs=ps.executeQuery()) {
                if(!rs.next()) return null;
                return new User(rs.getLong("id"),rs.getString("full_name"),rs.getString("email"),
                    rs.getString("role") + "|" + rs.getString("password_hash"));
            }
        }
    }
    public void createDefaultAdminIfEmpty(String passwordHash) throws Exception {
        try(Connection c=Database.getConnection(); Statement st=c.createStatement();
            ResultSet rs=st.executeQuery("SELECT COUNT(*) FROM users")) {
            rs.next();
            if(rs.getInt(1)==0) {
                try(PreparedStatement ps=c.prepareStatement("INSERT INTO users(full_name,email,password_hash,role) VALUES(?,?,?,'SUPER_ADMIN')")) {
                    ps.setString(1,"MediFlow Administrator"); ps.setString(2,"admin@mediflow.local"); ps.setString(3,passwordHash); ps.executeUpdate();
                }
            }
        }
    }
}
