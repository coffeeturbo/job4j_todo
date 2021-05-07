package todo.servlet;

import todo.model.Category;
import todo.repository.ItemRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class CategoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            List<Category> categories = ItemRepository.getInstance().findAll(Category.class);
            JsonResponse jsonResponse = JsonResponse.builder().response(resp).build();
            jsonResponse.setData(categories);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
