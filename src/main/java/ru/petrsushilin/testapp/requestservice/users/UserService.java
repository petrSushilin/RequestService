package ru.petrsushilin.testapp.requestservice.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.petrsushilin.testapp.requestservice.global.exceptions.IdentifierMismatchException;
import ru.petrsushilin.testapp.requestservice.users.enums.Role;

import java.util.List;

/**
 * @author Petr Sushilin
 * @version 1.0
 * @since 05.05.2024
 */
@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<User> getUserByName(String fullName) {
        String[] nameParts = fullName.split(" ");
        String namePart1 = nameParts[0];
        String namePart2 = nameParts.length > 1 ? nameParts[1] : "";

        return userRepository.findByName(namePart1, namePart2);
    }

    @Transactional(rollbackFor = Exception.class)
    public User promoteToOperator(Long userID) {
        User user = userRepository
                .findById(userID)
                .orElseThrow(() -> new IdentifierMismatchException("User with ID " + userID + " not found"));

        user.getRoles().add(Role.OPERATOR);

        return userRepository.save(user);
    }
}
