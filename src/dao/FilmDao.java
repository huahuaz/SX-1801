package dao;

import util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilmDao {
    //添加电影信息
    public int addFilm(String fname, String ftype, String fyear, String flength, String fdate, String director, String actors, String fdis, String farea, String fimage) throws SQLException {
        //获取连接
        Connection conn = DbUtil.getConn();
        int result = 0;
        String sql = "insert into tb_film values(null,?,?,?,?,?,?,?,?,?,?)";
        //获取预编译对象
        PreparedStatement ps = conn.prepareStatement(sql);
        //设置参数
        ps.setObject(1, fname);
        ps.setObject(2, ftype);
        ps.setObject(3, fyear);
        ps.setObject(4, flength);
        ps.setObject(5, fdate);
        ps.setObject(6, director);
        ps.setObject(7, actors);
        ps.setObject(8, fdis);
        ps.setObject(9, farea);
        ps.setObject(10, fimage);

        result = ps.executeUpdate();
        DbUtil.close();
        return result;
    }

    //查看所有的电影信息
    public List<Map<String, Object>> findAll() throws SQLException {
        String sql = "select * from tb_film order by fid desc";
        //创建集合对象
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;
        Connection conn = DbUtil.getConn();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            map = new HashMap<String, Object>();
            map.put("fid", rs.getObject("fid"));
            map.put("fname", rs.getObject("fname"));
            map.put("ftype", rs.getObject("ftype"));
            map.put("fyear", rs.getObject("fyear"));
            map.put("flength", rs.getObject("flength"));
            map.put("fdate", rs.getObject("fdate"));
            map.put("director", rs.getObject("director"));
            map.put("actors", rs.getObject("actors"));
            map.put("fdis", rs.getObject("fdis"));
            map.put("farea", rs.getObject("farea"));
            map.put("fimage", rs.getObject("fimage"));
            list.add(map);
        }
        DbUtil.close();
        return list;
    }

    //根据电影编号查看电影
    public Map<String, Object> findByFid(String fid) throws SQLException {
        String sql="select * from tb_film where fid=?";
        Map<String,Object> map = null;
        Connection conn = DbUtil.getConn();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setObject(1,fid);
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
            map = new HashMap<String, Object>();
            map.put("fid", rs.getObject("fid"));
            map.put("fname", rs.getObject("fname"));
            map.put("ftype", rs.getObject("ftype"));
            map.put("fyear", rs.getObject("fyear"));
            map.put("flength", rs.getObject("flength"));
            map.put("fdate", rs.getObject("fdate"));
            map.put("director", rs.getObject("director"));
            map.put("actors", rs.getObject("actors"));
            map.put("fdis", rs.getObject("fdis"));
            map.put("farea", rs.getObject("farea"));
            map.put("fimage", rs.getObject("fimage"));
        }
        DbUtil.close();
        return map;
    }

    public static void main(String[] args) throws SQLException {
        FilmDao f = new FilmDao();
        /*f.addFilm("八佰","战争 历史","2020","147","2020-08-21","管虎","黄志忠、欧豪、王千源、姜武、张译、杜淳、魏晨、李晨、俞灏明",
                "1937年淞沪会战末期，中日双方激战已持续三个月，上海濒临沦陷。第88师262旅524团团副谢晋元（杜淳饰）率420余人，孤军坚守最后的防线，留守上海四行仓库。与租界一河之隔，造就了罕见的被围观的战争。为壮声势，实际人数四百人而对外号称八百人。“八百壮士”奉命留守上海闸北，在苏州河畔的四行仓库鏖战四天，直至10月30日才获令撤往英租界",
                "中国大陆","D:\\Users\\82427\\IdeaProjects\\JavaEEsx\\web\\film_images\\babai.png");*/
        /*List<Map<String,Object>> l = f.findAll();
        System.out.println(l);*/
        Map<String,Object> m = f.findByFid("1");
        System.out.println(m);
    }
}
