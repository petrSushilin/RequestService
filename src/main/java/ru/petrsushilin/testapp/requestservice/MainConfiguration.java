package ru.petrsushilin.testapp.requestservice;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Petr Sushilin
 * @version 1.0
 * @since 05.05.2024
 */
@Import({
        HibernateConfig.class
})
@Configuration
public class MainConfiguration {
}
