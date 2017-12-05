package org.examples.pbk.playtech.commands;

import org.examples.pbk.playtech.PasswordFile;
import org.examples.pbk.playtech.UserEntry;
import org.examples.pbk.playtech.hasher.PasswordHasher;
import org.examples.pbk.playtech.parser.Parameter;
import org.examples.pbk.playtech.parser.ParserCommand;

@ParserCommand(keyword = "add", description = "Add username:password entry to the .password file")
public class CommandAdd implements Command {
    private PasswordFile passwordFile;
    private PasswordHasher passwordHasher;

    @Parameter(name = "username:password", converterClass = UserEntry.class, converterMethodName = "valueOf")
    private UserEntry userEntry;

    public CommandAdd(PasswordFile passwordFile, PasswordHasher passwordHasher) {
        this.passwordFile = passwordFile;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public void execute() throws CommandException {
        if (passwordFile.findUserEntry(userEntry.getUsername()) == null) {
            String hashedPassword = passwordHasher.hashPassword(userEntry.getPassword(), passwordHasher.generateSalt());
            UserEntry entryToWrite = new UserEntry(userEntry.getUsername(), hashedPassword);
            passwordFile.writeUserEntry(entryToWrite);
        } else {
            throw new CommandException("User already exists.");
        }
    }
}
