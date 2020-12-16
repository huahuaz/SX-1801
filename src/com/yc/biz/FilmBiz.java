package com.yc.biz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yc.bean.Film;
import com.yc.dao.FilmDAO;

public class FilmBiz {
	
	FilmDAO film=new FilmDAO();
	
	public Map<String,Object>findByPage(Integer pageNum,Integer pageSize) throws Exception{
		List<Film> list=film.findByPage(pageNum, pageSize);
		int rows=film.totalRows();
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("rows", rows);//×ÜÐÐÊý
		map.put("films", list);//
		return map;
		
	}
	
//	public static void main(String[] args) {
//		FilmBiz biz=new FilmBiz();
//		try {
//			Map<String,Object> map=biz.findByPage(1, 3);
//			System.out.println(map);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
