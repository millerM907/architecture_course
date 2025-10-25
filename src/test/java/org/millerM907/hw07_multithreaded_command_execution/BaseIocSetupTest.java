package org.millerM907.hw07_multithreaded_command_execution;

import org.junit.jupiter.api.BeforeEach;
import org.millerM907.hw05_ioc_container.base.Command;
import org.millerM907.hw05_ioc_container.impl.ioc.InitCommand;
import org.millerM907.hw05_ioc_container.impl.ioc.IoC;

import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class BaseIocSetupTest {
    @BeforeEach
    void initIoc() {
        new InitCommand().execute();

        IoC.<Command>resolve("IoC.Register",
                "ExceptionHandler.Strategy",
                (Function<Object[], Object>) args ->
                        (BiConsumer<Command, Exception>) (cmd, ex) ->
                                System.err.printf("[ExceptionHandler] cmd=%s ex=%s%n",
                                        cmd != null ? cmd.getClass().getSimpleName() : "<null>", ex)
        ).execute();

        IoC.<Command>resolve("IoC.Register",
                "ExceptionHandler",
                (Function<Object[], Object>) args -> {
                    final Command failed =
                            args.length > 0 && args[0] instanceof Command ? (Command) args[0] : null;
                    final Exception ex = (Exception) args[1];

                    final BiConsumer<Command, Exception> strategy = getStrategySafe();

                    return (Command) () -> {
                        if (strategy != null) {
                            strategy.accept(failed, ex);
                        }
                    };
                }
        ).execute();
    }

    /**
     * Безопасное получение зарегистрированной стратегии.
     */
    private BiConsumer<Command, Exception> getStrategySafe() {
        try {
            return IoC.resolve("ExceptionHandler.Strategy");
        } catch (Exception e) {
            return null;
        }
    }
}
