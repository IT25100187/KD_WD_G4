package com.bakery.model;

import java.time.LocalDateTime;

/**
 * Product — OOP: Inheritance, Encapsulation, Polymorphism
 */
public class Product extends BaseEntity {

    private static final String DELIM       = "||";
    private static final String DELIM_REGEX = "\\|\\|";

    private String  name;
    private String  category;   // CAKE, CUPCAKE, PASTRY, BREAD, CUSTOM
    private String  description;
    private double  price;
    private boolean available;
    private String  imageTag;   // emoji or icon tag for display

    public Product() {
        super();
        this.available = true;
        this.category  = "CAKE";
    }

    public Product(int id, String name, String category, String description,
                   double price, boolean available, String imageTag,
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(id);
        this.name        = name;
        this.category    = category;
        this.description = description;
        this.price       = price;
        this.available   = available;
        this.imageTag    = imageTag;
        if (createdAt != null) setCreatedAt(createdAt);
        if (updatedAt != null) setUpdatedAt(updatedAt);
    }

    @Override public String getEntityType() { return "Product"; }

    @Override
    public boolean isValid() {
        return name != null && !name.isBlank() && price >= 0;
    }

    @Override
    public String getDisplayInfo() {
        return "Product #" + getId() + " | " + name + " | Rs." +
               String.format("%.2f", price) + " [" + category + "]" +
               (available ? " ✓" : " ✗");
    }

    @Override
    public String toFileRecord() {
        return String.join(DELIM,
                String.valueOf(getId()),
                esc(name), esc(category), esc(description),
                String.valueOf(price),
                String.valueOf(available),
                esc(imageTag != null ? imageTag : ""),
                getCreatedAtFormatted(),
                getUpdatedAtFormatted()
        );
    }

    public static Product fromFileRecord(String line) {
        if (line == null || line.isBlank()) return null;
        try {
            String[] p = line.split(DELIM_REGEX, -1);
            if (p.length < 6) return null;
            int           id      = Integer.parseInt(p[0].trim());
            String        name    = p[1].trim();
            String        cat     = p[2].trim();
            String        desc    = p[3].trim();
            double        price   = Double.parseDouble(p[4].trim());
            boolean       avail   = Boolean.parseBoolean(p[5].trim());
            String        tag     = p.length > 6 ? p[6].trim() : "";
            LocalDateTime created = p.length > 7 && !p[7].isBlank()
                    ? LocalDateTime.parse(p[7].trim(), DT_FMT) : LocalDateTime.now();
            LocalDateTime updated = p.length > 8 && !p[8].isBlank()
                    ? LocalDateTime.parse(p[8].trim(), DT_FMT) : LocalDateTime.now();
            return new Product(id, name, cat, desc, price, avail, tag, created, updated);
        } catch (Exception e) {
            System.err.println("[Product.fromFileRecord] " + e.getMessage());
            return null;
        }
    }

    private static String esc(String s) { return s == null ? "" : s.replace("||","//").replace("\n"," "); }

    // Getters / Setters
    public String  getName()                  { return name; }
    public void    setName(String v)          { this.name = v; }
    public String  getCategory()              { return category; }
    public void    setCategory(String v)      { this.category = v; }
    public String  getDescription()           { return description; }
    public void    setDescription(String v)   { this.description = v; }
    public double  getPrice()                 { return price; }
    public void    setPrice(double v)         { this.price = v; }
    public boolean isAvailable()              { return available; }
    public void    setAvailable(boolean v)    { this.available = v; }
    public String  getImageTag()              { return imageTag; }
    public void    setImageTag(String v)      { this.imageTag = v; }

    public String getCategoryIcon() {
        return switch (category != null ? category.toUpperCase() : "") {
            case "CAKE"    -> "🎂";
            case "CUPCAKE" -> "🧁";
            case "PASTRY"  -> "🥐";
            case "BREAD"   -> "🍞";
            case "CUSTOM"  -> "⭐";
            default        -> "🍰";
        };
    }
}
