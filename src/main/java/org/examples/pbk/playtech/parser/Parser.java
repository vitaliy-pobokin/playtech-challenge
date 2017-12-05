package org.examples.pbk.playtech.parser;

import org.examples.pbk.playtech.commands.Command;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO: add app_name support for print usage
// TODO: builder for parser or Parser constructor commands min quantity handling with checking invariant
public class Parser {
    private Map<String, ParserCommandData> possibleCommands;
    private List<Command> parsedCommands;

    public Parser(Command... commands) {
        initPossibleCommands(commands);
        this.parsedCommands = new ArrayList<>();
    }

    private void initPossibleCommands(Command[] commands) throws IllegalArgumentException {
        this.possibleCommands = new HashMap<>();
        for (Command command : commands) {
            ParserCommandData data = getParserCommandData(command);
            possibleCommands.put(data.commandAnnotation.keyword(), data);
        }
    }

    public void parse(String[] args) throws ParserException {
        ParserCommandData parsedCommandData = possibleCommands.get(args[0]);
        if (parsedCommandData == null) throw new ParserException();
        parseParameters(parsedCommandData, args);
        parsedCommands.add((Command) parsedCommandData.command);
    }

    public List<Command> getParsedCommands() {
        return parsedCommands;
    }

    public void printUsage() {
        System.err.print("usage: <CommandLineUtil> [command] [parameters]\n" +
                "\t commands:\n");
        possibleCommands.forEach((String keyword, ParserCommandData data) -> {
            System.err.printf("\t\t%s", data.commandAnnotation.keyword());
            for (Parameter parameter : data.parameters.keySet()) {
                System.err.printf(" [%s]", parameter.name());
            }
            System.err.printf("\t\t%s\n", data.commandAnnotation.description());
        });
    }

    private void parseParameters(ParserCommandData data, String[] args) throws ParserException {
        if (data.parameters.size() != args.length - 1) throw new ParserException("Parameters quantity mismatch");
        int argPointer = 1;
        for (Map.Entry<Parameter, Field> entry : data.parameters.entrySet()) {
            Parameter parameter = entry.getKey();
            Field field = entry.getValue();
            boolean isAccessible = field.isAccessible();
            try {
                Method converterMethod = parameter.converterClass().getMethod(parameter.converterMethodName(), String.class);
                field.setAccessible(true);
                Object fieldValue = converterMethod.invoke(data.command, args[argPointer]);
                if (fieldValue == null)
                    throw new ParserException("Argument cannot be parsed with converter method in parameter class.");
                field.set(data.command, fieldValue);
                argPointer++;
            } catch (NoSuchMethodException e) {
                String message = String.format("Converter method %s(String s) in converter class not found.", parameter.converterMethodName());
                throw new ParserException(message);
            } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                throw new ParserException(e);
            } finally {
                field.setAccessible(isAccessible);
            }
        }
    }

    private ParserCommandData getParserCommandData(Object command) {
        ParserCommand commandAnnotation = command.getClass().getAnnotation(ParserCommand.class);
        if (commandAnnotation == null) {
            throw new IllegalArgumentException("@ParserCommand annotation should be used on command object.");
        }
        Map<Parameter, Field> parameters = new HashMap<>();
        Field[] fields = command.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Parameter.class)) {
                parameters.put(field.getAnnotation(Parameter.class), field);
            }
        }
        ParserCommandData data = new ParserCommandData();
        data.command = command;
        data.commandAnnotation = commandAnnotation;
        data.parameters = parameters;
        return data;
    }

    private class ParserCommandData {
        Object command;
        ParserCommand commandAnnotation;
        Map<Parameter, Field> parameters;
    }
}
