package com.bakery.model;

import java.time.LocalDateTime;

/**
 * Review — OOP: Inheritance (extends BaseEntity), Encapsulation, Polymorphism
 */
public class Review extends BaseEntity {

    private static final String DELIM       = "||";
    private static final String DELIM_REGEX = "\\|\\|";

    private int    customerId;
    private String customerName;
    private int    productId;
    private String productName;
    private int    rating;       // 1–5
    private String comment;
    private boolean approved;   // Admin moderation

    public Review() {
        super();
        this.rating   = 5;
        this.approved = false;
    }

    public Review(int id, int customerId, String customerName, int productId,
                  String productName, int rating, String comment, boolean approved,
                  LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(id);
        this.customerId   = customerId;
        this.customerName = customerName;
        this.productId    = productId;
        this.productName  = productName;
        this.rating       = Math.max(1, Math.min(5, rating));
        this.comment      = comment;
        this.approved     = approved;
        if (createdAt != null) setCreatedAt(createdAt);
        if (updatedAt != null) setUpdatedAt(updatedAt);
    }

    @Override public String getEntityType() { return "Review"; }

    @Override
    public boolean isValid() {
        return customerId > 0 && rating >= 1 && rating <= 5
                && comment != null && !comment.isBlank();
    }

    @Override
    public String getDisplayInfo() {
        return "Review #" + getId() + " | " + customerName + " | "
                + productName + " | " + rating + "★ | "
                + (approved ? "Approved" : "Pending");
    }

    @Override
    public String toFileRecord() {
        return String.join(DELIM,
                String.valueOf(getId()),
                String.valueOf(customerId),
                esc(customerName),
                String.valueOf(productId),
                esc(productName),
                String.valueOf(rating),
                esc(comment),
                String.valueOf(approved),
                getCreatedAtFormatted(),
                getUpdatedAtFormatted()
        );
    }

    public static Review fromFileRecord(String line) {
        if (line == null || line.isBlank()) return null;
        try {
            String[] p = line.split(DELIM_REGEX, -1);
            if (p.length < 8) return null;
            int           id      = Integer.parseInt(p[0].trim());
            int           custId  = Integer.parseInt(p[1].trim());
            String        cName   = p[2].trim();
            int           prodId  = Integer.parseInt(p[3].trim());
            String        pName   = p[4].trim();
            int           rating  = Integer.parseInt(p[5].trim());
            String        comment = p[6].trim();
            boolean       approv  = Boolean.parseBoolean(p[7].trim());
            LocalDateTime created = p.length > 8 && !p[8].isBlank()
                    ? LocalDateTime.parse(p[8].trim(), DT_FMT) : LocalDateTime.now();
            LocalDateTime updated = p.length > 9 && !p[9].isBlank()
                    ? LocalDateTime.parse(p[9].trim(), DT_FMT) : LocalDateTime.now();
            return new Review(id, custId, cName, prodId, pName, rating, comment, approv, created, updated);
        } catch (Exception e) {
            System.err.println("[Review.fromFileRecord] " + e.getMessage());
            return null;
        }
    }

    public String getStars() {
        return "★".repeat(rating) + "☆".repeat(5 - rating);
    }

    private static String esc(String s) { return s == null ? "" : s.replace("||","//").replace("\n"," "); }

    // Getters / Setters
    public int     getCustomerId()              { return customerId; }
    public void    setCustomerId(int v)         { this.customerId = v; }
    public String  getCustomerName()            { return customerName; }
    public void    setCustomerName(String v)    { this.customerName = v; }
    public int     getProductId()               { return productId; }
    public void    setProductId(int v)          { this.productId = v; }
    public String  getProductName()             { return productName; }
    public void    setProductName(String v)     { this.productName = v; }
    public int     getRating()                  { return rating; }
    public void    setRating(int v)             { this.rating = Math.max(1, Math.min(5, v)); }
    public String  getComment()                 { return comment; }
    public void    setComment(String v)         { this.comment = v; }
    public boolean isApproved()                 { return approved; }
    public void    setApproved(boolean v)       { this.approved = v; }
}
