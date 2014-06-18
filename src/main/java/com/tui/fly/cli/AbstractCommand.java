package com.tui.fly.cli;

abstract class AbstractCommand implements Command {

    protected String command;

    @Override
    public final void setBeanName(String name) {
        command = name;
    }
}
