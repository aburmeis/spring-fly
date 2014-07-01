package com.tui.fly;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations = "classpath:context.xml")
@ActiveProfiles("relational")
public class DatabaseXmlConfigTest extends AbstractConfigTest {

}
