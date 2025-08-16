package org.millerM907.hw04_macro_commands;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.millerM907.hw02_stable_abstractions.space_battle.core.Point;
import org.millerM907.hw02_stable_abstractions.space_battle.core.Vector;
import org.millerM907.hw04_macro_commands.base.FuelConsumer;
import org.millerM907.hw04_macro_commands.base.Movable;
import org.millerM907.hw04_macro_commands.impl.CommandException;
import org.millerM907.hw04_macro_commands.impl.MoveForwardWithFuelCommand;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MoveForwardWithFuelCommandTest {

    @Test
    @DisplayName("Should execute macro command: check fuel, burn fuel, move")
    void givenValidFuel_whenExecute_thenBurnFuelAndCallSetLocation() {
        FuelConsumer fuelConsumer = mock(FuelConsumer.class);
        Movable movable = mock(Movable.class);

        when(fuelConsumer.getFuelLevel()).thenReturn(10);
        when(fuelConsumer.getFuelConsumptionRate()).thenReturn(3);

        Point initialPoint = new Point(1, 2);
        Vector velocity = new Vector(3, 4);
        when(movable.getLocation()).thenReturn(initialPoint);
        when(movable.getVelocity()).thenReturn(velocity);

        MoveForwardWithFuelCommand command = new MoveForwardWithFuelCommand(fuelConsumer, movable);


        command.execute();


        verify(fuelConsumer).setFuelLevel(anyInt());
        verify(movable).setLocation(any(Point.class));
    }

    @Test
    @DisplayName("Should throw CommandException and stop if fuel check fails")
    void givenInsufficientFuel_whenExecute_thenThrowCommandException() {
        FuelConsumer fuelConsumer = mock(FuelConsumer.class);
        Movable movable = mock(Movable.class);

        when(fuelConsumer.getFuelLevel()).thenReturn(1);
        when(fuelConsumer.getFuelConsumptionRate()).thenReturn(3);

        MoveForwardWithFuelCommand command = new MoveForwardWithFuelCommand(fuelConsumer, movable);


        CommandException ex = assertThrows(CommandException.class, command::execute);


        verify(fuelConsumer, never()).setFuelLevel(anyInt());
        verify(movable, never()).setLocation(any(Point.class));
        assertNotNull(ex.getCause());
    }
}
