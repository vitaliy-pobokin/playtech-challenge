package org.examples.pbk.playtech.parser;

import org.examples.pbk.playtech.UserEntry;
import org.examples.pbk.playtech.commands.Command;
import java.util.HashMap;
import java.util.Map;

public class Parser {
    private Args parsedArgs;
    private Map<String, Command> possibleCommands;

    public Parser(Args args, Command... commands) {
        this.parsedArgs = args;
        this.possibleCommands = new HashMap<>();
        for (Command command : commands) {
            possibleCommands.put(command.getKeyword(), command);
        }
    }

    public void parse(String[] args) throws ParserException {
        if (args.length != 2) throw new ParserException();
        if (possibleCommands.containsKey(args[0])) {
            UserEntry userEntry = UserEntry.valueOf(args[1]);
            if (userEntry != null) {
                parsedArgs.setCommandKeyword(args[0]);
                parsedArgs.setUserEntry(userEntry);
                return;
            }
        }
        throw new ParserException();
    }

    public Args getParsedArgs() {
        return parsedArgs;
    }

    public void printUsage() {
        System.out.println("Usage printed");
    }
}
