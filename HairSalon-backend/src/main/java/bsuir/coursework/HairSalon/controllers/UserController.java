package bsuir.coursework.HairSalon.controllers;

import bsuir.coursework.HairSalon.models.User;
import bsuir.coursework.HairSalon.services.UserService;
import bsuir.coursework.HairSalon.utils.PasswordHasher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable int id, @RequestBody User updatedUser) {
        User updated = userService.updateUser(id, updatedUser);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) throws NoSuchAlgorithmException {
        User existingUser = userService.findByUsername(user.getUsername());
        if (existingUser != null) {
            return ResponseEntity.badRequest().body(null);
        }

        String hashedPassword = PasswordHasher.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);

        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }


    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody User user) throws NoSuchAlgorithmException {
        User existingUser = userService.findByUsername(user.getUsername());
        if (existingUser != null) {
            String hashedPassword = PasswordHasher.hashPassword(user.getPassword());
            if (hashedPassword.equals(existingUser.getPassword())) {
                return ResponseEntity.ok(existingUser);
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
}
