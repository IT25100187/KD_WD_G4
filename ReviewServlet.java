package com.bakery.servlet;

import com.bakery.dao.ReviewDAO;
import com.bakery.model.Review;
import com.bakery.util.DAOFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/reviews")
public class ReviewServlet extends HttpServlet {

    private ReviewDAO reviewDAO;

    @Override public void init() {
        reviewDAO = DAOFactory.getInstance(getServletContext()).reviews();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "list";
        switch (action) {
            case "new"     -> showForm(req, res, null);
            case "edit"    -> showEditForm(req, res);
            case "delete"  -> deleteReview(req, res);
            case "approve" -> approveReview(req, res);
            case "search"  -> searchReviews(req, res);
            default        -> listReviews(req, res);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("save".equals(action))        saveReview(req, res);
        else if ("update".equals(action)) updateReview(req, res);
        else                              res.sendRedirect(req.getContextPath() + "/reviews");
    }

    private void listReviews(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setAttribute("reviews", reviewDAO.findAllSorted());
        req.setAttribute("totalReviews", reviewDAO.count());
        req.setAttribute("pendingCount", reviewDAO.findPending().size());
        req.setAttribute("approvedCount", reviewDAO.findApproved().size());
        req.setAttribute("activeFilter", "ALL");
        forward(req, res, "review-list");
    }

    private void showForm(HttpServletRequest req, HttpServletResponse res, Review r)
            throws ServletException, IOException {
        req.setAttribute("review", r);
        req.setAttribute("customers", DAOFactory.getInstance(getServletContext()).customers().findAllSorted());
        req.setAttribute("products", DAOFactory.getInstance(getServletContext()).products().findAvailable());
        forward(req, res, "review-form");
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        Review r = findOrRedirect(req, res);
        if (r == null) return;
        req.setAttribute("editMode", true);
        showForm(req, res, r);
    }

    private void saveReview(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        Review r = build(req);
        boolean ok = reviewDAO.save(r);
        flash(req, ok?"success":"error", ok?"Review submitted successfully!":"Save failed.");
        res.sendRedirect(req.getContextPath() + "/reviews");
    }

    private void updateReview(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        Review r = build(req);
        r.setId(parseInt(req.getParameter("reviewId"), 0));
        boolean ok = reviewDAO.update(r);
        flash(req, ok?"success":"error", ok?"Review updated.":"Update failed.");
        res.sendRedirect(req.getContextPath() + "/reviews");
    }

    private void deleteReview(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        int id = parseInt(req.getParameter("id"), 0);
        boolean ok = reviewDAO.delete(id);
        flash(req, ok?"success":"error", ok?"Review #"+id+" removed.":"Delete failed.");
        res.sendRedirect(req.getContextPath() + "/reviews");
    }

    private void approveReview(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        int id = parseInt(req.getParameter("id"), 0);
        boolean ok = reviewDAO.approve(id);
        flash(req, ok?"success":"error", ok?"Review #"+id+" approved.":"Approval failed.");
        res.sendRedirect(req.getContextPath() + "/reviews");
    }

    private void searchReviews(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String q = req.getParameter("q");
        List<Review> all = reviewDAO.findAllSorted();
        if (q != null && !q.isBlank()) {
            String lower = q.toLowerCase();
            all = all.stream().filter(r ->
                    r.getCustomerName().toLowerCase().contains(lower)
                    || r.getProductName().toLowerCase().contains(lower))
                    .collect(java.util.stream.Collectors.toList());
        }
        req.setAttribute("reviews", all);
        req.setAttribute("searchQuery", q);
        req.setAttribute("totalReviews", reviewDAO.count());
        req.setAttribute("pendingCount", reviewDAO.findPending().size());
        req.setAttribute("approvedCount", reviewDAO.findApproved().size());
        req.setAttribute("activeFilter", "ALL");
        forward(req, res, "review-list");
    }

    private Review build(HttpServletRequest req) {
        Review r = new Review();
        r.setCustomerId(parseInt(req.getParameter("customerId"), 0));
        r.setCustomerName(clean(req.getParameter("customerName")));
        r.setProductId(parseInt(req.getParameter("productId"), 0));
        r.setProductName(clean(req.getParameter("productName")));
        r.setRating(parseInt(req.getParameter("rating"), 5));
        r.setComment(clean(req.getParameter("comment")));
        r.setApproved(false);
        return r;
    }

    private Review findOrRedirect(HttpServletRequest req, HttpServletResponse res) throws IOException {
        int id = parseInt(req.getParameter("id"), 0);
        Review r = reviewDAO.findById(id);
        if (r == null) { flash(req,"error","Review #"+id+" not found."); res.sendRedirect(req.getContextPath()+"/reviews"); }
        return r;
    }

    private void forward(HttpServletRequest req, HttpServletResponse res, String view)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/" + view + ".jsp").forward(req, res);
    }
    private void flash(HttpServletRequest req, String t, String m) { req.getSession().setAttribute(t+"Msg",m); }
    private int parseInt(String v, int d) { try { return Integer.parseInt(v.trim()); } catch (Exception e) { return d; } }
    private String clean(String v) { return v == null ? "" : v.trim(); }
}
