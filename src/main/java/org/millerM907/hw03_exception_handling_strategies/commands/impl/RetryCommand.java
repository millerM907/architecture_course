package org.millerM907.hw03_exception_handling_strategies.commands.impl;

import org.millerM907.hw03_exception_handling_strategies.commands.base.Command;

public class RetryCommand implements Command {

    private final Command originalCommand;

    public RetryCommand(Command originalCommand) {
        this.originalCommand = originalCommand;
    }

    @Override
    public void execute() {
        originalCommand.execute();
    }
}
