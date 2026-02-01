package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Simple helper for hashing and verifying passwords.
 * Uses SHA-256 plus Base64 encoding. For production systems

 */
public final class PasswordUtil {

    private PasswordUtil() {}

    /**
     * Hash a plain text password using SHA-256 + Base64.
     */
    public static String hash(String plainText) {
        if (plainText == null) {
            return null;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashed);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }

    /**
     * Verify a raw password against a stored hash.
     * Falls back to plain-text comparison to avoid breaking older data.
     */
    public static boolean matches(String rawPassword, String storedHash) {
        if (rawPassword == null || storedHash == null) {
            return false;
        }
        String hashed = hash(rawPassword);
        return storedHash.equals(hashed) || storedHash.equals(rawPassword);
    }
}

