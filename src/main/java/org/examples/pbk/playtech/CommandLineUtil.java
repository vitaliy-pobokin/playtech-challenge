package org.examples.pbk.playtech;

import org.examples.pbk.playtech.commands.Command;
import org.examples.pbk.playtech.commands.CommandAdd;
import org.examples.pbk.playtech.commands.CommandAuth;
import org.examples.pbk.playtech.commands.CommandException;
import org.examples.pbk.playtech.hasher.BCryptPasswordHasher;
import org.examples.pbk.playtech.hasher.PasswordHasher;
import org.examples.pbk.playtech.parser.Parser;
import org.examples.pbk.playtech.parser.ParserException;

public class CommandLineUtil {

    public static void main(String[] args) {
        String homeDirectory = System.getProperty("user.home");
        PasswordFile file = new PasswordFile(homeDirectory);
        PasswordHasher hasher = new BCryptPasswordHasher();

        CommandAdd add = new CommandAdd(file, hasher);
        CommandAuth auth = new CommandAuth(file, hasher);
        Parser parser = new Parser(add, auth);
        try {
            parser.parse(args);
        } catch (ParserException e) {
            System.err.println(e.getMessage());
            System.err.print(parser.getUsageInfo());
            System.exit(-1);
        }
        try {
            Command command = (Command) parser.getParsedCommand();
            command.execute();
        } catch (CommandException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }

    }
}
