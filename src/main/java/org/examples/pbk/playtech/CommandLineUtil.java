package org.examples.pbk.playtech;

import org.examples.pbk.playtech.commands.CommandAdd;
import org.examples.pbk.playtech.commands.CommandAuth;
import org.examples.pbk.playtech.commands.CommandException;
import org.examples.pbk.playtech.parser.Args;
import org.examples.pbk.playtech.parser.Parser;
import org.examples.pbk.playtech.parser.ParserException;

public class CommandLineUtil {

    public static void main(String[] args) {
        String homeDirectory = System.getProperty("user.home");
        PasswordFile file = new PasswordFile(homeDirectory);

        Args arguments = new Args();
        CommandAdd add = new CommandAdd(file, arguments);
        CommandAuth auth = new CommandAuth(file, arguments);
        Parser parser = new Parser(arguments, add, auth);
        CommandExecutor executor = CommandExecutor.newInstance();
        executor.addCommand("add", add);
        executor.addCommand("auth", auth);
        try {
            parser.parse(args);
        } catch (ParserException e) {
            parser.printUsage();
            System.exit(-1);
        }
        try {
            executor.executeCommand(parser.getParsedArgs().getCommandKeyword());
        } catch (CommandException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }

    }
}
