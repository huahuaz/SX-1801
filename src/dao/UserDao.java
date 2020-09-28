package dao;

import util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class UserDao {
    /**
     * 用户注册
     *
     * @return
     */
    public static int register(String uname, String email, String pwd) throws SQLException {
        //声明整型变量
        int result = 0;
        //获取连接对象
        Connection conn = DbUtil.getConn();
        String sql = "insert into tb_user values(null,?,?,?,1)";
        //获取预编译对象
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setObject(1, uname);
        ps.setObject(2, email);
        ps.setObject(3, pwd);
        //执行更新操作
        result = ps.executeUpdate();
        if (null != ps) {
            ps.close();
        }
        DbUtil.close();
        return result;
    }

    /**
     * 修改密码
     */
    public static int updatePwd(int uid, String pwd) throws SQLException {
        //声明整型变量
        int result = 0;
        //获取连接对象
        Connection conn = DbUtil.getConn();
        String sql = "update tb_user set upwd=? where uid=?";
        //获取预编译对象
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setObject(1, pwd);
        ps.setObject(2, uid);
        //执行更新操作
        result = ps.executeUpdate();
        DbUtil.close();
        return result;
    }

    /**
     * 用户登录 最多返回一个数据
     */
    public static Map<String, Object> login(String email, String pwd) throws SQLException {
        //声明map变量
        Map<String, Object> map = null;
        Connection conn = DbUtil.getConn();
        String sql = "select uid,uname,upwd,email,state from tb_user where email=? and upwd=? and state=1";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setObject(1, email);
        ps.setObject(2, pwd);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            map = new HashMap<String, Object>();
            map.put("uid", rs.getObject("uid"));
            map.put("uname", rs.getObject("uname"));
            map.put("email", rs.getObject("email"));
            map.put("upwd", rs.getObject("upwd"));
            map.put("state", rs.getObject("state"));
        }
        DbUtil.close();
        return map;
    }

    /**
     * 根据邮箱查询
     */
    public static Map<String, Object> findByEmail(String email) throws SQLException {
        Map<String, Object> map = null;
        Connection conn = DbUtil.getConn();
        String sql = "select * from tb_user where email=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setObject(1,email);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            map = new HashMap<String, Object>();
            map.put("uid", rs.getObject("uid"));
            map.put("uname", rs.getObject("uname"));
            map.put("email", rs.getObject("email"));
            map.put("upwd", rs.getObject("upwd"));
            map.put("state", rs.getObject("state"));
        }
        DbUtil.close();
        return map;
    }

    public static void main(String[] args) throws SQLException {
        /*int result = register("李四","lisi@qq.com","123");
        System.out.println(result);*/
        /*int result = updatePwd(2,"123");
        System.out.println(result);*/
        Map<String, Object> map = login("lisi@qq.com", "12");
        System.out.println(map);
    }
}
