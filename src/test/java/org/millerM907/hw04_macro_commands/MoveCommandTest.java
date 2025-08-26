package org.millerM907.hw04_macro_commands;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.millerM907.hw02_stable_abstractions.space_battle.core.Point;
import org.millerM907.hw02_stable_abstractions.space_battle.core.Vector;
import org.millerM907.hw04_macro_commands.base.Movable;
import org.millerM907.hw04_macro_commands.impl.commands.MoveCommand;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class MoveCommandTest {

    @Test
    @DisplayName("Should update location by adding velocity")
    void givenValidLocationAndVelocity_whenExecute_thenLocationUpdated() {
        Movable movable = mock(Movable.class);
        Point start = new Point(5, 10);
        Vector velocity = new Vector(3, -2);
        Point expected = start.plus(velocity);

        when(movable.getLocation()).thenReturn(start);
        when(movable.getVelocity()).thenReturn(velocity);

        MoveCommand command = new MoveCommand(movable);


        command.execute();


        verify(movable).setLocation(expected);
    }

    @Test
    @DisplayName("Should throw NullPointerException if getLocation returns null")
    void givenNullLocation_whenExecute_thenThrowException() {
        Movable movable = mock(Movable.class);

        when(movable.getLocation()).thenReturn(null);
        when(movable.getVelocity()).thenReturn(new Vector(1, 1));

        MoveCommand command = new MoveCommand(movable);


        assertThrows(NullPointerException.class, command::execute);
    }

    @Test
    @DisplayName("Should throw NullPointerException if getVelocity returns null")
    void givenNullVelocity_whenExecute_thenThrowException() {
        Movable movable = mock(Movable.class);

        when(movable.getLocation()).thenReturn(new Point(1, 1));
        when(movable.getVelocity()).thenReturn(null);

        MoveCommand command = new MoveCommand(movable);


        assertThrows(NullPointerException.class, command::execute);
    }

    @Test
    @DisplayName("Should propagate exception thrown by setLocation")
    void givenSetLocationThrowsException_whenExecute_thenPropagateException() {
        Movable movable = mock(Movable.class);
        Point start = new Point(0, 0);
        Vector velocity = new Vector(1, 1);

        when(movable.getLocation()).thenReturn(start);
        when(movable.getVelocity()).thenReturn(velocity);
        doThrow(new IllegalStateException("Set location failed")).when(movable).setLocation(any());

        MoveCommand command = new MoveCommand(movable);


        IllegalStateException ex = assertThrows(IllegalStateException.class, command::execute);


        assertEquals("Set location failed", ex.getMessage());
    }
}
