package org.examples.pbk.playtech;

import org.examples.pbk.playtech.commands.CommandAdd;
import org.examples.pbk.playtech.commands.CommandAuth;
import org.examples.pbk.playtech.commands.CommandException;
import org.examples.pbk.playtech.parser.Parser;
import org.examples.pbk.playtech.parser.ParserException;

public class CommandLineUtil {

    public static void main(String[] args) {
        String homeDirectory = System.getProperty("user.home");
        PasswordFile file = new PasswordFile(homeDirectory);

        CommandAdd add = new CommandAdd(file);
        CommandAuth auth = new CommandAuth(file);
        Parser parser = new Parser(add, auth);
        try {
            parser.parse(args);
        } catch (ParserException e) {
            e.printStackTrace();
            parser.printUsage();
            System.exit(-1);
        }
        try {
            parser.getParsedCommands().get(0).execute();
        } catch (CommandException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }

    }
}
