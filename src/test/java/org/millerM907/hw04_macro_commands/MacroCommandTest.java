package org.millerM907.hw04_macro_commands;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.millerM907.hw04_macro_commands.base.Command;
import org.millerM907.hw04_macro_commands.impl.CommandException;
import org.millerM907.hw04_macro_commands.impl.MacroCommand;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MacroCommandTest {

    @Test
    @DisplayName("Should execute all commands when no exceptions")
    void givenAllCommandsSucceed_whenExecuted_thenAllCommandsRun() {
        Command first = mock(Command.class);
        Command second = mock(Command.class);
        MacroCommand macroCommand = new MacroCommand(List.of(first, second));


        macroCommand.execute();


        verify(first).execute();
        verify(second).execute();
    }

    @Test
    @DisplayName("Should stop execution when a command throws exception")
    void givenCommandThrows_whenExecuted_thenExecutionStopsAndExceptionThrown() {
        Command first = mock(Command.class);
        Command second = mock(Command.class);
        doThrow(new RuntimeException("Boom")).when(first).execute();
        MacroCommand macroCommand = new MacroCommand(List.of(first, second));


        CommandException ex = assertThrows(CommandException.class, macroCommand::execute);


        assertInstanceOf(RuntimeException.class, ex.getCause());
        assertEquals("Boom", ex.getCause().getMessage());

        verify(first).execute();
        verify(second, never()).execute();
    }

    @Test
    @DisplayName("Should wrap CommandException thrown by command")
    void givenCommandThrowsCommandException_whenExecuted_thenWrappedAndExecutionStops() {
        Command first = mock(Command.class);
        Command second = mock(Command.class);
        doThrow(new CommandException("Wrapped already")).when(first).execute();
        MacroCommand macroCommand = new MacroCommand(List.of(first, second));


        CommandException ex = assertThrows(CommandException.class, macroCommand::execute);


        assertEquals("Wrapped already", ex.getCause().getMessage());

        verify(first).execute();
        verify(second, never()).execute();
    }

    @Test
    @DisplayName("Should do nothing when command list is empty")
    void givenEmptyCommandList_whenExecuted_thenNoActionTaken() {
        MacroCommand macroCommand = new MacroCommand(List.of());


        assertDoesNotThrow(macroCommand::execute);
    }
}
