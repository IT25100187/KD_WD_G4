package com.bakery.servlet;

import com.bakery.dao.CustomerDAO;
import com.bakery.model.Customer;
import com.bakery.util.DAOFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/customers")
public class CustomerServlet extends HttpServlet {

    private CustomerDAO customerDAO;

    @Override public void init() {
        customerDAO = DAOFactory.getInstance(getServletContext()).customers();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "list";
        switch (action) {
            case "new"    -> showForm(req, res, null);
            case "edit"   -> showEditForm(req, res);
            case "view"   -> viewCustomer(req, res);
            case "delete" -> deleteCustomer(req, res);
            case "search" -> searchCustomers(req, res);
            default       -> listCustomers(req, res);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("save".equals(action))        saveCustomer(req, res);
        else if ("update".equals(action)) updateCustomer(req, res);
        else                              res.sendRedirect(req.getContextPath() + "/customers");
    }

    private void listCustomers(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setAttribute("customers", customerDAO.findAllSorted());
        req.setAttribute("totalCustomers", customerDAO.count());
        req.setAttribute("premiumCount", customerDAO.findByMembership("PREMIUM").size());
        forward(req, res, "customer-list");
    }

    private void showForm(HttpServletRequest req, HttpServletResponse res, Customer c)
            throws ServletException, IOException {
        req.setAttribute("customer", c);
        forward(req, res, "customer-form");
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        Customer c = findOrRedirect(req, res);
        if (c == null) return;
        req.setAttribute("editMode", true);
        showForm(req, res, c);
    }

    private void viewCustomer(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        Customer c = findOrRedirect(req, res);
        if (c == null) return;
        req.setAttribute("customer", c);
        req.setAttribute("orders", DAOFactory.getInstance(getServletContext()).orders().findByCustomerId(c.getId()));
        forward(req, res, "customer-detail");
    }

    private void saveCustomer(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        Customer c = build(req);
        boolean ok = customerDAO.save(c);
        flash(req, ok ? "success" : "error",
              ok ? "Customer " + c.getName() + " added successfully!"
                 : "Failed to save customer.");
        res.sendRedirect(req.getContextPath() + "/customers");
    }

    private void updateCustomer(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        Customer c = build(req);
        c.setId(parseInt(req.getParameter("customerId"), 0));
        boolean ok = customerDAO.update(c);
        flash(req, ok ? "success" : "error",
              ok ? "Customer updated successfully." : "Update failed.");
        res.sendRedirect(req.getContextPath() + "/customers");
    }

    private void deleteCustomer(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        int id = parseInt(req.getParameter("id"), 0);
        boolean ok = customerDAO.delete(id);
        flash(req, ok ? "success" : "error",
              ok ? "Customer #" + id + " removed from system." : "Delete failed.");
        res.sendRedirect(req.getContextPath() + "/customers");
    }

    private void searchCustomers(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String q = req.getParameter("q");
        List<Customer> results = (q == null || q.isBlank())
                ? customerDAO.findAllSorted() : customerDAO.search(q.trim());
        req.setAttribute("customers", results);
        req.setAttribute("searchQuery", q);
        req.setAttribute("totalCustomers", customerDAO.count());
        req.setAttribute("premiumCount", customerDAO.findByMembership("PREMIUM").size());
        forward(req, res, "customer-list");
    }

    private Customer build(HttpServletRequest req) {
        Customer c = new Customer();
        c.setName(clean(req.getParameter("name")));
        c.setEmail(clean(req.getParameter("email")));
        c.setPhone(clean(req.getParameter("phone")));
        c.setAddress(clean(req.getParameter("address")));
        c.setMembershipType(req.getParameter("membershipType") != null
                ? req.getParameter("membershipType") : "REGULAR");
        return c;
    }

    private Customer findOrRedirect(HttpServletRequest req, HttpServletResponse res) throws IOException {
        int id = parseInt(req.getParameter("id"), 0);
        Customer c = customerDAO.findById(id);
        if (c == null) { flash(req,"error","Customer #"+id+" not found."); res.sendRedirect(req.getContextPath()+"/customers"); }
        return c;
    }

    private void forward(HttpServletRequest req, HttpServletResponse res, String view)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/" + view + ".jsp").forward(req, res);
    }
    private void flash(HttpServletRequest req, String t, String m) { req.getSession().setAttribute(t+"Msg", m); }
    private int parseInt(String v, int d) { try { return Integer.parseInt(v.trim()); } catch (Exception e) { return d; } }
    private String clean(String v) { return v == null ? "" : v.trim(); }
}
