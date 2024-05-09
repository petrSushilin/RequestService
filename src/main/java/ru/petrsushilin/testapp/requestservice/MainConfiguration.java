package ru.petrsushilin.testapp.requestservice;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.petrsushilin.testapp.requestservice.requests.RequestConfiguration;
import ru.petrsushilin.testapp.requestservice.users.UserConfiguration;

/**
 * @author Petr Sushilin
 * @version 1.0
 * @since 05.05.2024
 */
@Import({
        RequestConfiguration.class,
        UserConfiguration.class,
        HibernateConfig.class
})
@Configuration
public class MainConfiguration {
}
