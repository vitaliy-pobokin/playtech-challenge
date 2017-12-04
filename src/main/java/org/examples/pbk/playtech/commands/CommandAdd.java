package org.examples.pbk.playtech.commands;

import org.examples.pbk.playtech.PasswordFile;
import org.examples.pbk.playtech.UserEntry;
import org.examples.pbk.playtech.parser.Args;
import org.mindrot.jbcrypt.BCrypt;

public class CommandAdd implements Command {
    private Args args;
    private PasswordFile passwordFile;

    public CommandAdd(PasswordFile passwordFile, Args args) {
        this.passwordFile = passwordFile;
        this.args = args;
    }

    @Override
    public void execute() throws CommandException {
        UserEntry parsedEntry = args.getUserEntry();
        if (passwordFile.findUserEntry(parsedEntry.getUsername()) == null) {
            String hashedPassword = BCrypt.hashpw(parsedEntry.getPassword(), BCrypt.gensalt());
            UserEntry entryToWrite = new UserEntry(parsedEntry.getUsername(), hashedPassword);
            passwordFile.writeUserEntry(entryToWrite);
        } else {
            throw new CommandException("User already exists.");
        }
    }
}
