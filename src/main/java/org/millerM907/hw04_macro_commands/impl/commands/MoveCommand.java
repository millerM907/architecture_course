package org.millerM907.hw04_macro_commands.impl.commands;


import org.millerM907.hw04_macro_commands.base.Command;
import org.millerM907.hw04_macro_commands.base.Movable;

public class MoveCommand implements Command {

    private final Movable movable;

    public MoveCommand(Movable movable) {
        this.movable = movable;
    }

    public void execute() {
        movable.setLocation(movable.getLocation().plus(movable.getVelocity()));
    }
}
