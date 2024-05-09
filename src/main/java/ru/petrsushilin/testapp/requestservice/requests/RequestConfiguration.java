package ru.petrsushilin.testapp.requestservice.requests;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Petr Sushilin
 * @version 1.0
 * @since 05.05.2024
 */
@Configuration
public class RequestConfiguration {
    @Bean
    public RequestController requestController(RequestService requestService) {
        return new RequestController(requestService);
    }

    @Bean
    public RequestService requestService(RequestRepository requestRepository) {
        return new RequestService(requestRepository);
    }
}
