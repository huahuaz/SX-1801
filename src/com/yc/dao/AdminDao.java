
package com.yc.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.yc.utils.DbUtil;

public class AdminDao {
    /**
     * 管理员登录
     */
    public static Map<String, Object> login(String name,String pwd) throws SQLException {
        Map<String, Object> map = null;
        Connection conn = DbUtil.getConn();
        String sql = "select * from tb_admin where aname=? and apwd=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setObject(1,name);
        ps.setObject(2,pwd);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            map = new HashMap<String, Object>();
            map.put("aid", rs.getObject("aid"));
            map.put("aname", rs.getObject("aname"));
            map.put("apwd", rs.getObject("apwd"));
        }
        
        return map;
    }

    public static void main(String[] args) throws SQLException {
        Map<String,Object> map = login("yc","a");
        System.out.println(map);
    }
}
