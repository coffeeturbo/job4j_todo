package todo.servlet;

import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import todo.model.Category;
import todo.model.Item;
import todo.model.User;
import todo.repository.ItemRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ItemServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            String done = req.getParameter("done");

            List<Item> items;
            switch (done) {
                case "true" -> items = ItemRepository.getInstance().findByDoneAll(true);
                case "false" -> items = ItemRepository.getInstance().findByDoneAll(false);
                default -> items = ItemRepository.getInstance().findAll(Item.class);
            }

            JsonResponse jsonResponse = JsonResponse.builder().response(resp).build();
            jsonResponse.setData(items);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String desc = req.getParameter("data");

            int id = 0;
            if (req.getParameter("id") != null) {
                id = Integer.parseInt(req.getParameter("id"));
            }

            User user = (User) req.getSession().getAttribute("user");

            JSONObject object = new JSONObject(desc);

            Item item;

            Boolean done = Boolean.parseBoolean(object.get("done").toString());

            if (id != 0) {
                item = ItemRepository.getInstance().findById(Item.class ,id);
                item.setDone(done);
            } else {

                List<Category> categories = new ArrayList<>();
                JSONArray jsonCategories = object.getJSONArray("categories");
                if(jsonCategories != null) {
                    jsonCategories.forEach(categoryId -> {
                        Category category = ItemRepository.getInstance().findById(Category.class, Integer.parseInt(categoryId.toString()));
                        categories.add(category);
                    });
                }

                item = Item.builder()
                    .description(object.get("description").toString())
                    .created(new Date(System.currentTimeMillis()))
                    .done(done)
                    .categories(categories)
                    .user(user)
                .build();
            }

            ItemRepository.getInstance().save(item);

            JsonResponse jsonResponse = JsonResponse.builder().response(resp).build();
            jsonResponse.setData(item);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer id = Integer.valueOf(req.getParameter("id"));

        PrintWriter writer = new PrintWriter(resp.getOutputStream());
        try {
            Item item = Item.builder().id(id).build();
            ItemRepository.getInstance().delete(item);

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
