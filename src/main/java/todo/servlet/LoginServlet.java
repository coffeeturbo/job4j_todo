package todo.servlet;

import todo.model.User;
import todo.repository.ItemRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("user/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            User dbUser = ItemRepository.getInstance().findByEmail(req.getParameter("email"));

            if (dbUser == null) {
                throw new Exception(String.format("User with email %s not foud", req.getParameter("email")));
            }

            if (!dbUser.getPassword().equals(req.getParameter("password"))) {
                throw new Exception("wrong password");
            }

            HttpSession ses = req.getSession();
            ses.setAttribute("user", dbUser);
            resp.sendRedirect(req.getContextPath() + "/");

        } catch (Exception e) {
            resp.sendError(400, e.getMessage());
        }
    }
}
