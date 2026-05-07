package com.bakery.servlet;

import com.bakery.dao.OrderDAO;
import com.bakery.model.Order;
import com.bakery.model.Order.Status;
import com.bakery.util.DAOFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * OrderServlet — handles all Order CRUD operations.
 *
 * Routes:
 *  GET  /orders              → list all
 *  GET  /orders?action=new   → add form
 *  GET  /orders?action=edit&id=X  → edit form
 *  GET  /orders?action=view&id=X  → detail view
 *  GET  /orders?action=delete&id=X → cancel/delete
 *  GET  /orders?action=search&q=X  → search
 *  GET  /orders?action=filter&status=X → filter
 *  GET  /orders?action=status&id=X → status update page
 *  POST /orders?action=save   → create
 *  POST /orders?action=update → update
 *  POST /orders?action=status → update status
 */
@WebServlet("/orders")
public class OrderServlet extends HttpServlet {

    private OrderDAO orderDAO;

    @Override
    public void init() {
        orderDAO = DAOFactory.getInstance(getServletContext()).orders();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "new"    -> showForm(req, res, null);
            case "edit"   -> showEditForm(req, res);
            case "view"   -> viewOrder(req, res);
            case "delete" -> deleteOrder(req, res);
            case "search" -> searchOrders(req, res);
            case "filter" -> filterOrders(req, res);
            case "status" -> showStatusForm(req, res);
            default       -> listOrders(req, res);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "";

        switch (action) {
            case "save"   -> saveOrder(req, res);
            case "update" -> updateOrder(req, res);
            case "status" -> updateStatus(req, res);
            default       -> res.sendRedirect(req.getContextPath() + "/orders");
        }
    }

    private void listOrders(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        List<Order> orders = orderDAO.findAllSorted();
        setStats(req);
        req.setAttribute("orders", orders);
        req.setAttribute("activeFilter", "ALL");
        forward(req, res, "order-list");
    }

    private void showForm(HttpServletRequest req, HttpServletResponse res, Order order)
            throws ServletException, IOException {
        req.setAttribute("order", order);
        req.setAttribute("products", DAOFactory.getInstance(getServletContext()).products().findAvailable());
        req.setAttribute("customers", DAOFactory.getInstance(getServletContext()).customers().findAllSorted());
        req.setAttribute("statuses", Status.values());
        forward(req, res, "order-form");
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        Order order = findOrderOrRedirect(req, res);
        if (order == null) return;
        req.setAttribute("editMode", true);
        showForm(req, res, order);
    }

    private void viewOrder(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        Order order = findOrderOrRedirect(req, res);
        if (order == null) return;
        req.setAttribute("order", order);
        forward(req, res, "order-detail");
    }

    private void saveOrder(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        try {
            Order order = buildFromRequest(req);
            if (!order.isValid()) {
                flash(req, "error", "Please fill all required fields correctly.");
                req.setAttribute("editMode", false);
                showForm(req, res, order);
                return;
            }
            boolean ok = orderDAO.save(order);
            flash(req, ok ? "success" : "error",
                  ok ? "Order #" + order.getId() + " placed successfully for " + order.getCustomerName() + "!"
                     : "Failed to save order. Please try again.");
        } catch (Exception e) {
            flash(req, "error", "Invalid input: " + e.getMessage());
        }
        res.sendRedirect(req.getContextPath() + "/orders");
    }

    private void updateOrder(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        try {
            Order order = buildFromRequest(req);
            order.setId(parseInt(req.getParameter("orderId"), 0));
            boolean ok = orderDAO.update(order);
            flash(req, ok ? "success" : "error",
                  ok ? "Order #" + order.getId() + " updated successfully."
                     : "Update failed.");
        } catch (Exception e) {
            flash(req, "error", "Error: " + e.getMessage());
        }
        res.sendRedirect(req.getContextPath() + "/orders");
    }

    private void deleteOrder(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        int id = parseInt(req.getParameter("id"), 0);
        boolean ok = orderDAO.delete(id);
        flash(req, ok ? "success" : "error",
              ok ? "Order #" + id + " has been cancelled and removed."
                 : "Could not delete Order #" + id);
        res.sendRedirect(req.getContextPath() + "/orders");
    }

    private void searchOrders(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String q = req.getParameter("q");
        List<Order> orders = (q == null || q.isBlank())
                ? orderDAO.findAllSorted()
                : orderDAO.search(q.trim());
        setStats(req);
        req.setAttribute("orders", orders);
        req.setAttribute("searchQuery", q);
        req.setAttribute("activeFilter", "ALL");
        forward(req, res, "order-list");
    }

    private void filterOrders(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String statusStr = req.getParameter("status");
        List<Order> orders = (statusStr == null || statusStr.equals("ALL"))
                ? orderDAO.findAllSorted()
                : orderDAO.findByStatus(Status.fromString(statusStr));
        setStats(req);
        req.setAttribute("orders", orders);
        req.setAttribute("activeFilter", statusStr != null ? statusStr : "ALL");
        forward(req, res, "order-list");
    }

    private void showStatusForm(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        Order order = findOrderOrRedirect(req, res);
        if (order == null) return;
        req.setAttribute("order", order);
        req.setAttribute("statuses", Status.values());
        forward(req, res, "order-status");
    }

    private void updateStatus(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        int    id     = parseInt(req.getParameter("orderId"), 0);
        String status = req.getParameter("status");
        boolean ok    = orderDAO.updateStatus(id, Status.fromString(status));
        flash(req, ok ? "success" : "error",
              ok ? "Order #" + id + " status updated to " + status
                 : "Status update failed.");
        res.sendRedirect(req.getContextPath() + "/orders");
    }

    // ---- Helpers ----

    private void setStats(HttpServletRequest req) {
        req.setAttribute("totalOrders",    orderDAO.count());
        req.setAttribute("pendingCount",   orderDAO.countByStatus(Status.PENDING));
        req.setAttribute("confirmedCount", orderDAO.countByStatus(Status.CONFIRMED));
        req.setAttribute("completedCount", orderDAO.countByStatus(Status.COMPLETED));
        req.setAttribute("totalRevenue",   orderDAO.totalRevenue());
    }

    private Order buildFromRequest(HttpServletRequest req) {
        Order o = new Order();
        o.setCustomerId(parseInt(req.getParameter("customerId"), 0));
        o.setCustomerName(clean(req.getParameter("customerName")));
        o.setProductName(clean(req.getParameter("productName")));
        o.setQuantity(parseInt(req.getParameter("quantity"), 1));
        o.setUnitPrice(parseDouble(req.getParameter("unitPrice"), 0.0));
        String pd = req.getParameter("pickupDate");
        if (pd != null && !pd.isBlank()) o.setPickupDate(LocalDate.parse(pd));
        o.setStatusFromString(req.getParameter("status") != null ? req.getParameter("status") : "PENDING");
        o.setSpecialNotes(clean(req.getParameter("specialNotes")));
        return o;
    }

    private Order findOrderOrRedirect(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        int id = parseInt(req.getParameter("id"), 0);
        Order order = orderDAO.findById(id);
        if (order == null) {
            flash(req, "error", "Order #" + id + " not found.");
            res.sendRedirect(req.getContextPath() + "/orders");
        }
        return order;
    }

    private void forward(HttpServletRequest req, HttpServletResponse res, String view)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/" + view + ".jsp").forward(req, res);
    }

    private void flash(HttpServletRequest req, String type, String msg) {
        req.getSession().setAttribute(type + "Msg", msg);
    }

    private int    parseInt(String v, int def)       { try { return Integer.parseInt(v.trim()); } catch (Exception e) { return def; } }
    private double parseDouble(String v, double def) { try { return Double.parseDouble(v.trim()); } catch (Exception e) { return def; } }
    private String clean(String v) { return v == null ? "" : v.trim().replace("<","&lt;").replace(">","&gt;"); }
}
