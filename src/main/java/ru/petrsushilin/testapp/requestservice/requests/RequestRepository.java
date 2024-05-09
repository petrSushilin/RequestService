package ru.petrsushilin.testapp.requestservice.requests;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.petrsushilin.testapp.requestservice.requests.enums.Stage;

import java.util.Optional;

/**
 * @author Petr Sushilin
 * @version 1.0
 * @since 05.05.2024
 */
@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    @Query(value = "SELECT r FROM Request r JOIN FETCH r.user u WHERE u.id = :userID",
            countQuery = "SELECT count(r) FROM Request r WHERE r.user.id = :userID")
    Page<Request> findRequestsByUserID(@Param("userID") Long userID, Pageable pageable);

    Page<Request> findRequestsByStage(Stage stage, Pageable pageable);

    @Query("SELECT r FROM Request r JOIN r.user u WHERE " +
            "(u.name = :namePart1 AND u.surname = :namePart2) OR (u.name = :namePart2 AND u.surname = :namePart1)")
    Page<Request> findRequestsByPersonName(@Param("namePart1") String namePart1, @Param("namePart2") String namePart2, Pageable pageable);

    @Query("SELECT r FROM Request r JOIN FETCH r.user u WHERE r.id = :requestID")
    Optional<Request> findByIdForUpdate(Long requestID);
}
