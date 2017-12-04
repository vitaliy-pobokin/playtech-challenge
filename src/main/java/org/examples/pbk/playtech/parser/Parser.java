package org.examples.pbk.playtech.parser;

import org.examples.pbk.playtech.UserEntry;

public class Parser {
    private Args parsedArgs;

    public Parser(Args args) {
        this.parsedArgs = args;
    }

    public void parse(String[] args) throws ParserException {
        if (args.length != 2) throw new ParserException();
        if (args[0].equals("add") || args[0].equals("auth")) {
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
