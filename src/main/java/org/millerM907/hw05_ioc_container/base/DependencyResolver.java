package org.millerM907.hw05_ioc_container.base;

public interface DependencyResolver {

    <T> T resolve(String key, Object... args);
}
