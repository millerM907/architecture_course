package org.millerM907.hw03_exception_handling_strategies;

import org.millerM907.hw03_exception_handling_strategies.commands.base.Command;
import org.millerM907.hw03_exception_handling_strategies.exception_handlers.ExceptionHandler;

import java.util.Queue;

public class CommandHandler {

    private final Queue<Command> commandQueue;
    private final ExceptionHandler exceptionHandler;

    public CommandHandler(Queue<Command> commandQueue, ExceptionHandler exceptionHandler) {
        this.commandQueue = commandQueue;
        this.exceptionHandler = exceptionHandler;
    }

    void handle() {
        while (!commandQueue.isEmpty()) {
            Command command = commandQueue.poll();
            try {
                command.execute();
            } catch (Exception e) {
                exceptionHandler.handle(command, e).execute();
            }
        }
    }
}
