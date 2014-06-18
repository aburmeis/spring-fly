package com.tui.fly;

import com.tui.fly.cli.Command;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

@Component
public class Application implements Runnable, InitializingBean {

    public static void main(String... args) {
        boolean useXml = false;
        for (String arg : args) {
            if ("-xml".equals(arg)) {
                useXml = true;
            } else {
                System.err.println("Usage: [-xml]");
            }
        }
        ConfigurableApplicationContext context = useXml ? createXmlContext() : createAnnoationContext();
        context.refresh();
        Application application = context.getBean(Application.class);
        application.run();
        context.close();
    }

    private static ConfigurableApplicationContext createAnnoationContext() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(Config.class);
        return context;
    }

    private static ConfigurableApplicationContext createXmlContext() {
        GenericXmlApplicationContext context = new GenericXmlApplicationContext();
        context.load(new ClassPathResource("context.xml"));
        return context;
    }

    @Autowired
    private ApplicationContext context;

    Map<String, Command> commands;

    @Override
    public void afterPropertiesSet() {
        commands = context.getBeansOfType(Command.class);
    }

    @Override
    public void run() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while (true) {
            try {
                line = in.readLine();
                if (line == null) {
                    return;
                }
                String[] words = line.split(" +");
                if (words.length > 0) {
                    Command command = commands.get(words[0]);
                    if (command == null) {
                        System.err.println("unknown command " + words[0]);
                        System.out.println("Commands: " + commands.keySet());
                    } else {
                        try {
                            System.out.println(command.execute(words));
                        } catch (Exception e) {
                            System.err.println("Error executing command " + words[0] + ": " + e.getMessage());
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
