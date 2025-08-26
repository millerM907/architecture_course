package org.millerM907.hw04_macro_commands;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.millerM907.hw04_macro_commands.base.FuelConsumer;
import org.millerM907.hw04_macro_commands.impl.commands.BurnFuelCommand;

import static org.mockito.Mockito.*;

public class BurnFuelCommandTest {

    @Test
    @DisplayName("Should decrease fuel level by consumption rate")
    void givenFuelGreaterThanConsumption_whenExecuted_thenFuelDecreased() {
        FuelConsumer fuelConsumer = mock(FuelConsumer.class);
        when(fuelConsumer.getFuelLevel()).thenReturn(10);
        when(fuelConsumer.getFuelConsumptionRate()).thenReturn(3);

        BurnFuelCommand command = new BurnFuelCommand(fuelConsumer);


        command.execute();


        verify(fuelConsumer).setFuelLevel(7);
    }

    @Test
    @DisplayName("Should set fuel level to zero when fuel equals consumption rate")
    void givenFuelEqualToConsumption_whenExecuted_thenFuelBecomesZero() {
        FuelConsumer fuelConsumer = mock(FuelConsumer.class);
        when(fuelConsumer.getFuelLevel()).thenReturn(5);
        when(fuelConsumer.getFuelConsumptionRate()).thenReturn(5);

        BurnFuelCommand command = new BurnFuelCommand(fuelConsumer);


        command.execute();


        verify(fuelConsumer).setFuelLevel(0);
    }

    @Test
    @DisplayName("Should set negative fuel level when fuel is less than consumption rate")
    void givenFuelLessThanConsumption_whenExecuted_thenFuelBecomesNegative() {
        FuelConsumer fuelConsumer = mock(FuelConsumer.class);
        when(fuelConsumer.getFuelLevel()).thenReturn(2);
        when(fuelConsumer.getFuelConsumptionRate()).thenReturn(5);

        BurnFuelCommand command = new BurnFuelCommand(fuelConsumer);


        command.execute();


        verify(fuelConsumer).setFuelLevel(-3);
    }
}
