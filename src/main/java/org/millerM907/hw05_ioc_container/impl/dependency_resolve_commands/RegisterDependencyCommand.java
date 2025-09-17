package org.millerM907.hw05_ioc_container.impl.dependency_resolve_commands;

import org.millerM907.hw05_ioc_container.base.Command;
import org.millerM907.hw05_ioc_container.impl.ioc.IoC;

import java.util.function.Function;

public class RegisterDependencyCommand implements Command {
    private final String key;
    private final Function<Object[], Object> factory;

    public RegisterDependencyCommand(String key, Function<Object[], Object> factory) {
        this.key = key;
        this.factory = factory;
    }

    @Override
    public void execute() {
        IoC.<Command>resolve("IoC.Register.Internal", key, factory).execute();
    }
}
