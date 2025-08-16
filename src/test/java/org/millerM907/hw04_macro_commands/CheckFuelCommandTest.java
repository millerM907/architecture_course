package org.millerM907.hw04_macro_commands;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.millerM907.hw04_macro_commands.base.FuelConsumer;
import org.millerM907.hw04_macro_commands.impl.commands.CheckFuelCommand;
import org.millerM907.hw04_macro_commands.impl.CommandException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CheckFuelCommandTest {

    @Test
    @DisplayName("Should not throw when fuel is greater than consumption rate")
    void givenFuelGreaterThanConsumption_whenExecuted_thenNoException() {
        FuelConsumer fuelConsumer = mock(FuelConsumer.class);
        when(fuelConsumer.getFuelLevel()).thenReturn(10);
        when(fuelConsumer.getFuelConsumptionRate()).thenReturn(5);

        CheckFuelCommand command = new CheckFuelCommand(fuelConsumer);


        assertDoesNotThrow(command::execute);
    }

    @Test
    @DisplayName("Should not throw when fuel is exactly equal to consumption rate")
    void givenFuelEqualToConsumption_whenExecuted_thenNoException() {
        FuelConsumer fuelConsumer = mock(FuelConsumer.class);
        when(fuelConsumer.getFuelLevel()).thenReturn(5);
        when(fuelConsumer.getFuelConsumptionRate()).thenReturn(5);

        CheckFuelCommand command = new CheckFuelCommand(fuelConsumer);


        assertDoesNotThrow(command::execute);
    }

    @Test
    @DisplayName("Should throw CommandException when fuel is less than consumption rate")
    void givenFuelLessThanConsumption_whenExecuted_thenThrowsException() {
        FuelConsumer fuelConsumer = mock(FuelConsumer.class);
        when(fuelConsumer.getFuelLevel()).thenReturn(3);
        when(fuelConsumer.getFuelConsumptionRate()).thenReturn(5);

        CheckFuelCommand command = new CheckFuelCommand(fuelConsumer);


        CommandException ex = assertThrows(CommandException.class, command::execute);
        assertEquals("Not enough fuel", ex.getMessage());
    }
}
