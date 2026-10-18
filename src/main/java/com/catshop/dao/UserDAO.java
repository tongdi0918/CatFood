package com.catshop.dao;

import com.catshop.javabean.UserBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;

public class UserDAO {

    //登录查询
    public UserBean login(String username, String password) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        UserBean user = null;
        String sql = "select * from `user` where username=? and password=?";
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                user = new UserBean();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return user;
    }

    //注册新增用户
    public boolean register(String username, String password) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "insert into `user`(username,password,role) values (?,?,'user')";
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            int count = pstmt.executeUpdate();
            System.out.println("影响行数count="+count);
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("注册数据库异常!");
            return false;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    // ========== 下面是新增的4个方法 ==========
    // 根据用户名查询单个用户（个人中心）
    public UserBean findUserByUsername(String username){
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        UserBean user = null;
        String sql = "select * from `user` where username = ?";
        try{
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,username);
            rs = pstmt.executeQuery();
            if(rs.next()){
                user = new UserBean();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setName(rs.getString("name"));
                user.setPhone(rs.getString("phone"));
                user.setRole(rs.getString("role"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            DBUtil.close(conn,pstmt,rs);
        }
        return user;
    }
    // 普通用户更新个人资料：姓名、手机号
    public boolean updateUserInfo(UserBean user){
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "update `user` set name=?, phone=? where id=?";
        try{
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,user.getName());
            pstmt.setString(2,user.getPhone());
            pstmt.setInt(3,user.getId());
            int row = pstmt.executeUpdate();
            return row>0;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }finally{
            DBUtil.close(conn,pstmt);
        }
    }
    // 查询全部用户（管理员用户列表）
    public List<UserBean> findAllUser(){
        List<UserBean> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "select * from `user`";
        try{
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while(rs.next()){
                UserBean user = new UserBean();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setName(rs.getString("name"));
                user.setPhone(rs.getString("phone"));
                user.setRole(rs.getString("role"));
                list.add(user);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            DBUtil.close(conn,pstmt,rs);
        }
        return list;
    }
    // 更新用户信息-管理员
    public boolean updateAllUser(UserBean user){
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "update user set username=?,password=?,name=?,phone=? where id=?";
        try{
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,user.getUsername());
            pstmt.setString(2,user.getPassword());
            pstmt.setString(3,user.getName());
            pstmt.setString(4,user.getPhone());
            pstmt.setInt(5,user.getId());
            int row = pstmt.executeUpdate();
            return row>0;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }finally{
            DBUtil.close(conn,pstmt);
        }
    }

    //编辑用户-管理员
    public UserBean findById(int id){
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        UserBean user = null;
        try{
            conn = DBUtil.getConnection();
            String sql = "select * from user where id=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,id);
            rs = pstmt.executeQuery();
            if(rs.next()){
                user = new UserBean();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setName(rs.getString("name"));
                user.setPhone(rs.getString("phone"));
                user.setRole(rs.getString("role"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            DBUtil.close(conn,pstmt,rs);
        }
        return user;
    }

    public boolean update(UserBean user){
        Connection conn = null;
        PreparedStatement pstmt = null;
        try{
            conn = DBUtil.getConnection();
            String sql = "update user set username=?,name=?,phone=?,role=? where id=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,user.getUsername());
            pstmt.setString(2,user.getName());
            pstmt.setString(3,user.getPhone());
            pstmt.setString(4,user.getRole());
            pstmt.setInt(5,user.getId());
            int row = pstmt.executeUpdate();
            return row > 0;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }finally{
            DBUtil.close(conn,pstmt);
        }
    }
    //删除用户
    public boolean delete(int id){
        Connection conn = null;
        PreparedStatement pstmt = null;
        try{
            conn = DBUtil.getConnection();
            String sql = "delete from user where id=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,id);
            int row = pstmt.executeUpdate();
            return row>0;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }finally{
            DBUtil.close(conn,pstmt);
        }
    }
    // 重置用户密码
    public void resetPassword(int id,String newPwd){
        Connection conn = null;
        PreparedStatement pst = null;
        try{
            conn = DBUtil.getConnection();
            String sql = "update user set password=? where id=?";
            pst = conn.prepareStatement(sql);
            pst.setString(1,newPwd);
            pst.setInt(2,id);
            pst.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            DBUtil.close(conn,pst);
        }
    }
}