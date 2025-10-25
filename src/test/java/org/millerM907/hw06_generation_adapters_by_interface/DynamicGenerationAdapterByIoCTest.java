package org.millerM907.hw06_generation_adapters_by_interface;

import org.junit.jupiter.api.*;
import org.millerM907.hw02_stable_abstractions.space_battle.ConfigurableObject;
import org.millerM907.hw02_stable_abstractions.space_battle.core.Vector;
import org.millerM907.hw05_ioc_container.base.Command;
import org.millerM907.hw05_ioc_container.impl.ioc.InitCommand;
import org.millerM907.hw05_ioc_container.impl.ioc.IoC;
import org.millerM907.hw06_generation_adapters_by_interface.spaceship.operations.Movable;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class DynamicGenerationAdapterByIoCTest {

    private ConfigurableObject obj;

    @BeforeEach
    void setUp() {
        new InitCommand().execute();


        obj = mock(ConfigurableObject.class);

        String interfaceName = Movable.class.getName();

        Command registerPosition = IoC.resolve(
                "IoC.Register",
                interfaceName + ":position.get",
                (Function<Object[], Object>) args -> new Vector(1, 2)
        );
        registerPosition.execute();


        Command registerVelocity = IoC.resolve(
                "IoC.Register",
                interfaceName + ":velocity.get",
                (Function<Object[], Object>) args -> new Vector(3, 4)
        );
        registerVelocity.execute();
    }

    @Test
    @DisplayName("Should create adapter via IoC when Adapter strategy is registered")
    void givenAdapterRegisteredInIoC_whenResolveAdapter_thenAdapterWorksCorrectly() {
        Command registerAdapter = IoC.resolve(
                "IoC.Register",
                "Adapter",
                (Function<Object[], Object>) args -> {
                    Class<?> iface = (Class<?>) args[0];
                    ConfigurableObject obj = (ConfigurableObject) args[1];
                    return AdapterFactory.createAdapter(iface, obj);
                }
        );
        registerAdapter.execute();


        Movable adapter = IoC.resolve("Adapter", Movable.class, obj);


        Vector pos = adapter.getPosition();
        assertEquals(new Vector(1, 2), pos);


        Vector vel = adapter.getVelocity();
        assertEquals(new Vector(3, 4), vel);
    }

    @AfterEach
    void tearDown() {
        resetIoCCompletely();
    }

    private static void resetIoCCompletely() {
        IoC.setResolveStrategy((dep, args) -> {
            throw new IllegalStateException("IoC is not initialized");
        });

        try {
            // 2) InitCommand.alreadyInitialized = false
            var fInit = InitCommand.class.getDeclaredField("alreadyInitialized");
            fInit.setAccessible(true);
            fInit.set(null, false);

            //clean rootScope
            var fRoot = InitCommand.class.getDeclaredField("rootScope");
            fRoot.setAccessible(true);
            @SuppressWarnings("unchecked")
            var root = (java.util.concurrent.ConcurrentHashMap<String, java.util.function.Function<Object[], Object>>) fRoot.get(null);
            root.clear();

            // clean ThreadLocal current scope
            var fTL = InitCommand.class.getDeclaredField("currentScopes");
            fTL.setAccessible(true);
            ThreadLocal<?> tl = (ThreadLocal<?>) fTL.get(null);
            tl.remove();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to reset IoC", e);
        }
    }

}
