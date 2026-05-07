package com.bakery.servlet;

import com.bakery.util.DAOFactory;
import com.bakery.model.Order.Status;
import com.bakery.model.Payment.PaymentStatus;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        DAOFactory dao = DAOFactory.getInstance(getServletContext());

        // Order stats
        req.setAttribute("totalOrders",    dao.orders().count());
        req.setAttribute("pendingOrders",  dao.orders().countByStatus(Status.PENDING));
        req.setAttribute("confirmedOrders",dao.orders().countByStatus(Status.CONFIRMED));
        req.setAttribute("completedOrders",dao.orders().countByStatus(Status.COMPLETED));

        // Other stats
        req.setAttribute("totalCustomers", dao.customers().count());
        req.setAttribute("totalProducts",  dao.products().count());
        req.setAttribute("totalReviews",   dao.reviews().count());
        req.setAttribute("pendingReviews", dao.reviews().findPending().size());
        req.setAttribute("totalPayments",  dao.payments().count());
        req.setAttribute("totalRevenue",   dao.payments().totalRevenue());

        // Recent orders (top 5)
        var orders = dao.orders().findAllSorted();
        req.setAttribute("recentOrders", orders.size() > 5 ? orders.subList(0, 5) : orders);

        // Recent reviews
        var reviews = dao.reviews().findAllSorted();
        req.setAttribute("recentReviews", reviews.size() > 3 ? reviews.subList(0, 3) : reviews);

        req.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(req, res);
    }
}
