package com.bakery.model;

import java.time.LocalDateTime;
import com.bakery.util.PasswordUtil;

/**
 * Admin — OOP: Inheritance (extends BaseEntity), Encapsulation
 * Demonstrates Information Hiding: password is never returned raw.
 */
public class Admin extends BaseEntity {

    private static final String DELIM       = "||";
    private static final String DELIM_REGEX = "\\|\\|";

    private String username;
    private String passwordHash;   // Information Hiding — stored hashed
    private String fullName;
    private String email;
    private String role;           // SUPER_ADMIN, ADMIN

    public Admin() {
        super();
        this.role = "ADMIN";
    }

    public Admin(int id, String username, String passwordHash, String fullName,
                 String email, String role,
                 LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(id);
        this.username     = username;
        this.passwordHash = passwordHash;
        this.fullName     = fullName;
        this.email        = email;
        this.role         = role;
        if (createdAt != null) setCreatedAt(createdAt);
        if (updatedAt != null) setUpdatedAt(updatedAt);
    }

    @Override public String getEntityType() { return "Admin"; }

    @Override
    public boolean isValid() {
        return username != null && !username.isBlank()
                && passwordHash != null && !passwordHash.isBlank();
    }

    @Override
    public String getDisplayInfo() {
        return "Admin #" + getId() + " | " + username + " | " + fullName + " [" + role + "]";
    }

    @Override
    public String toFileRecord() {
        return String.join(DELIM,
                String.valueOf(getId()),
                esc(username), esc(passwordHash), esc(fullName), esc(email), esc(role),
                getCreatedAtFormatted(),
                getUpdatedAtFormatted()
        );
    }

    public static Admin fromFileRecord(String line) {
        if (line == null || line.isBlank()) return null;
        try {
            String[] p = line.split(DELIM_REGEX, -1);
            if (p.length < 6) return null;
            int           id       = Integer.parseInt(p[0].trim());
            String        user     = p[1].trim();
            String        hash     = p[2].trim();
            String        full     = p[3].trim();
            String        email    = p[4].trim();
            String        role     = p[5].trim();
            LocalDateTime created  = p.length > 6 && !p[6].isBlank()
                    ? LocalDateTime.parse(p[6].trim(), DT_FMT) : LocalDateTime.now();
            LocalDateTime updated  = p.length > 7 && !p[7].isBlank()
                    ? LocalDateTime.parse(p[7].trim(), DT_FMT) : LocalDateTime.now();
            return new Admin(id, user, hash, full, email, role, created, updated);
        } catch (Exception e) {
            System.err.println("[Admin.fromFileRecord] " + e.getMessage());
            return null;
        }
    }

    // Information Hiding — password verification without exposing hash
    public boolean verifyPassword(String rawPassword) {
        String hashed = PasswordUtil.hash(rawPassword);
        return passwordHash != null && passwordHash.equals(hashed);
    }

    private static String esc(String s) { return s == null ? "" : s.replace("||","//").replace("\n"," "); }

    // Getters / Setters
    public String getUsername()                   { return username; }
    public void   setUsername(String v)           { this.username = v; }
    public String getPasswordHash()               { return passwordHash; }
    public void   setPasswordHash(String v)       { this.passwordHash = v; }
    public String getFullName()                   { return fullName; }
    public void   setFullName(String v)           { this.fullName = v; }
    public String getEmail()                      { return email; }
    public void   setEmail(String v)              { this.email = v; }
    public String getRole()                       { return role; }
    public void   setRole(String v)               { this.role = v; }
    public boolean isSuperAdmin()                 { return "SUPER_ADMIN".equalsIgnoreCase(role); }

    public char getInitial() {
        return (fullName != null && !fullName.isEmpty()) ? fullName.charAt(0) :
               (username != null && !username.isEmpty()) ? username.charAt(0) : 'A';
    }
}
