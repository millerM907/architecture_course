package org.millerM907.hw03_exception_handling_strategies.commands.base;

/**
 * Represents a command that can be executed.
 * <p>
 * This interface follows the Command design pattern, encapsulating a request
 * as an object, thereby allowing for parameterization of clients with different
 * requests, queuing of requests, and support for undoable operations.
 * </p>
 *
 * <p>Implementations of this interface should define the behavior of the {@code execute}
 * method, which performs the command's action.</p>
 */
public interface Command {

    /**
     * Executes the command.
     * <p>
     * Implementing classes should define the specific logic that should be executed
     * when this method is called. This method may throw exceptions if the execution
     * fails or encounters an error.
     * </p>
     */
    void execute();
}
