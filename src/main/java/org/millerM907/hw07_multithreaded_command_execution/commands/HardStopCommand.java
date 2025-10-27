package org.millerM907.hw07_multithreaded_command_execution.commands;

import org.millerM907.hw05_ioc_container.base.Command;
import org.millerM907.hw07_multithreaded_command_execution.ServerThread;

public class HardStopCommand implements Command {

    private final ServerThread st;

    public HardStopCommand(ServerThread st) {
        this.st = st;
    }

    @Override
    public void execute() {
        st.stop();
    }
}
