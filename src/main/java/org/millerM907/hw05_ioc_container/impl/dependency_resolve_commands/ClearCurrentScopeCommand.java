package org.millerM907.hw05_ioc_container.impl.dependency_resolve_commands;

import org.millerM907.hw05_ioc_container.base.Command;
import org.millerM907.hw05_ioc_container.impl.ioc.IoC;

public class ClearCurrentScopeCommand implements Command {
    @Override
    public void execute() {
        IoC.<Command>resolve("IoC.Scope.Current.Clear.Internal").execute();
    }
}
