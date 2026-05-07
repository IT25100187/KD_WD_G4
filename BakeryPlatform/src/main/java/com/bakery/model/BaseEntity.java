package com.bakery.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * BaseEntity — Abstract superclass for all domain models.
 *
 * OOP: ABSTRACTION — defines contract via abstract methods
 *      ENCAPSULATION — private fields, public accessors
 *      INHERITANCE   — Order, Customer, Product, Admin, Payment, Review all extend this
 */
public abstract class BaseEntity {

    public static final DateTimeFormatter DT_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private int id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    protected BaseEntity() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    protected BaseEntity(int id) {
        this.id = id;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Abstract methods — every entity must implement
    public abstract String getEntityType();
    public abstract String toFileRecord();
    public abstract boolean isValid();

    // Display method — overridden by subclasses (Polymorphism)
    public String getDisplayInfo() {
        return getEntityType() + " [ID=" + id + "]";
    }

    // Encapsulated getters/setters
    public int getId()                          { return id; }
    public void setId(int id)                  { this.id = id; }

    public LocalDateTime getCreatedAt()         { return createdAt; }
    public void setCreatedAt(LocalDateTime t)  { this.createdAt = t; }

    public LocalDateTime getUpdatedAt()         { return updatedAt; }
    public void setUpdatedAt(LocalDateTime t)  {
        this.updatedAt = t;
    }

    public void markUpdated() {
        this.updatedAt = LocalDateTime.now();
    }

    public String getCreatedAtFormatted() {
        return createdAt != null ? createdAt.format(DT_FMT) : "";
    }

    public String getUpdatedAtFormatted() {
        return updatedAt != null ? updatedAt.format(DT_FMT) : "";
    }
}
