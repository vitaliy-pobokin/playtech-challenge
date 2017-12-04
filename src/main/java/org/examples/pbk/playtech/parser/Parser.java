package org.examples.pbk.playtech.parser;

import org.examples.pbk.playtech.commands.Command;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO: possible null value in parameter field???
// TODO: Converter method name
// TODO: Command annotation
// TODO: print usage from annotations
public class Parser {
    private Map<String, Command> possibleCommands;
    private List<Command> parsedCommands;

    public Parser(Command... commands) {
        this.possibleCommands = new HashMap<>();
        for (Command command : commands) {
            possibleCommands.put(command.getKeyword(), command);
        }
        this.parsedCommands = new ArrayList<>();
    }

    public void parse(String[] args) throws ParserException {
        if (args.length != 2) throw new ParserException();
        Command parsedCommand = possibleCommands.get(args[0]);
        if (parsedCommand != null) {
            parseParameters(parsedCommand, args[1]);
            parsedCommands.add(parsedCommand);
            return;
        }
        throw new ParserException();
    }

    public List<Command> getParsedCommands() {
        return parsedCommands;
    }

    public void printUsage() {
        System.out.println("Usage printed");
    }

    private void parseParameters(Command command, String arg) throws ParserException {
        Class clazz = command.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Parameter.class)) {
                Parameter parameter = field.getAnnotation(Parameter.class);
                boolean isAccessible = field.isAccessible();
                try {
                    Method valueOf = parameter.converterClass().getMethod("valueOf", String.class);
                    field.setAccessible(true);
                    Object fieldValue = valueOf.invoke(command, arg);
                    field.set(command, valueOf.invoke(command, arg));
                } catch (NoSuchMethodException e) {
                    throw new ParserException("No valueOf converter method in parameter class.");
                } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                    throw new ParserException(e);
                } finally {
                    field.setAccessible(isAccessible);
                }
                return;
            }
        }

        throw new ParserException("Parameters parsing exception");
    }
}
