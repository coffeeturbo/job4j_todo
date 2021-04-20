package todo.servlet;

import org.json.JSONObject;
import todo.model.User;
import todo.repository.ItemRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("user/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        User user = User.builder()
                .email(req.getParameter("email"))
                .password(req.getParameter("password"))
                .build();

        ItemRepository.getInstance().save(user);
        HttpSession ses = req.getSession();
        ses.setAttribute("user", user);

        resp.sendRedirect(req.getContextPath() + "/");
    }
}
