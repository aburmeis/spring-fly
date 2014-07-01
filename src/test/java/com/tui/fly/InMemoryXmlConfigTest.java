package com.tui.fly;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations = "classpath:context.xml")
@ActiveProfiles("memory")
public class InMemoryXmlConfigTest extends AbstractConfigTest {

}
