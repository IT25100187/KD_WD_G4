package com.bakery.servlet;

import com.bakery.dao.AdminDAO;
import com.bakery.model.Admin;
import com.bakery.util.DAOFactory;
import com.bakery.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/admins")
public class AdminServlet extends HttpServlet {

    private AdminDAO adminDAO;

    @Override public void init() {
        adminDAO = DAOFactory.getInstance(getServletContext()).admins();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "list";
        switch (action) {
            case "new"    -> showForm(req, res, null);
            case "edit"   -> showEditForm(req, res);
            case "delete" -> deleteAdmin(req, res);
            case "search" -> searchAdmins(req, res);
            case "logout" -> logout(req, res);
            default       -> listAdmins(req, res);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        switch (action != null ? action : "") {
            case "save"   -> saveAdmin(req, res);
            case "update" -> updateAdmin(req, res);
            default       -> res.sendRedirect(req.getContextPath() + "/admins");
        }
    }

    private void listAdmins(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setAttribute("admins", adminDAO.findAllSorted());
        req.setAttribute("totalAdmins", adminDAO.count());
        forward(req, res, "admin-list");
    }

    private void showForm(HttpServletRequest req, HttpServletResponse res, Admin a)
            throws ServletException, IOException {
        req.setAttribute("admin", a);
        req.setAttribute("roles", new String[]{"ADMIN","SUPER_ADMIN"});
        forward(req, res, "admin-form");
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        Admin a = findOrRedirect(req, res);
        if (a == null) return;
        req.setAttribute("editMode", true);
        showForm(req, res, a);
    }

    private void saveAdmin(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        Admin a = build(req);
        String raw = req.getParameter("password");
        if (raw == null || raw.isBlank()) {
            flash(req, "error", "Password is required.");
            res.sendRedirect(req.getContextPath() + "/admins?action=new");
            return;
        }
        a.setPasswordHash(PasswordUtil.hash(raw));
        boolean ok = adminDAO.save(a);
        flash(req, ok ? "success" : "error",
              ok ? "Admin '" + a.getUsername() + "' created." : "Save failed.");
        res.sendRedirect(req.getContextPath() + "/admins");
    }

    private void updateAdmin(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        Admin a = build(req);
        a.setId(parseInt(req.getParameter("adminId"), 0));
        // Only update password if provided
        String raw = req.getParameter("password");
        if (raw != null && !raw.isBlank()) {
            a.setPasswordHash(PasswordUtil.hash(raw));
        } else {
            Admin existing = adminDAO.findById(a.getId());
            if (existing != null) a.setPasswordHash(existing.getPasswordHash());
        }
        boolean ok = adminDAO.update(a);
        flash(req, ok ? "success" : "error", ok ? "Admin updated." : "Update failed.");
        res.sendRedirect(req.getContextPath() + "/admins");
    }

    private void deleteAdmin(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        int id = parseInt(req.getParameter("id"), 0);
        boolean ok = adminDAO.delete(id);
        flash(req, ok ? "success" : "error",
              ok ? "Admin #" + id + " removed." : "Delete failed.");
        res.sendRedirect(req.getContextPath() + "/admins");
    }

    private void searchAdmins(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String q = req.getParameter("q");
        req.setAttribute("admins", q == null || q.isBlank()
                ? adminDAO.findAllSorted() : adminDAO.search(q.trim()));
        req.setAttribute("searchQuery", q);
        req.setAttribute("totalAdmins", adminDAO.count());
        forward(req, res, "admin-list");
    }

    private void logout(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        req.getSession().invalidate();
        res.sendRedirect(req.getContextPath() + "/login");
    }

    private Admin build(HttpServletRequest req) {
        Admin a = new Admin();
        a.setUsername(clean(req.getParameter("username")));
        a.setFullName(clean(req.getParameter("fullName")));
        a.setEmail(clean(req.getParameter("email")));
        a.setRole(req.getParameter("role") != null ? req.getParameter("role") : "ADMIN");
        return a;
    }

    private Admin findOrRedirect(HttpServletRequest req, HttpServletResponse res) throws IOException {
        int id = parseInt(req.getParameter("id"), 0);
        Admin a = adminDAO.findById(id);
        if (a == null) { flash(req,"error","Admin #"+id+" not found."); res.sendRedirect(req.getContextPath()+"/admins"); }
        return a;
    }

    private void forward(HttpServletRequest req, HttpServletResponse res, String view)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/" + view + ".jsp").forward(req, res);
    }
    private void flash(HttpServletRequest req, String t, String m) { req.getSession().setAttribute(t+"Msg",m); }
    private int parseInt(String v, int d) { try { return Integer.parseInt(v.trim()); } catch (Exception e) { return d; } }
    private String clean(String v) { return v == null ? "" : v.trim(); }
}
