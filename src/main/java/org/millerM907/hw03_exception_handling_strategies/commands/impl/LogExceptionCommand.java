package org.millerM907.hw03_exception_handling_strategies.commands.impl;

import org.millerM907.hw03_exception_handling_strategies.commands.base.Command;

import java.util.logging.Logger;

public class LogExceptionCommand implements Command {

    private final Command originalCommand;
    private final Exception exception;
    private final Logger logger;

    public LogExceptionCommand(Logger logger, Command originalCommand, Exception exception) {
        this.logger = logger;
        this.originalCommand = originalCommand;
        this.exception = exception;
    }

    @Override
    public void execute() {
        logger.severe("Exception occurred during execution of command: "
                + originalCommand.getClass().getSimpleName()
                + " - Exception: " + exception.getClass().getSimpleName()
                + " - Message: " + exception.getMessage());
    }
}
