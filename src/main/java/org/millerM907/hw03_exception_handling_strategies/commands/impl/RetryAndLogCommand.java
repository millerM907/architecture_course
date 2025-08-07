package org.millerM907.hw03_exception_handling_strategies.commands.impl;

import org.millerM907.hw03_exception_handling_strategies.commands.base.Command;

import java.util.logging.Logger;

public class RetryAndLogCommand implements Command {

    private final Logger logger;
    private final Command originalCommand;

    public RetryAndLogCommand(Logger logger, Command originalCommand) {
        this.logger = logger;
        this.originalCommand = originalCommand;
    }

    @Override
    public void execute() {
        try {
            Command retryCommand = new RetryCommand(originalCommand);
            retryCommand.execute();
        } catch (Exception e) {
            LogExceptionCommand logExceptionCommand = new LogExceptionCommand(logger, originalCommand, e);
            logExceptionCommand.execute();
        }
    }
}
