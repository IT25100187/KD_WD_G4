package com.bakery.model;

import java.time.LocalDateTime;

/**
 * Customer — OOP: Inheritance (extends BaseEntity), Encapsulation, Polymorphism
 */
public class Customer extends BaseEntity {

    private static final String DELIM       = "||";
    private static final String DELIM_REGEX = "\\|\\|";

    private String name;
    private String email;
    private String phone;
    private String address;
    private String membershipType; // REGULAR, PREMIUM

    public Customer() {
        super();
        this.membershipType = "REGULAR";
    }

    public Customer(int id, String name, String email, String phone,
                    String address, String membershipType,
                    LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(id);
        this.name           = name;
        this.email          = email;
        this.phone          = phone;
        this.address        = address;
        this.membershipType = membershipType;
        if (createdAt != null) setCreatedAt(createdAt);
        if (updatedAt != null) setUpdatedAt(updatedAt);
    }

    @Override public String getEntityType() { return "Customer"; }

    @Override
    public boolean isValid() {
        return name != null && !name.isBlank()
                && phone != null && !phone.isBlank();
    }

    @Override
    public String getDisplayInfo() {
        return "Customer #" + getId() + " | " + name + " | " + phone + " [" + membershipType + "]";
    }

    @Override
    public String toFileRecord() {
        return String.join(DELIM,
                String.valueOf(getId()),
                esc(name), esc(email), esc(phone), esc(address),
                esc(membershipType),
                getCreatedAtFormatted(),
                getUpdatedAtFormatted()
        );
    }

    public static Customer fromFileRecord(String line) {
        if (line == null || line.isBlank()) return null;
        try {
            String[] p = line.split(DELIM_REGEX, -1);
            if (p.length < 6) return null;
            int           id      = Integer.parseInt(p[0].trim());
            String        name    = p[1].trim();
            String        email   = p[2].trim();
            String        phone   = p[3].trim();
            String        addr    = p[4].trim();
            String        mem     = p[5].trim();
            LocalDateTime created = p.length > 6 && !p[6].isBlank()
                    ? LocalDateTime.parse(p[6].trim(), DT_FMT) : LocalDateTime.now();
            LocalDateTime updated = p.length > 7 && !p[7].isBlank()
                    ? LocalDateTime.parse(p[7].trim(), DT_FMT) : LocalDateTime.now();
            return new Customer(id, name, email, phone, addr, mem, created, updated);
        } catch (Exception e) {
            System.err.println("[Customer.fromFileRecord] " + e.getMessage());
            return null;
        }
    }

    private static String esc(String s) { return s == null ? "" : s.replace("||","//").replace("\n"," "); }

    // Getters / Setters
    public String getName()                     { return name; }
    public void   setName(String v)             { this.name = v; }
    public String getEmail()                    { return email; }
    public void   setEmail(String v)            { this.email = v; }
    public String getPhone()                    { return phone; }
    public void   setPhone(String v)            { this.phone = v; }
    public String getAddress()                  { return address; }
    public void   setAddress(String v)          { this.address = v; }
    public String getMembershipType()           { return membershipType; }
    public void   setMembershipType(String v)   { this.membershipType = v; }

    public char getInitial() {
        return (name != null && !name.isEmpty()) ? name.charAt(0) : '?';
    }
}
