package org.millerM907.hw05_ioc_container;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.millerM907.hw05_ioc_container.base.Command;
import org.millerM907.hw05_ioc_container.impl.ioc.InitCommand;
import org.millerM907.hw05_ioc_container.impl.ioc.IoC;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

public class IoCTest {

    @BeforeEach
    void setUp() {
        new InitCommand().execute();
    }

    @Test
    @DisplayName("Should register core dependencies in root scope")
    void givenIoCNotInitialized_whenInitExecuted_thenRootScopeIsRegistered() {
        Object currentScope = IoC.resolve("IoC.Scope.Current");


        assertNotNull(currentScope);
        assertTrue(currentScope instanceof ConcurrentHashMap);
    }

    @Test
    @DisplayName("Should not reinitialize IoC when InitCommand executed twice")
    void givenIoCInitialized_whenInitExecutedAgain_thenNoExceptionThrown() {
        new InitCommand().execute();


        assertDoesNotThrow(() -> IoC.resolve("IoC.Scope.Current"));
    }

    @Test
    @DisplayName("Should register simple dependency in root scope")
    void givenIoCInitialized_whenRegisterSimpleDependency_thenDependencyIsResolvable() {
        Command registerCmd = IoC.<Command>resolve(
                "IoC.Register",
                "foo",
                (Function<Object[], Object>) args -> "bar"
        );


        registerCmd.execute();


        assertEquals("bar", IoC.resolve("foo"));
    }

    @Test
    @DisplayName("Should register dependency with arguments and resolve correctly")
    void givenIoCInitialized_whenRegisterDependencyWithArgs_thenResolvedValueMatches() {
        Command registerCmd = IoC.<Command>resolve(
                "IoC.Register",
                "sum",
                (Function<Object[], Object>) args -> (int) args[0] + (int) args[1]
        );


        registerCmd.execute();


        assertEquals(5, IoC.<Integer>resolve("sum", 2, 3));
    }

    @Test
    @DisplayName("Should switch to parent scope after clearing current scope")
    void givenChildScopeIsCurrent_whenClearScopeExecuted_thenCurrentScopeSwitchedToParent() {
        Map<String, Function<Object[], Object>> childScope =
                IoC.<Map<String, Function<Object[], Object>>>resolve("IoC.Scope.Create");
        IoC.<Command>resolve("IoC.Scope.Current.Set", childScope).execute();

        Map<String, Function<Object[], Object>> currentScopeBeforeClear =
                IoC.<Map<String, Function<Object[], Object>>>resolve("IoC.Scope.Current");
        assertSame(childScope, currentScopeBeforeClear, "Current scope should be childScope");

        Command clearCmd = IoC.<Command>resolve("IoC.Scope.Current.Clear");


        clearCmd.execute();


        Map<String, Function<Object[], Object>> currentScopeAfterClear =
                IoC.<Map<String, Function<Object[], Object>>>resolve("IoC.Scope.Current");
        assertNotSame(childScope, currentScopeAfterClear, "Current scope should no longer be childScope");
    }

    @Test
    @DisplayName("Child scope should override parent dependency and revert after clearing")
    void givenChildScopeOverridesParent_whenCleared_thenParentDependencyResolves() {
        IoC.<Command>resolve("IoC.Register", "foo", (Function<Object[], Object>) args -> "root").execute();
        Map<String, Function<Object[], Object>> rootScopeMap =
                IoC.<Map<String, Function<Object[], Object>>>resolve("IoC.Scope.Current");

        Map<String, Function<Object[], Object>> childScope =
                IoC.<Map<String, Function<Object[], Object>>>resolve("IoC.Scope.Create", rootScopeMap);
        childScope.put("foo", args -> "child");

        IoC.<Command>resolve("IoC.Scope.Current.Set", childScope).execute();

        assertEquals("child", IoC.<String>resolve("foo"));


        IoC.<Command>resolve("IoC.Scope.Current.Clear").execute();

        assertEquals("root", IoC.<String>resolve("foo"));
    }

    @Test
    @DisplayName("Should resolve dependency from parent scope when not present in child")
    void givenDependencyInParent_whenResolvingFromChild_thenParentDependencyReturned() {
        IoC.<Command>resolve("IoC.Register", "foo", (Function<Object[], Object>) args -> "root").execute();
        Map<String, Function<Object[], Object>> childScope = IoC.resolve("IoC.Scope.Create");
        IoC.<Command>resolve("IoC.Scope.Current.Set", childScope).execute();


        assertEquals("root", IoC.resolve("foo"));
    }

    @Test
    @DisplayName("Resolving unknown dependency should throw exception")
    void givenUnknownDependency_whenResolve_thenThrowsException() {
        assertThrows(IllegalStateException.class, () -> IoC.resolve("unknown"));
    }

    @Test
    @DisplayName("Internal registration should make dependency resolvable")
    void givenInternalRegister_whenExecuted_thenDependencyIsResolvable() {
        Command registerInternalCmd = IoC.<Command>resolve(
                "IoC.Register.Internal",
                "foo",
                (Function<Object[], Object>) args -> "bar"
        );


        registerInternalCmd.execute();


        assertEquals("bar", IoC.resolve("foo"));
    }

    @Test
    @DisplayName("ThreadLocal scopes should be isolated across threads")
    void givenTwoThreads_whenScopesSetSeparately_thenDependenciesAreIsolated() throws InterruptedException {
        AtomicReference<String> result1 = new AtomicReference<>();
        AtomicReference<String> result2 = new AtomicReference<>();

        Thread t1 = new Thread(() -> {
            Map<String, Function<Object[], Object>> scope1 = IoC.resolve("IoC.Scope.Create");
            scope1.put("foo", args -> "thread1");
            IoC.<Command>resolve("IoC.Scope.Current.Set", scope1).execute();
            result1.set(IoC.resolve("foo"));
        });

        Thread t2 = new Thread(() -> {
            Map<String, Function<Object[], Object>> scope2 = IoC.resolve("IoC.Scope.Create");
            scope2.put("foo", args -> "thread2");
            IoC.<Command>resolve("IoC.Scope.Current.Set", scope2).execute();
            result2.set(IoC.resolve("foo"));
        });


        t1.start();
        t2.start();
        t1.join();
        t2.join();


        assertEquals("thread1", result1.get());
        assertEquals("thread2", result2.get());
    }
}
