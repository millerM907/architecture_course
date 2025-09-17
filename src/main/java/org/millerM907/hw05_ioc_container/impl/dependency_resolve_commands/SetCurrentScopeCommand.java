package org.millerM907.hw05_ioc_container.impl.dependency_resolve_commands;

import org.millerM907.hw05_ioc_container.base.Command;
import org.millerM907.hw05_ioc_container.impl.ioc.IoC;

public class SetCurrentScopeCommand implements Command {
    private final Object scope;

    public SetCurrentScopeCommand(Object scope) {
        this.scope = scope;
    }

    @Override
    public void execute() {
        IoC.<Command>resolve("IoC.Scope.Current.Set.Internal", scope).execute();
    }
}