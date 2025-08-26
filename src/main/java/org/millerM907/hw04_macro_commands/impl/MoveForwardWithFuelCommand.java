package org.millerM907.hw04_macro_commands.impl;

import org.millerM907.hw04_macro_commands.base.Command;
import org.millerM907.hw04_macro_commands.base.FuelConsumer;
import org.millerM907.hw04_macro_commands.base.Movable;
import org.millerM907.hw04_macro_commands.impl.commands.BurnFuelCommand;
import org.millerM907.hw04_macro_commands.impl.commands.CheckFuelCommand;
import org.millerM907.hw04_macro_commands.impl.commands.MoveCommand;

import java.util.List;

public class MoveForwardWithFuelCommand implements Command {

    private final Command macroCommand;

    public MoveForwardWithFuelCommand(FuelConsumer fuelConsumer, Movable movable) {
        this.macroCommand = new MacroCommand(List.of(
                new CheckFuelCommand(fuelConsumer),
                new MoveCommand(movable),
                new BurnFuelCommand(fuelConsumer)
        ));
    }

    @Override
    public void execute() {
        macroCommand.execute();
    }
}
