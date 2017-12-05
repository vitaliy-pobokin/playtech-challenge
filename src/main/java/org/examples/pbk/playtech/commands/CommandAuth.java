package org.examples.pbk.playtech.commands;

import org.examples.pbk.playtech.PasswordFile;
import org.examples.pbk.playtech.UserEntry;
import org.examples.pbk.playtech.parser.Parameter;
import org.examples.pbk.playtech.parser.ParserCommand;
import org.mindrot.jbcrypt.BCrypt;

@ParserCommand(keyword = "auth", description = "Authenticate with username:password entry stored in the .password file")
public class CommandAuth implements Command {
    private PasswordFile passwordFile;

    @Parameter(name = "username:password", converterClass = UserEntry.class, converterMethodName = "valueOf")
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

    private boolean checkPassword(String password, String hashedPassword) {
        if (BCrypt.checkpw(password, hashedPassword)) return true;
        return false;
    }
}
