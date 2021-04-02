package todo.servlet;

import com.google.gson.Gson;
import org.json.JSONObject;
import todo.model.Item;
import todo.repository.ItemRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;

public class ItemServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            String done = req.getParameter("done");

            Collection<Item> items;
            switch (done) {
                case "true": {
                    items = ItemRepository.getInstance().findByDoneAll(true);
                    break;
                }
                default:
                    items = ItemRepository.getInstance().findAll();
            }

            resp.setContentType("text/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setHeader("Access-Control-Allow-Origin", "*");
            PrintWriter writer = new PrintWriter(resp.getOutputStream());

            Gson gson = new Gson();
            writer.println(gson.toJson(items));
            writer.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String desc = req.getParameter("data");
            int id = Integer.parseInt(req.getParameter("id"));
            JSONObject object = new JSONObject(desc);

            Item item;

            Boolean done = Boolean.parseBoolean(object.get("done").toString());

            if (id != 0) {
                item = ItemRepository.getInstance().findById(id);
                item.setDone(done);
            } else {
                LocalDateTime dateTime = LocalDateTime.now();
                item = new Item(object.get("description").toString(), Timestamp.valueOf(dateTime), done);
            }

            ItemRepository.getInstance().save(item);

            resp.setContentType("text/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setHeader("Access-Control-Allow-Origin", "*");
            PrintWriter writer = new PrintWriter(resp.getOutputStream());

            Gson gson = new Gson();
            writer.println(gson.toJson(item));
            writer.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer id = Integer.valueOf(req.getParameter("id"));

        PrintWriter writer = new PrintWriter(resp.getOutputStream());
        try {
            ItemRepository.getInstance().delete(id);

            resp.setContentType("text/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setHeader("Access-Control-Allow-Origin", "*");


            writer.println("{\"success\":true}");


        } catch (Exception e) {
            Gson gson = new Gson();
            writer.println("{\"success\":false, \"error\":" + gson.toJson(e.getMessage()) + "}");
            e.printStackTrace();
        }

        writer.flush();
    }


}
