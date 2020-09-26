package servlet;

import com.google.gson.Gson;
import dao.UserDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/user.action")
public class UserServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        //从前端传入一个op属性  根据他所传递的值判断是什么操作
        String op = request.getParameter("op");
        if ("register".equals(op)) {
            //注册
            doRegister(request, response);
        } else if ("login".equals(op)) {
            //登录
            doLogin(request, response);
        } else if ("checkEmail".equals(op)) {
            doCheckEmail(request, response);
        }
    }

    protected void doCheckEmail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //从页面获取密码
        String email = request.getParameter("email");
        Map<String, Object> user;
        try {
            user = UserDao.findByEmail(email);
            int result = 0;
            if (user != null) {
                result = 1;
            }
            //转为json格式的字符串
            //创建gson对象
            Gson gson = new Gson();
            Map<String, Object> jsonMap = new HashMap<String, Object>();
            jsonMap.put("code", result);
            String info = gson.toJson(jsonMap);
            response.getWriter().print(info);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    protected void doRegister(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //从页面获取用户名，密码，邮箱
        String email = request.getParameter("email");
        String upwd = request.getParameter("upwd");
        String uname = request.getParameter("uname");
        //调用UserDao中的方法 UserDao所有的方法都是静态方法 直接使用类名 静态方法名()
        try {
            int result = UserDao.register(uname, email, upwd);
            //返回json格式字符串给外界  {"code":1}
            Map<String, Object> jsonMap = new HashMap<String, Object>();
            jsonMap.put("code", result);
            //转为json格式的字符串
            //创建gson对象
            Gson gson = new Gson();
            String info = gson.toJson(jsonMap);
            response.getWriter().print(info);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //登录
    protected void doLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String upwd = request.getParameter("upwd");
        //调用登录方法
        try {
            Map<String, Object> user = UserDao.login(email, upwd);
            int result = 0;
            if (user != null) {
                //登录成功
                result = 1;
                //将登录的用户数据存储到HttpSession
                request.getSession().setAttribute("user", user);
                //多个页面可以共享此对象的数据
            }

            Map<String, Object> jsonMap = new HashMap<String, Object>();
            jsonMap.put("code", result);
            //转为json格式的字符串
            //创建gson对象
            Gson gson = new Gson();
            String info = gson.toJson(jsonMap);
            response.getWriter().print(info);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);//所有的请求最终处理都在doGet中处理
    }
}
