package org.examples.pbk.playtech.commands;

import org.examples.pbk.playtech.PasswordFile;
import org.examples.pbk.playtech.UserEntry;
import org.examples.pbk.playtech.parser.Parameter;
import org.examples.pbk.playtech.parser.ParserCommand;
import org.mindrot.jbcrypt.BCrypt;

@ParserCommand(keyword = "add", description = "Add username:password entry to the .password file")
public class CommandAdd implements Command {
    private PasswordFile passwordFile;

    @Parameter(name = "username:password", converterClass = UserEntry.class, converterMethodName = "valueOf")
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
}
