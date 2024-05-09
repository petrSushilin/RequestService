package ru.petrsushilin.testapp.requestservice.users;

import jakarta.persistence.*;
import ru.petrsushilin.testapp.requestservice.users.enums.Role;

import java.util.Set;

/**
 * @author Petr Sushilin
 * @version 1.0
 * @since 05.05.2024
 */
@Entity
@Table(name = "users", indexes = {
        @Index(name = "name_index", columnList = "name"),
        @Index(name = "surname_index", columnList = "surname")
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "login", nullable = false, unique = true)
    private String login;

    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<Role> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
