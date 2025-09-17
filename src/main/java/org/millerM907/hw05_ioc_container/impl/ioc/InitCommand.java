package org.millerM907.hw05_ioc_container.impl.ioc;

import org.millerM907.hw05_ioc_container.base.Command;
import org.millerM907.hw05_ioc_container.base.DependencyResolver;
import org.millerM907.hw05_ioc_container.impl.dependency_resolve_commands.ClearCurrentScopeCommand;
import org.millerM907.hw05_ioc_container.impl.dependency_resolve_commands.RegisterDependencyCommand;
import org.millerM907.hw05_ioc_container.impl.dependency_resolve_commands.SetCurrentScopeCommand;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class InitCommand implements Command {

    private static final ThreadLocal<Object> currentScopes = new ThreadLocal<>();
    private static final ConcurrentHashMap<String, Function<Object[], Object>> rootScope = new ConcurrentHashMap<>();
    private static boolean alreadyInitialized = false;

    @Override
    public void execute() {
        if (alreadyInitialized) return;

        synchronized (rootScope) {
            rootScope.put("IoC.Scope.Current.Set", args -> new SetCurrentScopeCommand(args[0]));
            rootScope.put("IoC.Scope.Current.Clear", args -> new ClearCurrentScopeCommand());
            rootScope.put("IoC.Scope.Current", args -> currentScopes.get() != null ? currentScopes.get() : rootScope);
            rootScope.put("IoC.Scope.Parent", args -> { throw new IllegalStateException("Root scope has no parent"); });
            rootScope.put("IoC.Scope.Create.Empty", args -> new HashMap<String, Function<Object[], Object>>());
            rootScope.put("IoC.Scope.Create", args -> {
                Map<String, Function<Object[], Object>> newScope = IoC.resolve("IoC.Scope.Create.Empty");
                Object parent = args.length > 0 ? args[0] : IoC.resolve("IoC.Scope.Current");
                newScope.put("IoC.Scope.Parent", __ -> parent);
                return newScope;
            });
            rootScope.put("IoC.Register", args -> new RegisterDependencyCommand((String) args[0], (Function<Object[], Object>) args[1]));
            rootScope.put("IoC.Scope.Current.Set.Internal", args -> new Command() {
                @Override
                public void execute() {
                    InitCommand.currentScopes.set(args[0]);
                }
            });
            rootScope.put("IoC.Scope.Current.Clear.Internal", args -> new Command() {
                @Override
                public void execute() {
                    InitCommand.currentScopes.remove();
                }
            });
            rootScope.put("IoC.Register.Internal", args -> new Command() {
                @SuppressWarnings("unchecked")
                @Override
                public void execute() {
                    Map<String, Function<Object[], Object>> scope =
                            (Map<String, Function<Object[], Object>>) IoC.resolve("IoC.Scope.Current");
                    String key = (String) args[0];
                    Function<Object[], Object> factory = (Function<Object[], Object>) args[1];
                    scope.put(key, factory);
                }
            });

            IoC.setResolveStrategy((dependency, args) -> {
                Object scope = currentScopes.get() != null ? currentScopes.get() : rootScope;
                DependencyResolver resolver = new DependencyResolverImpl((Map<String, Function<Object[], Object>>) scope);
                return resolver.resolve(dependency, args);
            });

            alreadyInitialized = true;
        }
    }
}
