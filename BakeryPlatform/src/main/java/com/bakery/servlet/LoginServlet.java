package com.bakery.servlet;

import com.bakery.model.Admin;
import com.bakery.util.DAOFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        // If already logged in, redirect to dashboard
        if (req.getSession().getAttribute("loggedInAdmin") != null) {
            res.sendRedirect(req.getContextPath() + "/dashboard");
            return;
        }
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        Admin admin = DAOFactory.getInstance(getServletContext())
                               .admins().authenticate(username, password);

        if (admin != null) {
            req.getSession().setAttribute("loggedInAdmin", admin);
            req.getSession().setAttribute("successMsg", "Welcome back, " + admin.getFullName() + "!");
            res.sendRedirect(req.getContextPath() + "/dashboard");
        } else {
            req.setAttribute("errorMsg", "Invalid username or password.");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, res);
        }
    }
}
