package com.catshop.dao;
import com.catshop.javabean.UserBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {
    //登录查询
    public UserBean login(String username,String password){
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        UserBean user = null;
        String sql = "select * from user where username=? and password=?";
        try{
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,username);
            pstmt.setString(2,password);
            rs = pstmt.executeQuery();
            if(rs.next()){
                user = new UserBean();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            DBUtil.close(conn,pstmt,rs);
        }
        return user;
    }
    //注册新增用户
    public boolean register(String username,String password){
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "insert into user(username,password) values (?,?)";
        try{
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,username);
            pstmt.setString(2,password);
            int count = pstmt.executeUpdate();
            return count>0;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }finally{
            DBUtil.close(conn,pstmt,rs);
        }
    }
}
