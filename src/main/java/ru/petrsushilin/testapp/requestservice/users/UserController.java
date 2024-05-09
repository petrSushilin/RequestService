package ru.petrsushilin.testapp.requestservice.users;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The controller for handling admin methods for manage users.
 * This .class using for:
 * - searching list of all users.
 * - searching all users by full name or some part.
 * - promote user to operator, user role remains.
 *
 * @author Petr Sushilin
 * @version 1.0
 * @since 05.05.2024
 */
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    /**
     * Handle using for searching list of all users.
     * Required Role: Admin.
     * Required HTTP method: GET.
     * Correct response status code: 200 OK.
     * @return ResponseEntity {@link List} of {@link User}
     */
    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/list")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getUsers());
    }

    /**
     * Handle using for searching all users by full name or some part.
     * It works same with name/surname and surname/name formats and with only part of full name by the way it have worse performance.
     * Required Role: Admin.
     * Required HTTP method: GET.
     * Correct response status code: 200 OK.
     * @param name consists of {@link String} as full name or some part of it.
     * @return ResponseEntity {@link List} of {@link User}.
     */
    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/{name}")
    public ResponseEntity<List<?>> getUserByName(@PathVariable String name) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getUserByName(name));
    }

    /**
     * Handle using for promote user to operator, user role remains.
     * Required Role: Admin.
     * Required HTTP method: POST.
     * Correct response status code: 200 OK.
     * @param userID consists of {@link Long}.
     * @return ResponseEntity {@link User}
     */
    @Secured("ROLE_ADMIN")
    @PostMapping(value = "/{userID}")
    public ResponseEntity<?> addUserOperatorRole(@PathVariable Long userID) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.promoteToOperator(userID));
    }
}
