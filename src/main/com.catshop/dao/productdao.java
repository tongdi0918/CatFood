package com.catshop.dao;

import com.catshop.javabean.Product;
import java.util.ArrayList;
import java.util.List;

public class ProductDao {

    //模拟数据库的商品列表
    public List<Product> findAllProduct(){
        List<Product> list = new ArrayList<>();
        Product p1 = new Product(1,"美味猫粮",69.9,"img/m1.jpg","营养均衡的成猫猫粮");
        Product p2 = new Product(2,"冻干零食",25.5,"img/m2.jpg","高蛋白猫咪冻干");
        Product p3 = new Product(3,"猫抓板",35.0,"img/m3.jpg","耐磨瓦楞纸猫抓板");
        list.add(p1);
        list.add(p2);
        list.add(p3);
        return list;
    }

    //根据id查询单个商品
    public Product findProductById(int id){
        List<Product> all = findAllProduct();
        for(Product p : all){
            if(p.getId() == id){
                return p;
            }
        }
        return null;
    }
}
