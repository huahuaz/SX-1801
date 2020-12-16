package com.yc.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.yc.bean.Film;
import com.yc.biz.FilmBiz;
import com.yc.dao.FilmDAO;

@WebServlet("/film.action")
public class FilmServlet extends HttpServlet {
	//实例化FilmDao
		FilmDAO dao = new FilmDAO();
		FilmBiz biz=new FilmBiz();
		@Override
		protected void doGet(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			String op=request.getParameter("op");
			if("add".equals(op)){
				doAdd(request,response);
			}else if("findByFid".equals(op)){
				doFindById(request,response);
			}else if("findAll".equals(op)){
				doFindAll(request,response);
			}else if("deleteFilm".equals(op)) {
				doDeleteFilm(request,response);
			}else if("findByPage".equals(op)) {
				doFindPage(request,response);
			}
		}
		
		
		private void doFindPage(HttpServletRequest request, HttpServletResponse response) {
			String pageNum=request.getParameter("pageNum");
			String pageSize=request.getParameter("pageSize");
			try {
				Map<String,Object> map=biz.findByPage(Integer.parseInt(pageNum), Integer.parseInt(pageSize));
				Gson gson=new Gson();
				String info=gson.toJson(map);
				response.getWriter().print(info);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}


		private void doDeleteFilm(HttpServletRequest request, HttpServletResponse response)  {
			
			String fid=request.getParameter("fid");
			System.out.println("fid:"+fid);
			int id=Integer.parseInt(fid);
			
			try {
				int result = dao.deleteFilm(id);
				Gson gson=new Gson();
				String info=gson.toJson(result);
				response.getWriter().print(info);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}


		/**
		 * 添加电影信息
		 * @param request
		 * @param response
		 */

		protected void doAdd(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
			//从页面获取
			String fname=request.getParameter("fname");
			String ftype=request.getParameter("ftype");
			String fyear=request.getParameter("fyear");
			String flength=request.getParameter("flength");
			String fdate=request.getParameter("fdate");
			String director=request.getParameter("director");
			String actors=request.getParameter("actors");
			String fdis=request.getParameter("fdis");
			String farea=request.getParameter("farea");
			String imagepath=request.getParameter("fimage");
			imagepath="film_images/"+imagepath;//film_image/jiangziyan,jpg
			
			try{
				int result=dao.addFilm(fname, ftype, fyear, flength, fdate, director, actors, fdis, farea, imagepath);
				Map<String,Object> jsonMap=new HashMap<String,Object>();
				jsonMap.put("code", result);
				Gson gson=new Gson();
				String info=gson.toJson(jsonMap);
				response.getWriter().print(info);
				
						
			}catch (Exception e){
				e.printStackTrace();
			}
			
		}
		/**
		 * 根据编号查询
		 * @param request
		 * @param response
		 * @throws ServletException
		 * @throws IOException
		 */
		protected void doFindById(HttpServletRequest request, HttpServletResponse response) 
				throws ServletException, IOException {
			String fid=request.getParameter("fid");
			try{
				Map<String,Object> map =dao.findByFid(fid);
				Gson gson =new Gson();
				String info=gson.toJson(map);
				response.getWriter().print(info);
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		/**
		 * 查看所有
		 * @param request
		 * @param response
		 * @throws ServletException
		 * @throws IOException
		 */
		
		protected void doFindAll(HttpServletRequest request, HttpServletResponse response) 
				throws ServletException, IOException {
			
			try{
				
				List<Map<String,Object>> list =dao.findAll();
				Gson gson =new Gson();
				String info=gson.toJson(list);
				response.getWriter().print(info);
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		protected void doPost(HttpServletRequest request, HttpServletResponse response) 
				throws ServletException, IOException {
			doGet(request,response);
		}

}
