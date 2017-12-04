package org.examples.pbk.playtech;

import javax.annotation.*;

public class UserEntry {
    private String username;
    private String password;
    public static final String USERNAME_PASSWORD_DELIMITER = ":";

    public UserEntry(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @CheckForNull
    public static UserEntry valueOf(String usernamePwdPair) {
        String[] values = usernamePwdPair.split(USERNAME_PASSWORD_DELIMITER);
        if (values.length == 2) {
            if (values[0].length() > 0 && values[1].length() > 0)
                return new UserEntry(values[0].trim(), values[1].trim());
        }
        return null;
    }

    public String getStringRepresentation() {
        return username + USERNAME_PASSWORD_DELIMITER + password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
