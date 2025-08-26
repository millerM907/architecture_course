package org.millerM907.hw04_macro_commands.impl;

public class CommandException extends RuntimeException {
    public CommandException(String message) {
        super(message);
    }

    public CommandException(Exception e) {
        super(e);
    }
}
