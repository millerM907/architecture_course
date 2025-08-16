package org.millerM907.hw03_exception_handling_strategies.exception_handlers;

import org.millerM907.hw03_exception_handling_strategies.commands.base.Command;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class ExceptionHandler {

    private final Map<CommandExceptionKey, BiFunction<Command, Exception, Command>> store = new HashMap<>();

    public Command handle(Command cmd, Exception e) {
        CommandExceptionKey key = new CommandExceptionKey(cmd.getClass(), e.getClass());
        BiFunction<Command, Exception, Command> handler = store.get(key);
        if (handler != null) {
            return handler.apply(cmd, e);
        } else {
            throw new IllegalStateException("No handler registered for: " + key);
        }
    }

    public void register(Class<? extends Command> cmdType,
                         Class<? extends Exception> exceptionType,
                         BiFunction<Command, Exception, Command> handler) {
        CommandExceptionKey key = new CommandExceptionKey(cmdType, exceptionType);
        store.put(key, handler);
    }
}
