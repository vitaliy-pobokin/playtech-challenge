package org.examples.pbk.playtech.commands;

public interface Command {
    void execute() throws CommandException;
    String getKeyword();
}
