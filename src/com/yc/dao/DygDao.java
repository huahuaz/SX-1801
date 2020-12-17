package com.yc.dao;

import com.yc.bean.DygLinks;
import com.yc.utils.DBHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DygDao {

    public List<DygLinks> selectDygLinks() throws SQLException {
        //查询友情链接
        String sql = "select * from tb_links";
        List<DygLinks> list;
        list = DBHelper.selectList(sql, new DBHelper.ResultSetMapper<DygLinks>() {
            @Override
            public DygLinks map(ResultSet rs) throws SQLException {
                DygLinks dl = new DygLinks();
                dl.setId(rs.getInt("id"));
                dl.setHref(rs.getString("href"));
                dl.setName(rs.getString("name"));
                return dl;
            }
        });
        return list;
    }
}
