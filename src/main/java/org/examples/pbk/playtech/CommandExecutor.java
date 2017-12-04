package org.examples.pbk.playtech;

import org.examples.pbk.playtech.commands.Command;
import org.examples.pbk.playtech.commands.CommandException;

import java.util.HashMap;
import java.util.Map;

public class CommandExecutor {
    private Map<String, Command> commands;

    private CommandExecutor() {
        this.commands = new HashMap<>();
    }

    public static CommandExecutor newInstance() {
        return new CommandExecutor();
    }

    public void addCommand(String commandName, Command command) {
        commands.put(commandName, command);
    }

    public void executeCommand(String commandName) throws CommandException {
        commands.get(commandName).execute();
    }
}
