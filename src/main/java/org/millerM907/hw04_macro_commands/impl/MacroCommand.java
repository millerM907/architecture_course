package org.millerM907.hw04_macro_commands.impl;

import org.millerM907.hw04_macro_commands.base.Command;

import java.util.List;

public class MacroCommand implements Command {

    private final List<Command> commands;

    public MacroCommand(List<Command> commands) {
        this.commands = commands;
    }

    @Override
    public void execute() {
        try {
            for (Command command : commands) {
                command.execute();
            }
        } catch (Exception e) {
            throw new CommandException(e);
        }
    }
}
