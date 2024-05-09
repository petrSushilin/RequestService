package ru.petrsushilin.testapp.requestservice.users;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Petr Sushilin
 * @version 1.0
 * @since 05.05.2024
 */
@Configuration
public class UserConfiguration {
    @Bean
    public UserController userController(UserService userService) {
        return new UserController(userService);
    }

    @Bean
    public UserService userService(UserRepository userRepository) {
        return new UserService(userRepository);
    }
}
