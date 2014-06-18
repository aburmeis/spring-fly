package com.tui.fly.cli;

import org.springframework.beans.factory.BeanNameAware;

public interface Command extends BeanNameAware {

    public String execute(String... args);
}
