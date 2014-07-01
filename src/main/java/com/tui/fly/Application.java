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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.springframework.util.StringUtils.collectionToDelimitedString;

@Component
public class Application implements Runnable, InitializingBean {

    static final Set<String> VALID_PROFILES = new HashSet<>(asList("memory", "relational"));

    public static void main(String... args) {
        boolean useXml = false;
        String profile = "memory";
        for (String arg : args) {
            if ("-xml".equals(arg)) {
                useXml = true;
            } else if (VALID_PROFILES.contains(arg)) {
                profile = arg;
            } else {
                System.err.println("Usage: [-xml] " + collectionToDelimitedString(VALID_PROFILES, "|", "[", "]"));
            }
        }
        ConfigurableApplicationContext context = useXml ? createXmlContext(profile) : createAnnoationContext(profile);
        context.refresh();
        Application application = context.getBean(Application.class);
        application.run();
        context.close();
    }

    private static ConfigurableApplicationContext createAnnoationContext(String profile) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.getEnvironment().setActiveProfiles(profile);
        context.register(Config.class);
        return context;
    }

    private static ConfigurableApplicationContext createXmlContext(String profile) {
        GenericXmlApplicationContext context = new GenericXmlApplicationContext();
        context.getEnvironment().setActiveProfiles(profile);
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
