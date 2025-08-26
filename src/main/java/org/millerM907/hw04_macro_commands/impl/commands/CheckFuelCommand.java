package org.millerM907.hw04_macro_commands.impl.commands;

import org.millerM907.hw04_macro_commands.base.Command;
import org.millerM907.hw04_macro_commands.base.FuelConsumer;
import org.millerM907.hw04_macro_commands.impl.CommandException;

public class CheckFuelCommand implements Command {

    private final FuelConsumer fuelConsumer;

    public CheckFuelCommand(FuelConsumer fuelConsumer) {
        this.fuelConsumer = fuelConsumer;
    }

    @Override
    public void execute() {
        if (fuelConsumer.getFuelLevel() - fuelConsumer.getFuelConsumptionRate() < 0) {
            throw new CommandException("Not enough fuel");
        }
    }
}
