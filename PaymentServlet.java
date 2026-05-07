package com.bakery.servlet;

import com.bakery.dao.PaymentDAO;
import com.bakery.model.Payment;
import com.bakery.model.Payment.PaymentMethod;
import com.bakery.model.Payment.PaymentStatus;
import com.bakery.util.DAOFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/payments")
public class PaymentServlet extends HttpServlet {

    private PaymentDAO paymentDAO;

    @Override public void init() {
        paymentDAO = DAOFactory.getInstance(getServletContext()).payments();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "list";
        switch (action) {
            case "new"    -> showForm(req, res, null);
            case "edit"   -> showEditForm(req, res);
            case "delete" -> deletePayment(req, res);
            case "search" -> searchPayments(req, res);
            case "filter" -> filterPayments(req, res);
            default       -> listPayments(req, res);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("save".equals(action))        savePayment(req, res);
        else if ("update".equals(action)) updatePayment(req, res);
        else                              res.sendRedirect(req.getContextPath() + "/payments");
    }

    private void listPayments(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setAttribute("payments", paymentDAO.findAllSorted());
        setStats(req);
        req.setAttribute("activeFilter", "ALL");
        forward(req, res, "payment-list");
    }

    private void showForm(HttpServletRequest req, HttpServletResponse res, Payment p)
            throws ServletException, IOException {
        req.setAttribute("payment", p);
        req.setAttribute("orders",   DAOFactory.getInstance(getServletContext()).orders().findAllSorted());
        req.setAttribute("methods",  PaymentMethod.values());
        req.setAttribute("statuses", PaymentStatus.values());
        forward(req, res, "payment-form");
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        Payment p = findOrRedirect(req, res);
        if (p == null) return;
        req.setAttribute("editMode", true);
        showForm(req, res, p);
    }

    private void savePayment(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        Payment p = build(req);
        p.setTransactionRef(paymentDAO.generateRef());
        boolean ok = paymentDAO.save(p);
        flash(req, ok?"success":"error", ok?"Payment recorded for Order #"+p.getOrderId():"Save failed.");
        res.sendRedirect(req.getContextPath() + "/payments");
    }

    private void updatePayment(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        Payment p = build(req);
        p.setId(parseInt(req.getParameter("paymentId"), 0));
        boolean ok = paymentDAO.update(p);
        flash(req, ok?"success":"error", ok?"Payment updated.":"Update failed.");
        res.sendRedirect(req.getContextPath() + "/payments");
    }

    private void deletePayment(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        int id = parseInt(req.getParameter("id"), 0);
        boolean ok = paymentDAO.delete(id);
        flash(req, ok?"success":"error", ok?"Payment #"+id+" removed.":"Delete failed.");
        res.sendRedirect(req.getContextPath() + "/payments");
    }

    private void searchPayments(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String q = req.getParameter("q");
        List<Payment> all = paymentDAO.findAllSorted();
        if (q != null && !q.isBlank()) {
            String lower = q.toLowerCase();
            all = all.stream().filter(p ->
                    p.getCustomerName().toLowerCase().contains(lower)
                    || String.valueOf(p.getOrderId()).contains(lower)
                    || (p.getTransactionRef() != null && p.getTransactionRef().toLowerCase().contains(lower)))
                    .collect(java.util.stream.Collectors.toList());
        }
        req.setAttribute("payments", all);
        req.setAttribute("searchQuery", q);
        setStats(req);
        req.setAttribute("activeFilter", "ALL");
        forward(req, res, "payment-list");
    }

    private void filterPayments(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String statusStr = req.getParameter("status");
        List<Payment> all = (statusStr == null || statusStr.equals("ALL"))
                ? paymentDAO.findAllSorted()
                : paymentDAO.findByStatus(PaymentStatus.valueOf(statusStr));
        req.setAttribute("payments", all);
        setStats(req);
        req.setAttribute("activeFilter", statusStr != null ? statusStr : "ALL");
        forward(req, res, "payment-list");
    }

    private void setStats(HttpServletRequest req) {
        req.setAttribute("totalPayments", paymentDAO.count());
        req.setAttribute("paidCount",    paymentDAO.findByStatus(PaymentStatus.PAID).size());
        req.setAttribute("pendingCount", paymentDAO.findByStatus(PaymentStatus.PENDING).size());
        req.setAttribute("totalRevenue", paymentDAO.totalRevenue());
    }

    private Payment build(HttpServletRequest req) {
        Payment p = new Payment();
        p.setOrderId(parseInt(req.getParameter("orderId"), 0));
        p.setCustomerId(parseInt(req.getParameter("customerId"), 0));
        p.setCustomerName(clean(req.getParameter("customerName")));
        p.setAmount(parseDouble(req.getParameter("amount"), 0.0));
        try { p.setMethod(PaymentMethod.valueOf(req.getParameter("method"))); }
        catch (Exception e) { p.setMethod(PaymentMethod.CASH); }
        try { p.setStatus(PaymentStatus.valueOf(req.getParameter("status"))); }
        catch (Exception e) { p.setStatus(PaymentStatus.PENDING); }
        p.setTransactionRef(clean(req.getParameter("transactionRef")));
        return p;
    }

    private Payment findOrRedirect(HttpServletRequest req, HttpServletResponse res) throws IOException {
        int id = parseInt(req.getParameter("id"), 0);
        Payment p = paymentDAO.findById(id);
        if (p == null) { flash(req,"error","Payment #"+id+" not found."); res.sendRedirect(req.getContextPath()+"/payments"); }
        return p;
    }

    private void forward(HttpServletRequest req, HttpServletResponse res, String view)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/" + view + ".jsp").forward(req, res);
    }
    private void flash(HttpServletRequest req, String t, String m) { req.getSession().setAttribute(t+"Msg",m); }
    private int    parseInt(String v, int d)     { try { return Integer.parseInt(v.trim()); }    catch (Exception e) { return d; } }
    private double parseDouble(String v, double d){ try { return Double.parseDouble(v.trim()); } catch (Exception e) { return d; } }
    private String clean(String v) { return v == null ? "" : v.trim(); }
}
