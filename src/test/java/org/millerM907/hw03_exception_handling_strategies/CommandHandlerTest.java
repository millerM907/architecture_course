package org.millerM907.hw03_exception_handling_strategies;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.millerM907.hw03_exception_handling_strategies.commands.base.Command;
import org.millerM907.hw03_exception_handling_strategies.commands.impl.EnqueueRetryCommand;
import org.millerM907.hw03_exception_handling_strategies.commands.impl.LogExceptionCommand;
import org.millerM907.hw03_exception_handling_strategies.commands.impl.RetryAndLogCommand;
import org.millerM907.hw03_exception_handling_strategies.commands.impl.RetryCommand;
import org.millerM907.hw03_exception_handling_strategies.exception_handlers.ExceptionHandler;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CommandHandlerTest {

    private static final String TEST_FAILURE_MESSAGE = "Test failure";

    @Test
    @DisplayName("Should log exception when command throws during execution")
    void givenCommandThrowsException_whenHandled_thenLogExceptionCommandExecuted() {
        Command faultyCommand = mock(Command.class);
        Logger mockLogger = mock(Logger.class);
        Exception testException = new RuntimeException(TEST_FAILURE_MESSAGE);

        doThrow(testException).when(faultyCommand).execute();

        Queue<Command> commandQueue = new LinkedList<>();
        commandQueue.add(faultyCommand);

        ExceptionHandler exceptionHandler = new ExceptionHandler();
        exceptionHandler.register(
                faultyCommand.getClass(),
                testException.getClass(),
                (cmd, ex) -> new LogExceptionCommand(mockLogger, cmd, ex)
        );

        CommandHandler commandHandler = new CommandHandler(commandQueue, exceptionHandler);


        commandHandler.handle();


        verify(mockLogger).severe(
                "Exception occurred during execution of command: "
                        + faultyCommand.getClass().getSimpleName()
                        + " - Exception: " + testException.getClass().getSimpleName()
                        + " - Message: " + testException.getMessage()
        );
    }

    @Test
    @DisplayName("Should retry command once when it throws an exception on first execution")
    void givenCommandThrowsException_whenHandled_thenRetryCommandExecuted() {
        Command originalCommand = mock(Command.class);
        RuntimeException testException = new RuntimeException(TEST_FAILURE_MESSAGE);

        doThrow(testException).doNothing().when(originalCommand).execute();

        Queue<Command> commandQueue = new LinkedList<>();
        commandQueue.add(originalCommand);

        ExceptionHandler exceptionHandler = new ExceptionHandler();
        exceptionHandler.register(
                originalCommand.getClass(),
                RuntimeException.class,
                (cmd, ex) -> new RetryCommand(cmd)
        );

        CommandHandler commandHandler = new CommandHandler(commandQueue, exceptionHandler);


        commandHandler.handle();


        verify(originalCommand, times(2)).execute();
    }

    @Test
    @DisplayName("Should enqueue and execute RetryCommand when original command throws an exception")
    void givenCommandThrowsException_whenHandled_thenRetryCommandIsEnqueuedAndExecuted() {
        Command originalCommand = mock(Command.class);
        Queue<Command> commandQueue = new LinkedList<>();

        RuntimeException testException = new RuntimeException(TEST_FAILURE_MESSAGE);
        doThrow(testException)
                .doNothing()
                .when(originalCommand).execute();

        ExceptionHandler exceptionHandler = new ExceptionHandler();
        exceptionHandler.register(
                originalCommand.getClass(),
                testException.getClass(),
                (cmd, ex) -> new EnqueueRetryCommand(commandQueue, cmd)
        );

        commandQueue.add(originalCommand);

        CommandHandler commandHandler = new CommandHandler(commandQueue, exceptionHandler);


        commandHandler.handle();


        verify(originalCommand, times(2)).execute();
        assertEquals(0, commandQueue.size());
    }


    @Test
    @DisplayName("Should retry and then log exception when command fails twice during execution")
    void givenCommandFailsTwice_whenHandled_thenCommandIsRetriedAndLogged() {
        Command failingCommand = mock(Command.class);
        Logger logger = mock(Logger.class);

        doThrow(new RuntimeException("First fail"))
                .doThrow(new RuntimeException("Second fail"))
                .when(failingCommand).execute();

        Queue<Command> commandQueue = new LinkedList<>();
        ExceptionHandler exceptionHandler = new ExceptionHandler();
        CommandHandler commandHandler = new CommandHandler(commandQueue, exceptionHandler);

        exceptionHandler.register(
                failingCommand.getClass(),
                RuntimeException.class,
                (cmd, ex) -> new RetryAndLogCommand(logger, cmd)
        );

        commandQueue.add(failingCommand);


        commandHandler.handle();


        verify(failingCommand, times(2)).execute();
        verify(logger).severe(contains("Exception occurred during execution of command"));
        assertTrue(commandQueue.isEmpty());
    }
}
