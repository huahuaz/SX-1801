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

//ע����ʵ�ַ
@WebServlet("/user.action")
public class UserServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//������������Ӧ����ı��뼯����Ϊutf-8����������
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		//��ǰ�˴���һ��op���ԣ������������ݵ�ֵ��ʲô����
		String op=request.getParameter("op");
		//�ַ����Ƚ�s1.equals(s2)		true	false
		if("register".equals(op)) {
			//ע��
			doRegister(request,response);
		}else if("login".equals(op)) {
			//��¼
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
            //����json��ʽ�ַ��������  {"code":1}
            Map<String, Object> jsonMap = new HashMap<String, Object>();
            jsonMap.put("code", result);
            //תΪjson��ʽ���ַ���
            //����gson����
            Gson gson = new Gson();
            String info = gson.toJson(jsonMap);
            System.out.println("info:"+info);
            response.getWriter().print(info);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

	private void doCheckEmail(HttpServletRequest request, HttpServletResponse response) {
		
		//��ҳ���ȡ����
		String email=request.getParameter("email");
		//���õ�¼����
		try {
			Map<String,Object> user=UserDAO.findByEmail(email);
			int result=0;
			if(user!=null) {
				//��¼�ɹ�
				result=1;
			}
			
			Map<String,Object> jsonMap=new HashMap<String,Object>();
			jsonMap.put("code", result);
			//תΪjson��ʽ���ַ���
			//����gson����
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
		//���õ�¼����
		try {
			Map<String,Object> user=UserDAO.login(email, upwd);
			int result=0;
			if(user!=null) {
				//��¼�ɹ�
				result=1;
				//����¼���û����ݴ洢��HttpSession
				//�������رյ�ʱ������
				request.getSession().setAttribute("user", user);
				//���ҳ����Թ���˶��������
				
			}
			
			Map<String,Object> jsonMap=new HashMap<String,Object>();
			jsonMap.put("code", result);
			//תΪjson��ʽ���ַ���
			//����gson����
			Gson gson=new Gson();
			String info=gson.toJson(jsonMap);
			response.getWriter().print(info);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void doRegister(HttpServletRequest request, HttpServletResponse response) {
		//��ҳ���ȡ�û��������룬����
		String email=request.getParameter("email");
		String upwd=request.getParameter("upwd");
		String uname=request.getParameter("uname");
		//����UserDAO�еķ�����UserDAO���еķ����Ǿ�̬������ֱ��ʹ������.��̬������
		try {
			int result=UserDAO.register(uname, email, upwd);
			//����json��ʽ�ַ��������{"code":1}
			Map<String,Object> jsonMap=new HashMap<String,Object>();
			jsonMap.put("code", result);
			//����Json��ʽ���ַ���
			//����gson����
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
		doGet(request, response);//���е�����������doGet�д���
	}
	
	
	
}
