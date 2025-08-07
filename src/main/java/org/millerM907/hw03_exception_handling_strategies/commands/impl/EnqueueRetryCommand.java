package org.millerM907.hw03_exception_handling_strategies.commands.impl;

import org.millerM907.hw03_exception_handling_strategies.commands.base.Command;

import java.util.Queue;

public class EnqueueRetryCommand implements Command {

    private final Queue<Command> commandQueue;
    private final Command originalCommand;

    public EnqueueRetryCommand(Queue<Command> commandQueue, Command originalCommand) {
        this.commandQueue = commandQueue;
        this.originalCommand = originalCommand;
    }

    @Override
    public void execute() {
        commandQueue.add(new RetryCommand(originalCommand));
    }
}
