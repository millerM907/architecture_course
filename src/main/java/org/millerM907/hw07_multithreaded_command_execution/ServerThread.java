package org.millerM907.hw07_multithreaded_command_execution;

import org.millerM907.hw05_ioc_container.base.Command;
import org.millerM907.hw05_ioc_container.impl.ioc.IoC;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;

public class ServerThread {
    private final BlockingQueue<Command> queue;
    private final Thread thread;
    private volatile Runnable behaviour;
    private volatile boolean stop = false;

    public ServerThread(BlockingQueue<Command> queue) {
        this.queue = Objects.requireNonNull(queue, "queue");

        this.behaviour = () -> {
            Command cmd = null;
            try {
                cmd = queue.take();
                cmd.execute();
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                IoC.<Command>resolve("ExceptionHandler", cmd, e).execute();
            }
        };

        this.thread = new Thread(() -> {
            try {
                // you can add some before-action logic here (e.g., logging, setup, signaling start)
                while (!stop && !Thread.currentThread().isInterrupted()) {
                    behaviour.run();
                }
            } finally {
                // you can add some after-action logic here (e.g., cleanup, signaling stop)
            }
        }, "server-thread");
        this.thread.setDaemon(false);
    }

    public void start() {
        thread.start();
    }

    public void stop() {
        stop = true;
        thread.interrupt();
    }

    public void join() throws InterruptedException {
        thread.join();
    }

    public void updateBehaviour(Runnable newBehaviour) {
        this.behaviour = Objects.requireNonNull(newBehaviour, "newBehaviour");
    }

    public Runnable getBehaviour() {
        return behaviour;
    }

    public BlockingQueue<Command> getQueue() {
        return queue;
    }
}
