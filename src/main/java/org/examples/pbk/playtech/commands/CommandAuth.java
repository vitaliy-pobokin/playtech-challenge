package org.examples.pbk.playtech.commands;

import org.examples.pbk.playtech.PasswordFile;
import org.examples.pbk.playtech.UserEntry;
import org.examples.pbk.playtech.parser.Args;
import org.mindrot.jbcrypt.BCrypt;

public class CommandAuth implements Command {
    private Args args;
    private PasswordFile passwordFile;
    private static final String KEYWORD = "auth";

    public CommandAuth(PasswordFile passwordFile, Args args) {
        this.passwordFile = passwordFile;
        this.args = args;
    }

    @Override
    public void execute() throws CommandException {
        UserEntry userEntry = passwordFile.findUserEntry(args.getUserEntry().getUsername());
        if (userEntry != null) {
            if (checkPassword(args.getUserEntry().getPassword(), userEntry.getPassword())) {
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
