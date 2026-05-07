package com.bakery.model;

import java.time.LocalDateTime;

/**
 * Payment — OOP: Inheritance (extends BaseEntity), Encapsulation, Polymorphism
 */
public class Payment extends BaseEntity {

    public enum PaymentStatus { PENDING, PAID, FAILED, REFUNDED }
    public enum PaymentMethod { CASH, CARD, BANK_TRANSFER, ONLINE }

    private static final String DELIM       = "||";
    private static final String DELIM_REGEX = "\\|\\|";

    private int           orderId;
    private int           customerId;
    private String        customerName;
    private double        amount;
    private PaymentMethod method;
    private PaymentStatus status;
    private String        transactionRef;

    public Payment() {
        super();
        this.status = PaymentStatus.PENDING;
        this.method = PaymentMethod.CASH;
    }

    public Payment(int id, int orderId, int customerId, String customerName,
                   double amount, PaymentMethod method, PaymentStatus status,
                   String transactionRef,
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(id);
        this.orderId        = orderId;
        this.customerId     = customerId;
        this.customerName   = customerName;
        this.amount         = amount;
        this.method         = method;
        this.status         = status;
        this.transactionRef = transactionRef;
        if (createdAt != null) setCreatedAt(createdAt);
        if (updatedAt != null) setUpdatedAt(updatedAt);
    }

    @Override public String getEntityType() { return "Payment"; }

    @Override
    public boolean isValid() {
        return orderId > 0 && amount >= 0;
    }

    @Override
    public String getDisplayInfo() {
        return "Payment #" + getId() + " | Order #" + orderId + " | " +
               customerName + " | Rs." + String.format("%.2f", amount) +
               " | " + method + " | " + status;
    }

    @Override
    public String toFileRecord() {
        return String.join(DELIM,
                String.valueOf(getId()),
                String.valueOf(orderId),
                String.valueOf(customerId),
                esc(customerName),
                String.valueOf(amount),
                method.name(),
                status.name(),
                esc(transactionRef != null ? transactionRef : ""),
                getCreatedAtFormatted(),
                getUpdatedAtFormatted()
        );
    }

    public static Payment fromFileRecord(String line) {
        if (line == null || line.isBlank()) return null;
        try {
            String[] p = line.split(DELIM_REGEX, -1);
            if (p.length < 8) return null;
            int           id      = Integer.parseInt(p[0].trim());
            int           ordId   = Integer.parseInt(p[1].trim());
            int           custId  = Integer.parseInt(p[2].trim());
            String        cName   = p[3].trim();
            double        amt     = Double.parseDouble(p[4].trim());
            PaymentMethod method  = PaymentMethod.valueOf(p[5].trim());
            PaymentStatus status  = PaymentStatus.valueOf(p[6].trim());
            String        ref     = p[7].trim();
            LocalDateTime created = p.length > 8 && !p[8].isBlank()
                    ? LocalDateTime.parse(p[8].trim(), DT_FMT) : LocalDateTime.now();
            LocalDateTime updated = p.length > 9 && !p[9].isBlank()
                    ? LocalDateTime.parse(p[9].trim(), DT_FMT) : LocalDateTime.now();
            return new Payment(id, ordId, custId, cName, amt, method, status, ref, created, updated);
        } catch (Exception e) {
            System.err.println("[Payment.fromFileRecord] " + e.getMessage());
            return null;
        }
    }

    private static String esc(String s) { return s == null ? "" : s.replace("||","//").replace("\n"," "); }

    // Getters / Setters
    public int           getOrderId()                { return orderId; }
    public void          setOrderId(int v)           { this.orderId = v; }
    public int           getCustomerId()             { return customerId; }
    public void          setCustomerId(int v)        { this.customerId = v; }
    public String        getCustomerName()           { return customerName; }
    public void          setCustomerName(String v)   { this.customerName = v; }
    public double        getAmount()                 { return amount; }
    public void          setAmount(double v)         { this.amount = v; }
    public PaymentMethod getMethod()                 { return method; }
    public void          setMethod(PaymentMethod v)  { this.method = v; }
    public PaymentStatus getStatus()                 { return status; }
    public void          setStatus(PaymentStatus v)  { this.status = v; }
    public String        getTransactionRef()         { return transactionRef; }
    public void          setTransactionRef(String v) { this.transactionRef = v; }

    public String getStatusBadgeClass() {
        return switch (status) {
            case PENDING  -> "badge-pending";
            case PAID     -> "badge-completed";
            case FAILED   -> "badge-cancelled";
            case REFUNDED -> "badge-inprogress";
        };
    }
}
