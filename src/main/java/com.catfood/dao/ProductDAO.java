package com.catfood.dao;

import com.catfood.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    //1 查询【前台】所有上架商品 status=1
    public List<Product> findAllOnSale() {
        List<Product> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "select * from product where status = 1";
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setPid(rs.getInt("pid"));
                product.setPname(rs.getString("pname"));
                product.setPrice(rs.getDouble("price"));
                product.setCategory(rs.getString("category"));
                product.setStock(rs.getInt("stock"));
                product.setImage(rs.getString("image"));
                product.setDescription(rs.getString("description"));
                product.setStatus(rs.getInt("status"));
                list.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return list;
    }

    //2 根据分类查询上架商品
    public List<Product> findByCategory(String category) {
        List<Product> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "select * from product where status=1 and category=?";
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, category);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setPid(rs.getInt("pid"));
                product.setPname(rs.getString("pname"));
                product.setPrice(rs.getDouble("price"));
                product.setCategory(rs.getString("category"));
                product.setStock(rs.getInt("stock"));
                product.setImage(rs.getString("image"));
                product.setDescription(rs.getString("description"));
                product.setStatus(rs.getInt("status"));
                list.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return list;
    }

    //3 模糊搜索上架商品
    public List<Product> searchProduct(String keyword) {
        List<Product> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "select * from product where status=1 and pname like ?";
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + keyword + "%");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setPid(rs.getInt("pid"));
                product.setPname(rs.getString("pname"));
                product.setPrice(rs.getDouble("price"));
                product.setCategory(rs.getString("category"));
                product.setStock(rs.getInt("stock"));
                product.setImage(rs.getString("image"));
                product.setDescription(rs.getString("description"));
                product.setStatus(rs.getInt("status"));
                list.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return list;
    }

    //4 根据pid查询单个商品（前台详情）
    public Product findById(Integer pid) {
        Product product = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "select * from product where pid = ?";
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, pid);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                product = new Product();
                product.setPid(rs.getInt("pid"));
                product.setPname(rs.getString("pname"));
                product.setPrice(rs.getDouble("price"));
                product.setCategory(rs.getString("category"));
                product.setStock(rs.getInt("stock"));
                product.setImage(rs.getString("image"));
                product.setDescription(rs.getString("description"));
                product.setStatus(rs.getInt("status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return product;
    }
    // 5 组合条件查询：分类 + 关键词 同时筛选
    public List<Product> findByCondition(String category, String keyword) {
        List<Product> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        StringBuilder sql = new StringBuilder("select * from product where status=1 ");
        List<Object> params = new ArrayList<>();

        //分类不是全部，拼接分类条件
        if(category != null && !"全部".equals(category)){
            sql.append(" and category = ? ");
            params.add(category);
        }
        //关键词不为空，拼接模糊查询
        if(keyword != null && keyword.trim().length()>0){
            sql.append(" and pname like ? ");
            params.add("%"+keyword.trim()+"%");
        }

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql.toString());
            //给?赋值
            for(int i=0; i<params.size(); i++){
                pstmt.setObject(i+1, params.get(i));
            }
            rs = pstmt.executeQuery();
            while (rs.next()){
                Product product = new Product();
                product.setPid(rs.getInt("pid"));
                product.setPname(rs.getString("pname"));
                product.setPrice(rs.getDouble("price"));
                product.setCategory(rs.getString("category"));
                product.setStock(rs.getInt("stock"));
                product.setImage(rs.getString("image"));
                product.setDescription(rs.getString("description"));
                product.setStatus(rs.getInt("status"));
                list.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(conn,pstmt,rs);
        }
        return list;
    }

    //【管理员后台】查询全部商品（上架+下架都查出来）
    public List<Product> findAllAdmin() {
        List<Product> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "select * from product";
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setPid(rs.getInt("pid"));
                product.setPname(rs.getString("pname"));
                product.setPrice(rs.getDouble("price"));
                product.setCategory(rs.getString("category"));
                product.setStock(rs.getInt("stock"));
                product.setImage(rs.getString("image"));
                product.setDescription(rs.getString("description"));
                product.setStatus(rs.getInt("status"));
                list.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return list;
    }

    //后台新增商品
    public int addProduct(Product product) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int rows = 0;
        String sql = "insert into product(pname,price,category,stock,image,description,status) values(?,?,?,?,?,?,?)";
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, product.getPname());
            pstmt.setDouble(2, product.getPrice());
            pstmt.setString(3, product.getCategory());
            pstmt.setInt(4, product.getStock());
            pstmt.setString(5, product.getImage());
            pstmt.setString(6, product.getDescription());
            pstmt.setInt(7, product.getStatus());
            rows = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt);
        }
        return rows;
    }

    //后台编辑修改商品
    public int updateProduct(Product product) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int rows = 0;
        String sql = "update product set pname=?,price=?,category=?,stock=?,image=?,description=?,status=? where pid=?";
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, product.getPname());
            pstmt.setDouble(2, product.getPrice());
            pstmt.setString(3, product.getCategory());
            pstmt.setInt(4, product.getStock());
            pstmt.setString(5, product.getImage());
            pstmt.setString(6, product.getDescription());
            pstmt.setInt(7, product.getStatus());
            pstmt.setInt(8, product.getPid());
            rows = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt);
        }
        return rows;
    }

    //后台删除商品
    public int deleteProduct(Integer pid) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int rows = 0;
        String sql = "delete from product where pid=?";
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, pid);
            rows = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt);
        }
        return rows;
    }

    //修改上下架状态
    public int updateStatus(Integer pid, Integer status) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int rows = 0;
        String sql = "update product set status=? where pid=?";
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, status);
            pstmt.setInt(2, pid);
            rows = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt);
        }
        return rows;
    }
}
