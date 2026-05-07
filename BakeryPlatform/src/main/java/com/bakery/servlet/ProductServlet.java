package com.bakery.servlet;

import com.bakery.dao.ProductDAO;
import com.bakery.model.Product;
import com.bakery.util.DAOFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {

    private ProductDAO productDAO;

    @Override public void init() {
        productDAO = DAOFactory.getInstance(getServletContext()).products();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "list";
        switch (action) {
            case "new"    -> showForm(req, res, null);
            case "edit"   -> showEditForm(req, res);
            case "view"   -> viewProduct(req, res);
            case "delete" -> deleteProduct(req, res);
            case "search" -> searchProducts(req, res);
            case "filter" -> filterProducts(req, res);
            default       -> listProducts(req, res);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("save".equals(action))        saveProduct(req, res);
        else if ("update".equals(action)) updateProduct(req, res);
        else                              res.sendRedirect(req.getContextPath() + "/products");
    }

    private void listProducts(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setAttribute("products", productDAO.findAllSorted());
        req.setAttribute("totalProducts", productDAO.count());
        req.setAttribute("availableCount", productDAO.findAvailable().size());
        req.setAttribute("activeFilter", "ALL");
        forward(req, res, "product-list");
    }

    private void showForm(HttpServletRequest req, HttpServletResponse res, Product p)
            throws ServletException, IOException {
        req.setAttribute("product", p);
        req.setAttribute("categories", new String[]{"CAKE","CUPCAKE","PASTRY","BREAD","CUSTOM"});
        forward(req, res, "product-form");
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        Product p = findOrRedirect(req, res);
        if (p == null) return;
        req.setAttribute("editMode", true);
        showForm(req, res, p);
    }

    private void viewProduct(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        Product p = findOrRedirect(req, res);
        if (p == null) return;
        req.setAttribute("product", p);
        req.setAttribute("reviews", DAOFactory.getInstance(getServletContext()).reviews().findByProduct(p.getId()));
        forward(req, res, "product-detail");
    }

    private void saveProduct(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        Product p = build(req);
        boolean ok = productDAO.save(p);
        flash(req, ok ? "success":"error", ok ? "Product '"+p.getName()+"' added!" : "Save failed.");
        res.sendRedirect(req.getContextPath() + "/products");
    }

    private void updateProduct(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        Product p = build(req);
        p.setId(parseInt(req.getParameter("productId"), 0));
        boolean ok = productDAO.update(p);
        flash(req, ok ? "success":"error", ok ? "Product updated successfully." : "Update failed.");
        res.sendRedirect(req.getContextPath() + "/products");
    }

    private void deleteProduct(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        int id = parseInt(req.getParameter("id"), 0);
        boolean ok = productDAO.delete(id);
        flash(req, ok?"success":"error", ok?"Product #"+id+" removed.":"Delete failed.");
        res.sendRedirect(req.getContextPath() + "/products");
    }

    private void searchProducts(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String q = req.getParameter("q");
        List<Product> results = q == null || q.isBlank()
                ? productDAO.findAllSorted() : productDAO.search(q.trim());
        req.setAttribute("products", results);
        req.setAttribute("searchQuery", q);
        req.setAttribute("totalProducts", productDAO.count());
        req.setAttribute("availableCount", productDAO.findAvailable().size());
        req.setAttribute("activeFilter", "ALL");
        forward(req, res, "product-list");
    }

    private void filterProducts(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String cat = req.getParameter("category");
        List<Product> results = (cat == null || cat.equals("ALL"))
                ? productDAO.findAllSorted() : productDAO.findByCategory(cat);
        req.setAttribute("products", results);
        req.setAttribute("totalProducts", productDAO.count());
        req.setAttribute("availableCount", productDAO.findAvailable().size());
        req.setAttribute("activeFilter", cat != null ? cat : "ALL");
        forward(req, res, "product-list");
    }

    private Product build(HttpServletRequest req) {
        Product p = new Product();
        p.setName(clean(req.getParameter("name")));
        p.setCategory(req.getParameter("category") != null ? req.getParameter("category") : "CAKE");
        p.setDescription(clean(req.getParameter("description")));
        p.setPrice(parseDouble(req.getParameter("price"), 0.0));
        p.setAvailable("on".equals(req.getParameter("available")) || "true".equals(req.getParameter("available")));
        p.setImageTag(clean(req.getParameter("imageTag")));
        return p;
    }

    private Product findOrRedirect(HttpServletRequest req, HttpServletResponse res) throws IOException {
        int id = parseInt(req.getParameter("id"), 0);
        Product p = productDAO.findById(id);
        if (p == null) { flash(req,"error","Product #"+id+" not found."); res.sendRedirect(req.getContextPath()+"/products"); }
        return p;
    }

    private void forward(HttpServletRequest req, HttpServletResponse res, String view)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/" + view + ".jsp").forward(req, res);
    }
    private void flash(HttpServletRequest req, String t, String m) { req.getSession().setAttribute(t+"Msg", m); }
    private int    parseInt(String v, int d)    { try { return Integer.parseInt(v.trim()); }    catch (Exception e) { return d; } }
    private double parseDouble(String v, double d){ try { return Double.parseDouble(v.trim()); } catch (Exception e) { return d; } }
    private String clean(String v) { return v == null ? "" : v.trim(); }
}
