package servlet;

import com.google.gson.Gson;
import dao.FilmDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/film.action")
public class FilmServlet extends HttpServlet {
    //实例化FilmDao
    FilmDao fDao = new FilmDao();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String op = request.getParameter("op");
        if ("add".equals(op)) {
            doAdd(request, response);
        } else if ("findByFid".equals(op)) {
            doFindById(request, response);
        } else if ("findAll".equals(op)) {
            doFindAll(request, response);
        }
    }

    protected void doAdd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //从页面取值
        String fname = request.getParameter("fname");
        String ftype = request.getParameter("ftype");
        String fyear = request.getParameter("fyear");
        String flength = request.getParameter("flength");
        String fdate = request.getParameter("fdate");
        String director = request.getParameter("director");
        String actors = request.getParameter("actors");
        String fdis = request.getParameter("fdis");
        String farea = request.getParameter("farea");
        String fimage = request.getParameter("fimage");
        fimage = "film_images/" + fimage;

        try {
            int result = fDao.addFilm(fname, ftype, fyear, flength, fdate, director, actors, fdis, farea, fimage);
            Map<String, Object> jsonMap = new HashMap<String, Object>();
            jsonMap.put("code",result);
            Gson gson = new Gson();
            String info = gson.toJson(jsonMap);
            response.getWriter().print(info);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * 根据id查询
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doFindById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fid = request.getParameter("fid");
        try {
            Map<String,Object> map = fDao.findByFid(fid);
            Gson gson = new Gson();
            String info = gson.toJson(map);
            response.getWriter().print(info);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * 查询所有
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doFindAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Map<String,Object>> list = fDao.findAll();
            Gson gson = new Gson();
            String info = gson.toJson(list);
            response.getWriter().print(info);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
