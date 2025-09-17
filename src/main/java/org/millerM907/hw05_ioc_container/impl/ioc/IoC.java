package org.millerM907.hw05_ioc_container.impl.ioc;

import java.util.function.BiFunction;

public final class IoC {

    private static BiFunction<String, Object[], Object> strategy =
            (dep, args) -> { throw new IllegalStateException("IoC is not initialized"); };

    private IoC() {}

    @SuppressWarnings("unchecked")
    public static <T> T resolve(String key, Object... args) {
        return (T) strategy.apply(key, args);
    }

    public static void setResolveStrategy(BiFunction<String, Object[], Object> newStrategy) {
        strategy = newStrategy;
    }
}
