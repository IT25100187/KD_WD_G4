package com.bakery.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * PasswordUtil — Information Hiding: raw passwords never stored.
 * Uses SHA-256 hashing for admin passwords.
 */
public class PasswordUtil {

    private PasswordUtil() {} // Utility class — no instantiation

    public static String hash(String raw) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(raw.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    public static boolean verify(String raw, String hash) {
        return hash(raw).equals(hash);
    }
}
