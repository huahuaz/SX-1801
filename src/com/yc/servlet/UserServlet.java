package com.yc.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.yc.bean.User;
import com.yc.dao.UserDAO;

//注解访问地址
@WebServlet("/user.action")
public class UserServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//将请求对象和相应对象的编码集设置为utf-8，中文乱码
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		//从前端传入一个op属性，根据他所传递的值是什么操作
		String op=request.getParameter("op");
		//字符串比较s1.equals(s2)		true	false
		if("register".equals(op)) {
			//注册
			doRegister(request,response);
		}else if("login".equals(op)) {
			//登录
			doLogin(request,response);
		}else if("checkEmail".equals(op)){
			doCheckEmail(request,response);
		}else if ("addMessage".equals(op)) {
            doAddMessage(request,response);
        }else if("findMessage".equals(op)) {
        	doFindMessage(request,response);
        }
	}
	
	private void doFindMessage(HttpServletRequest request, HttpServletResponse response) {
		UserDAO dao=new UserDAO();
		try {
			List<User> user=dao.findMessage();
			Gson gson=new Gson();
			String info=gson.toJson(user);
			response.getWriter().print(info);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	protected void doAddMessage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uname = request.getParameter("uname");
        String email = request.getParameter("email");
        String message = request.getParameter("message");

        try {
            int result = UserDAO.updateMessage(uname, email, message);
            //返回json格式字符串给外界  {"code":1}
            Map<String, Object> jsonMap = new HashMap<String, Object>();
            jsonMap.put("code", result);
            //转为json格式的字符串
            //创建gson对象
            Gson gson = new Gson();
            String info = gson.toJson(jsonMap);
            System.out.println("info:"+info);
            response.getWriter().print(info);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

	private void doCheckEmail(HttpServletRequest request, HttpServletResponse response) {
		
		//从页面获取密码
		String email=request.getParameter("email");
		//调用登录方法
		try {
			Map<String,Object> user=UserDAO.findByEmail(email);
			int result=0;
			if(user!=null) {
				//登录成功
				result=1;
			}
			
			Map<String,Object> jsonMap=new HashMap<String,Object>();
			jsonMap.put("code", result);
			//转为json格式的字符串
			//创建gson对象
			Gson gson=new Gson();
			String info=gson.toJson(jsonMap);
			response.getWriter().print(info);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	private void doLogin(HttpServletRequest request, HttpServletResponse response) {
		String email=request.getParameter("email");
		String upwd=request.getParameter("upwd");
		//调用登录方法
		try {
			Map<String,Object> user=UserDAO.login(email, upwd);
			int result=0;
			if(user!=null) {
				//登录成功
				result=1;
				//将登录的用户数据存储到HttpSession
				//服务器关闭的时候销毁
				request.getSession().setAttribute("user", user);
				//多个页面可以共享此对象的数据
				
			}
			
			Map<String,Object> jsonMap=new HashMap<String,Object>();
			jsonMap.put("code", result);
			//转为json格式的字符串
			//创建gson对象
			Gson gson=new Gson();
			String info=gson.toJson(jsonMap);
			response.getWriter().print(info);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void doRegister(HttpServletRequest request, HttpServletResponse response) {
		//从页面获取用户名，密码，邮箱
		String email=request.getParameter("email");
		String upwd=request.getParameter("upwd");
		String uname=request.getParameter("uname");
		//调用UserDAO中的方法，UserDAO所有的方法是静态方法，直接使用类名.静态方法名
		try {
			int result=UserDAO.register(uname, email, upwd);
			//返回json格式字符串给外界{"code":1}
			Map<String,Object> jsonMap=new HashMap<String,Object>();
			jsonMap.put("code", result);
			//返回Json格式的字符串
			//创建gson对象
			Gson gson=new Gson();
			String info=gson.toJson(jsonMap);
			System.out.println("info:    "+info);
			response.getWriter().print(info);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);//所有的请求最终在doGet中处理
	}
	
	
	
}
