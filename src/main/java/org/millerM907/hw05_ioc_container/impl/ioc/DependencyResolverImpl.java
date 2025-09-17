package org.millerM907.hw05_ioc_container.impl.ioc;

import org.millerM907.hw05_ioc_container.base.DependencyResolver;

import java.util.Map;
import java.util.function.Function;

public class DependencyResolverImpl implements DependencyResolver {

    private final Map<String, Function<Object[], Object>> scope;

    public DependencyResolverImpl(Map<String, Function<Object[], Object>> scope) {
        this.scope = scope;
    }

    @Override
    public <T> T resolve(String dependency, Object... args) {
        Map<String, Function<Object[], Object>> current = scope;
        while (true) {
            Function<Object[], Object> strategy = current.get(dependency);
            if (strategy != null) {
                return (T) strategy.apply(args);
            } else {
                Function<Object[], Object> parentResolver = current.get("IoC.Scope.Parent");
                if (parentResolver == null) {
                    throw new IllegalStateException("No parent scope found for dependency: " + dependency);
                }
                current = (Map<String, Function<Object[], Object>>) parentResolver.apply(args);
            }
        }
    }
}
