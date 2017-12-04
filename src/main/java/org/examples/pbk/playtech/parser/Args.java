package org.examples.pbk.playtech.parser;

import org.examples.pbk.playtech.UserEntry;

public class Args {
    private String commandKeyword;
    private UserEntry userEntry;

    public Args(){}

    public String getCommandKeyword() {
        return commandKeyword;
    }

    public void setCommandKeyword(String commandKeyword) {
        this.commandKeyword = commandKeyword;
    }

    public UserEntry getUserEntry() {
        return userEntry;
    }

    public void setUserEntry(UserEntry userEntry) {
        this.userEntry = userEntry;
    }
}
