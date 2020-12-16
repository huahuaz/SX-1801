package com.yc.servlet;

import com.google.gson.Gson;
import com.yc.dao.AdminDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/admin.action")
public class AdminServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        //从前端传入op
        String op = request.getParameter("op");
        if ("login".equals(op)){
            doLogin(request,response);
        }
    }

    //登录
    protected void doLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String aname = request.getParameter("aname");
        String apwd = request.getParameter("apwd");
        try {
            Map<String,Object> admin = AdminDao.login(aname,apwd);
            int result = 0;
            if (admin != null){
                result = 1;//登录成功
                request.getSession().setAttribute("admin",admin);
            }

            Map<String,Object> jsonMap = new HashMap<String, Object>();
            jsonMap.put("code",result);
            Gson gson = new Gson();
            String info = gson.toJson(jsonMap);
            response.getWriter().print(info);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
