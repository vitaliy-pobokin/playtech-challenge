package org.examples.pbk.playtech.commands;

import org.examples.pbk.playtech.PasswordFile;
import org.examples.pbk.playtech.UserEntry;
import org.examples.pbk.playtech.hasher.PasswordHasher;
import org.examples.pbk.playtech.parser.Parameter;
import org.examples.pbk.playtech.parser.ParserCommand;

@ParserCommand(keyword = "auth", description = "Authenticate with username:password entry stored in the .password file")
public class CommandAuth implements Command {
    private PasswordFile passwordFile;
    private PasswordHasher passwordHasher;

    @Parameter(name = "username:password", converterClass = UserEntry.class, converterMethodName = "valueOf")
    private UserEntry userEntry;

    public CommandAuth(PasswordFile passwordFile, PasswordHasher passwordHasher) {
        this.passwordFile = passwordFile;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public void execute() throws CommandException {
        UserEntry fileUserEntry = passwordFile.findUserEntry(userEntry.getUsername());
        if (fileUserEntry != null) {
            if (passwordHasher.checkPassword(userEntry.getPassword(), fileUserEntry.getPassword())) {
                return;
            }
        }
        throw new CommandException("Authentication exception");
    }
}
