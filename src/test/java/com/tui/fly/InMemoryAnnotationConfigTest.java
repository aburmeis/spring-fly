package com.tui.fly;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@ActiveProfiles("memory")
@TestPropertySource(properties = "liquibase.enabled=false")
public class InMemoryAnnotationConfigTest extends AbstractConfigTest {
}
