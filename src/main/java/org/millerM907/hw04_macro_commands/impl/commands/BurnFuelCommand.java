package org.millerM907.hw04_macro_commands.impl.commands;

import org.millerM907.hw04_macro_commands.base.Command;
import org.millerM907.hw04_macro_commands.base.FuelConsumer;

public class BurnFuelCommand implements Command {

    private final FuelConsumer fuelConsumer;

    public BurnFuelCommand(FuelConsumer fuelConsumer) {
        this.fuelConsumer = fuelConsumer;
    }

    @Override
    public void execute() {
        fuelConsumer.setFuelLevel(fuelConsumer.getFuelLevel() - fuelConsumer.getFuelConsumptionRate());
    }
}
