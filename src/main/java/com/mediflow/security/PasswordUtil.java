package com.mediflow.security;

import org.mindrot.jbcrypt.BCrypt;

public final class PasswordUtil {
    private PasswordUtil() {}
    public static String hash(String raw) { return BCrypt.hashpw(raw, BCrypt.gensalt(12)); }
    public static boolean matches(String raw, String hash) {
        try { return BCrypt.checkpw(raw, hash); } catch (RuntimeException ex) { return false; }
    }
}
