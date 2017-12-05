package org.examples.pbk.playtech.hasher;

import org.mindrot.jbcrypt.BCrypt;

public class BCryptPasswordHasher implements PasswordHasher {

    @Override
    public String hashPassword(String password, String salt) {
        return BCrypt.hashpw(password, salt);
    }

    @Override
    public boolean checkPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }

    @Override
    public String generateSalt() {
        return BCrypt.gensalt();
    }
}
