package com.bakery.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Order — core domain model.
 *
 * OOP: INHERITANCE   — extends BaseEntity
 *      ENCAPSULATION — all fields private
 *      POLYMORPHISM  — overrides getDisplayInfo() and getEntityType()
 */
public class Order extends BaseEntity {

    public enum Status {
        PENDING, CONFIRMED, IN_PROGRESS, READY, COMPLETED, CANCELLED;

        public String getLabel() {
            return switch (this) {
                case PENDING     -> "Pending";
                case CONFIRMED   -> "Confirmed";
                case IN_PROGRESS -> "In Progress";
                case READY       -> "Ready for Pickup";
                case COMPLETED   -> "Completed";
                case CANCELLED   -> "Cancelled";
            };
        }

        public String getBadgeClass() {
            return switch (this) {
                case PENDING     -> "badge-pending";
                case CONFIRMED   -> "badge-confirmed";
                case IN_PROGRESS -> "badge-inprogress";
                case READY       -> "badge-ready";
                case COMPLETED   -> "badge-completed";
                case CANCELLED   -> "badge-cancelled";
            };
        }

        public static Status fromString(String s) {
            try { return Status.valueOf(s.toUpperCase()); }
            catch (Exception e) { return PENDING; }
        }
    }

    private static final String DELIM = "||";
    private static final String DELIM_REGEX = "\\|\\|";

    // Encapsulated fields
    private int    customerId;
    private String customerName;
    private String productName;
    private int    quantity;
    private double unitPrice;
    private double totalAmount;
    private LocalDate pickupDate;
    private Status status;
    private String specialNotes;

    // Constructors
    public Order() {
        super();
        this.status = Status.PENDING;
    }

    public Order(int id, int customerId, String customerName, String productName,
                 int quantity, double unitPrice, LocalDate pickupDate,
                 Status status, String specialNotes,
                 LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(id);
        this.customerId   = customerId;
        this.customerName = customerName;
        this.productName  = productName;
        this.quantity     = quantity;
        this.unitPrice    = unitPrice;
        this.totalAmount  = quantity * unitPrice;
        this.pickupDate   = pickupDate;
        this.status       = status;
        this.specialNotes = specialNotes;
        if (createdAt != null) setCreatedAt(createdAt);
        if (updatedAt != null) setUpdatedAt(updatedAt);
    }

    // ---- BaseEntity abstract method implementations ----

    @Override
    public String getEntityType() { return "Order"; }

    @Override
    public boolean isValid() {
        return customerName != null && !customerName.isBlank()
                && productName != null && !productName.isBlank()
                && quantity > 0
                && unitPrice >= 0
                && pickupDate != null;
    }

    /** Polymorphism — overrides BaseEntity display */
    @Override
    public String getDisplayInfo() {
        return "Order #" + getId() + " | " + customerName + " | " + productName
                + " | Rs." + String.format("%.2f", totalAmount) + " | " + status.getLabel();
    }

    /** Serialize to pipe-delimited file record */
    @Override
    public String toFileRecord() {
        return String.join(DELIM,
                String.valueOf(getId()),
                String.valueOf(customerId),
                escape(customerName),
                escape(productName),
                String.valueOf(quantity),
                String.valueOf(unitPrice),
                String.valueOf(totalAmount),
                pickupDate != null ? pickupDate.toString() : "",
                status.name(),
                escape(specialNotes != null ? specialNotes : ""),
                getCreatedAtFormatted(),
                getUpdatedAtFormatted()
        );
    }

    /** Deserialize from pipe-delimited file record */
    public static Order fromFileRecord(String line) {
        if (line == null || line.isBlank()) return null;
        try {
            String[] p = line.split(DELIM_REGEX, -1);
            if (p.length < 10) return null;

            int         id          = Integer.parseInt(p[0].trim());
            int         custId      = Integer.parseInt(p[1].trim());
            String      custName    = unescape(p[2].trim());
            String      prodName    = unescape(p[3].trim());
            int         qty         = Integer.parseInt(p[4].trim());
            double      price       = Double.parseDouble(p[5].trim());
            double      total       = Double.parseDouble(p[6].trim());
            LocalDate   pickup      = p[7].trim().isEmpty() ? null : LocalDate.parse(p[7].trim());
            Status      status      = Status.fromString(p[8].trim());
            String      notes       = unescape(p[9].trim());
            LocalDateTime created   = p.length > 10 && !p[10].trim().isEmpty()
                    ? LocalDateTime.parse(p[10].trim(), DT_FMT) : LocalDateTime.now();
            LocalDateTime updated   = p.length > 11 && !p[11].trim().isEmpty()
                    ? LocalDateTime.parse(p[11].trim(), DT_FMT) : LocalDateTime.now();

            return new Order(id, custId, custName, prodName, qty, price,
                             pickup, status, notes, created, updated);
        } catch (Exception e) {
            System.err.println("[Order.fromFileRecord] Parse error: " + e.getMessage() + " | Line: " + line);
            return null;
        }
    }

    private static String escape(String s)   { return s == null ? "" : s.replace("||", "//").replace("\n", " "); }
    private static String unescape(String s) { return s == null ? "" : s.replace("//", "||"); }

    // ---- Getters & Setters (Encapsulation) ----
    public int     getCustomerId()                  { return customerId; }
    public void    setCustomerId(int v)             { this.customerId = v; }

    public String  getCustomerName()                { return customerName; }
    public void    setCustomerName(String v)        { this.customerName = v; }

    public String  getProductName()                 { return productName; }
    public void    setProductName(String v)         { this.productName = v; }

    public int     getQuantity()                    { return quantity; }
    public void    setQuantity(int v)               { this.quantity = v; recalcTotal(); }

    public double  getUnitPrice()                   { return unitPrice; }
    public void    setUnitPrice(double v)           { this.unitPrice = v; recalcTotal(); }

    public double  getTotalAmount()                 { return totalAmount; }
    public void    setTotalAmount(double v)         { this.totalAmount = v; }

    public LocalDate  getPickupDate()               { return pickupDate; }
    public void       setPickupDate(LocalDate v)    { this.pickupDate = v; }

    public Status  getStatus()                      { return status; }
    public void    setStatus(Status v)              { this.status = v; }
    public void    setStatusFromString(String s)    { this.status = Status.fromString(s); }

    public String  getSpecialNotes()                { return specialNotes; }
    public void    setSpecialNotes(String v)        { this.specialNotes = v; }

    private void recalcTotal() { this.totalAmount = this.quantity * this.unitPrice; }
}
