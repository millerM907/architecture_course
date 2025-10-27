package org.millerM907.hw07_multithreaded_command_execution;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.millerM907.hw05_ioc_container.base.Command;
import org.millerM907.hw07_multithreaded_command_execution.commands.HardStopCommand;
import org.millerM907.hw07_multithreaded_command_execution.commands.SoftStopCommand;
import org.millerM907.hw07_multithreaded_command_execution.commands.StartCommand;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("ServerThread: start, hard stop, soft stop via command queue")
public class ServerThreadTest extends BaseIocSetupTest {

    private ServerThread st;

    @AfterEach
    void tearDown() throws Exception {
        if (st != null) {
            st.stop();
            st.join();
        }
    }

    @Test
    @DisplayName("Should start server-thread and process queued command when StartCommand is enqueued")
    void givenServerThreadAndWorkInQueue_whenStartCommandIsEnqueued_thenThreadStartsAndProcessesWork() throws Exception {
        BlockingQueue<Command> q = new ArrayBlockingQueue<>(100);
        st = new ServerThread(q);
        CountDownLatch started = new CountDownLatch(1);
        q.add(started::countDown);


        new StartCommand(st).execute();


        assertTrue(started.await(1, TimeUnit.SECONDS),
                "After StartCommand, the thread should begin processing queued commands");
        assertTrue(Threads.findByName("server-thread").isPresent(),
                "The 'server-thread' should be alive after StartCommand");
    }

    @Test
    @DisplayName("Should terminate server-thread when HardStopCommand is enqueued")
    void givenRunningServerThread_whenHardStopCommandIsEnqueued_thenThreadTerminates() throws Exception {
        BlockingQueue<Command> q = new ArrayBlockingQueue<>(100);
        st = new ServerThread(q);
        new StartCommand(st).execute();

        CountDownLatch beforeStopRan = new CountDownLatch(2);
        q.add(beforeStopRan::countDown);
        q.add(beforeStopRan::countDown);


        q.add(new HardStopCommand(st));


        assertTrue(beforeStopRan.await(1, TimeUnit.SECONDS),
                "Pre-stop tasks should run before HardStopCommand terminates the thread");
        st.join();
        assertTrue(Threads.findByName("server-thread").isEmpty(),
                "After HardStopCommand (enqueued), the 'server-thread' should terminate");
    }

    @Test
    @DisplayName("Should finish all queued tasks before stopping when SoftStopCommand is enqueued")
    void givenTasksInQueue_whenSoftStopCommandIsEnqueued_thenThreadStopsAfterDrainingQueue() throws Exception {
        BlockingQueue<Command> q = new ArrayBlockingQueue<>(100);
        st = new ServerThread(q);

        final int tasks = 5;
        CountDownLatch allDone = new CountDownLatch(tasks);
        AtomicInteger executed = new AtomicInteger();

        // put work in the queue
        for (int i = 0; i < tasks; i++) {
            q.add(() -> {
                Threads.sleepQuiet(30); // simulate small work
                executed.incrementAndGet();
                allDone.countDown();
            });
        }

        q.add(new SoftStopCommand(st));


        new StartCommand(st).execute();


        assertTrue(allDone.await(2, TimeUnit.SECONDS),
                "All queued tasks should complete before the thread stops on SoftStopCommand");
        st.join();
        assertEquals(tasks, executed.get(),
                "All tasks must be finished before stopping");
        assertTrue(Threads.findByName("server-thread").isEmpty(),
                "After SoftStopCommand (enqueued), the 'server-thread' should terminate");
    }
}
