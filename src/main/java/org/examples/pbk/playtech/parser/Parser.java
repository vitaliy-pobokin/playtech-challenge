package org.examples.pbk.playtech.parser;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Parser {
    private Map<String, ParserCommandInfo> possibleCommands;
    private Object parsedCommand;

    public Parser(Object... commands) {
        initPossibleCommands(commands);
    }

    public void parse(String[] args) throws ParserException {
        ParserCommandInfo parsedCommandData = possibleCommands.get(args[0]);
        if (parsedCommandData == null) throw new ParserException("Command not found");
        parseParameters(parsedCommandData, args);
        parsedCommand = parsedCommandData.command;
    }

    public Object getParsedCommand() {
        return parsedCommand;
    }

    public String getUsageInfo() {
        StringBuilder builder = new StringBuilder("usage: <CommandLineUtil> [command] [parameters]\n" +
                "\t commands:\n");
        possibleCommands.forEach((String keyword, ParserCommandInfo commandInfo) -> {
            builder.append(String.format("\t\t%s", commandInfo.commandAnnotation.keyword()));
            for (Parameter parameter : commandInfo.parameters.keySet()) {
                builder.append(String.format(" [%s]", parameter.name()));
            }
            builder.append(String.format("\t\t%s\n", commandInfo.commandAnnotation.description()));
        });
        return builder.toString();
    }

    private void initPossibleCommands(Object[] commands) throws IllegalArgumentException {
        this.possibleCommands = new HashMap<>();
        for (Object command : commands) {
            ParserCommandInfo commandInfo = getParserCommandInfo(command);
            possibleCommands.put(commandInfo.commandAnnotation.keyword(), commandInfo);
        }
    }

    private void parseParameters(ParserCommandInfo commandInfo, String[] args) throws ParserException {
        if (commandInfo.parameters.size() != args.length - 1) throw new ParserException("Parameters quantity mismatch");
        int argPointer = 1;
        for (Map.Entry<Parameter, Field> entry : commandInfo.parameters.entrySet()) {
            Parameter parameter = entry.getKey();
            Field field = entry.getValue();
            boolean isAccessible = field.isAccessible();
            try {
                Method converterMethod = parameter.converterClass().getMethod(parameter.converterMethodName(), String.class);
                field.setAccessible(true);
                Object fieldValue = converterMethod.invoke(commandInfo.command, args[argPointer]);
                if (fieldValue == null)
                    throw new ParserException("Argument cannot be parsed with converter method in parameter class.");
                field.set(commandInfo.command, fieldValue);
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

    private ParserCommandInfo getParserCommandInfo(Object command) {
        ParserCommand commandAnnotation = command.getClass().getAnnotation(ParserCommand.class);
        if (commandAnnotation == null)
            throw new IllegalArgumentException("@ParserCommand annotation should be used on command object.");
        Map<Parameter, Field> parameters = getFieldsWithParameterAnnotation(command);
        ParserCommandInfo commandInfo = new ParserCommandInfo();
        commandInfo.command = command;
        commandInfo.commandAnnotation = commandAnnotation;
        commandInfo.parameters = parameters;
        return commandInfo;
    }

    private Map<Parameter, Field> getFieldsWithParameterAnnotation(Object command) {
        Map<Parameter, Field> parameters = new HashMap<>();
        Field[] fields = command.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Parameter.class))
                parameters.put(field.getAnnotation(Parameter.class), field);
        }
        return parameters;
    }

    private class ParserCommandInfo {
        Object command;
        ParserCommand commandAnnotation;
        Map<Parameter, Field> parameters;
    }
}
