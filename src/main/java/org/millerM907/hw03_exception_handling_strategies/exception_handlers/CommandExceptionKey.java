package org.millerM907.hw03_exception_handling_strategies.exception_handlers;

import org.millerM907.hw03_exception_handling_strategies.commands.base.Command;

import java.util.Objects;

public class CommandExceptionKey {

    private final Class<? extends Command> commandType;
    private final Class<? extends Exception> exceptionType;

    public CommandExceptionKey(Class<? extends Command> commandType, Class<? extends Exception> exceptionType) {
        this.commandType = commandType;
        this.exceptionType = exceptionType;
    }

    public Class<? extends Command> getCommandType() {
        return commandType;
    }

    public Class<? extends Exception> getExceptionType() {
        return exceptionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommandExceptionKey)) return false;
        CommandExceptionKey that = (CommandExceptionKey) o;
        return Objects.equals(commandType, that.commandType) &&
                Objects.equals(exceptionType, that.exceptionType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commandType, exceptionType);
    }

    @Override
    public String toString() {
        return "CommandExceptionKey{" +
                "commandType=" + commandType.getSimpleName() +
                ", exceptionType=" + exceptionType.getSimpleName() +
                '}';
    }
}
