package com.tui.fly;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@ActiveProfiles("database")
@TestPropertySource(properties = {"liquibase.enabled=true", "liquibase.changeLog=classpath:liquibase.xml"})
public class DatabaseAnnotationConfigTest extends AbstractConfigTest {
}
