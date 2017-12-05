package org.examples.pbk.playtech.hasher;

public interface PasswordHasher {
    String hashPassword(String password, String salt);
    boolean checkPassword(String password, String hashedPassword);
    String generateSalt();
}
