package com.tui.fly;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = Config.class)
@ActiveProfiles("relational")
public class DatabaseAnnotationConfigTest extends AbstractConfigTest {

}
