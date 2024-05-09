package ru.petrsushilin.testapp.requestservice.requests;

import ru.petrsushilin.testapp.requestservice.requests.enums.Stage;
import ru.petrsushilin.testapp.requestservice.users.User;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * @author Petr Sushilin
 * @version 1.0
 * @since 05.05.2024
 */
@Entity
@Table(name = "Requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Enumerated(EnumType.STRING)
    @Column(name = "stage", nullable = false)
    private Stage stage;
    @Column(name = "description", nullable = false)
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
