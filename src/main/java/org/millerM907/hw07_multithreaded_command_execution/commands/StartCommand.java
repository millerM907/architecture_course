package org.millerM907.hw07_multithreaded_command_execution.commands;

import org.millerM907.hw05_ioc_container.base.Command;
import org.millerM907.hw07_multithreaded_command_execution.ServerThread;

public class StartCommand implements Command {

    private final ServerThread st;

    public StartCommand(ServerThread st) {
        this.st = st;
    }

    @Override
    public void execute() {
        st.start();
    }
}
