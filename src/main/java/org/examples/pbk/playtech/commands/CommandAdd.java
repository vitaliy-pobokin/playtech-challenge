package org.examples.pbk.playtech.commands;

import org.examples.pbk.playtech.PasswordFile;
import org.examples.pbk.playtech.UserEntry;
import org.examples.pbk.playtech.parser.Parameter;
import org.mindrot.jbcrypt.BCrypt;

public class CommandAdd implements Command {
    private PasswordFile passwordFile;
    private static final String KEYWORD = "add";

    @Parameter(name = "username:password", converterClass = UserEntry.class)
    private UserEntry userEntry;

    public CommandAdd(PasswordFile passwordFile) {
        this.passwordFile = passwordFile;
    }

    @Override
    public void execute() throws CommandException {
        //UserEntry parsedEntry = args.getUserEntry();
        if (passwordFile.findUserEntry(userEntry.getUsername()) == null) {
            String hashedPassword = BCrypt.hashpw(userEntry.getPassword(), BCrypt.gensalt());
            UserEntry entryToWrite = new UserEntry(userEntry.getUsername(), hashedPassword);
            passwordFile.writeUserEntry(entryToWrite);
        } else {
            throw new CommandException("User already exists.");
        }
    }

    @Override
    public String getKeyword() {
        return KEYWORD;
    }
}
