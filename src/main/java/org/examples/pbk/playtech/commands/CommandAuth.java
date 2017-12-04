package org.examples.pbk.playtech.commands;

import org.examples.pbk.playtech.PasswordFile;
import org.examples.pbk.playtech.UserEntry;
import org.examples.pbk.playtech.parser.Parameter;
import org.mindrot.jbcrypt.BCrypt;

public class CommandAuth implements Command {
    private PasswordFile passwordFile;
    private static final String KEYWORD = "auth";

    @Parameter(name = "username:password", converterClass = UserEntry.class)
    private UserEntry userEntry;

    public CommandAuth(PasswordFile passwordFile) {
        this.passwordFile = passwordFile;
    }

    @Override
    public void execute() throws CommandException {
        UserEntry fileUserEntry = passwordFile.findUserEntry(userEntry.getUsername());
        if (fileUserEntry != null) {
            if (checkPassword(userEntry.getPassword(), fileUserEntry.getPassword())) {
                return;
            }
        }
        throw new CommandException("Authentication exception");
    }

    @Override
    public String getKeyword() {
        return KEYWORD;
    }

    private boolean checkPassword(String password, String hashedPassword) {
        if (BCrypt.checkpw(password, hashedPassword)) return true;
        return false;
    }
}
