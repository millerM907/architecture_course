package org.millerM907.hw07_multithreaded_command_execution.commands;

import org.millerM907.hw05_ioc_container.base.Command;
import org.millerM907.hw07_multithreaded_command_execution.ServerThread;

public class SoftStopCommand implements Command {

    private final ServerThread st;

    public SoftStopCommand(ServerThread st) {
        this.st = st;
    }

    @Override
    public void execute() {
        Runnable oldBehavior = st.getBehaviour();
        st.updateBehaviour(() -> {
            if (!st.getQueue().isEmpty()) {
                oldBehavior.run();
            } else {
                st.stop();
            }
        });
    }
}
