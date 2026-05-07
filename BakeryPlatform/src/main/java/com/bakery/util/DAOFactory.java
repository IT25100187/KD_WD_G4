package com.bakery.util;

import com.bakery.dao.*;

import jakarta.servlet.ServletContext;

/**
 * DAOFactory — Singleton factory providing shared DAO instances.
 * OOP: Encapsulation (private constructor), Abstraction (hides DAO wiring)
 */
public class DAOFactory {

    private static DAOFactory instance;
    private final String dataDir;

    private final OrderDAO    orderDAO;
    private final CustomerDAO customerDAO;
    private final ProductDAO  productDAO;
    private final AdminDAO    adminDAO;
    private final ReviewDAO   reviewDAO;
    private final PaymentDAO  paymentDAO;

    private DAOFactory(String dataDir) {
        this.dataDir     = dataDir;
        this.orderDAO    = new OrderDAO(dataDir);
        this.customerDAO = new CustomerDAO(dataDir);
        this.productDAO  = new ProductDAO(dataDir);
        this.adminDAO    = new AdminDAO(dataDir);
        this.reviewDAO   = new ReviewDAO(dataDir);
        this.paymentDAO  = new PaymentDAO(dataDir);
    }

    public static synchronized DAOFactory getInstance(String dataDir) {
        if (instance == null) instance = new DAOFactory(dataDir);
        return instance;
    }

    public static DAOFactory getInstance(ServletContext ctx) {
        String dataDir = (String) ctx.getAttribute("dataDir");
        return getInstance(dataDir);
    }

    // Getters
    public OrderDAO    orders()    { return orderDAO; }
    public CustomerDAO customers() { return customerDAO; }
    public ProductDAO  products()  { return productDAO; }
    public AdminDAO    admins()    { return adminDAO; }
    public ReviewDAO   reviews()   { return reviewDAO; }
    public PaymentDAO  payments()  { return paymentDAO; }
    public String      getDataDir(){ return dataDir; }
}
