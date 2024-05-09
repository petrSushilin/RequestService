package ru.petrsushilin.testapp.requestservice.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Petr Sushilin
 * @version 1.0
 * @since 05.05.2024
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE " +
            "(u.name = :namePart1 AND u.surname = :namePart2) OR (u.name = :namePart2 AND u.surname = :namePart1)")
    List<User> findByName(String namePart1, String namePart2);
}
