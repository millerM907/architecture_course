package org.millerM907.hw06_generation_adapters_by_interface;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.millerM907.hw02_stable_abstractions.space_battle.ConfigurableObject;
import org.millerM907.hw02_stable_abstractions.space_battle.core.Vector;
import org.millerM907.hw05_ioc_container.base.Command;
import org.millerM907.hw05_ioc_container.impl.ioc.IoC;
import org.millerM907.hw06_generation_adapters_by_interface.spaceship.operations.Movable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AdapterFactoryTest {

    private ConfigurableObject obj;

    @BeforeEach
    void setUp() {
        obj = mock(ConfigurableObject.class);

        String interfaceName = Movable.class.getName();

        IoC.setResolveStrategy((key, args) -> {
            if (key.equals(interfaceName + ":position.get")) {
                return new Vector(1, 2);
            }
            if (key.equals(interfaceName + ":velocity.get")) {
                return new Vector(4, 5);
            }
            if (key.equals(interfaceName + ":position.set")) {
                return (Command) () -> obj.setProperty("position", args[1]);
            }
            throw new IllegalStateException("Unexpected key: " + key);
        });
    }

    @Test
    @DisplayName("Should return value from IoC when getPosition is called on Movable adapter")
    void givenMovableAdapter_whenGetPosition_thenReturnsValueFromIoC() {
        Movable adapter = AdapterFactory.createAdapter(Movable.class, obj);

        Vector result = adapter.getPosition();

        assertEquals(new Vector(1, 2), result);
    }

    @Test
    @DisplayName("Should return value from IoC when getVelocity is called on Movable adapter")
    void givenMovableAdapter_whenGetVelocity_thenReturnsValueFromIoC() {
        Movable adapter = AdapterFactory.createAdapter(Movable.class, obj);

        Vector result = adapter.getVelocity();

        assertEquals(new Vector(4, 5), result);
    }

    @Test
    @DisplayName("Should execute IoC command when setPosition is called on Movable adapter")
    void givenMovableAdapter_whenSetPosition_thenIoCCommandIsExecuted() {
        Movable adapter = AdapterFactory.createAdapter(Movable.class, obj);
        Vector newPos = new Vector(7, 8);

        adapter.setPosition(newPos);

        verify(obj).setProperty("position", newPos);
    }

    @Test
    @DisplayName("Should throw exception when unsupported method is called on Movable adapter")
    void givenMovableAdapter_whenUnsupportedMethod_thenThrowsException() {
        Movable adapter = AdapterFactory.createAdapter(Movable.class, obj);

        assertThrows(UnsupportedOperationException.class, adapter::toString);
    }

    @Test
    @DisplayName("Should throw exception when trying to create adapter for non-interface class")
    void givenNonInterface_whenCreateAdapter_thenThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> AdapterFactory.createAdapter(Vector.class, obj));
    }

    @AfterEach
    void tearDown() {
        IoC.setResolveStrategy((dep, args) -> {
            throw new IllegalStateException("IoC is not initialized");
        });
    }
}
