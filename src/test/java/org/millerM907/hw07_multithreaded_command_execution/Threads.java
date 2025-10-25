package org.millerM907.hw07_multithreaded_command_execution;

import java.util.Optional;

public class Threads {
    private Threads() {}

    static Optional<Thread> findByName(String name) {
        return Thread.getAllStackTraces().keySet()
                .stream()
                .filter(t -> t.getName().equals(name) && t.isAlive())
                .findFirst();
    }

    static void sleepQuiet(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }
}
