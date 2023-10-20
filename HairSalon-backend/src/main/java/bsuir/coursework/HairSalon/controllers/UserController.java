package bsuir.coursework.HairSalon.controllers;

import bsuir.coursework.HairSalon.models.User;
import bsuir.coursework.HairSalon.services.UserService;
import bsuir.coursework.HairSalon.utils.PasswordHasher;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Operations for interacting with users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Retrieve all users",
            description = "Endpoint to get a list of all users"
    )
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @Operation(
            summary = "Retrieve a user by ID",
            description = "Endpoint to get a user by their ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful retrieval of a user"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found"
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(
            @PathVariable @Parameter(description = "ID of the user") int id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create a new user",
            description = "Endpoint to create a new user",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Successful creation of a user"
                    )
            }
    )
    @PostMapping
    public ResponseEntity<User> createUser(
            @RequestBody @Parameter(description = "User data to create") User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @Operation(
            summary = "Update user information",
            description = "Endpoint to update existing user information",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful update of a user"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found"
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable @Parameter(description = "ID of the user") int id,
            @RequestBody @Parameter(description = "Updated user data") User updatedUser) throws NoSuchAlgorithmException {
        User updated = userService.updateUser(id, updatedUser);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Delete a user",
            description = "Endpoint to delete a user by their ID",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Successful deletion of a user"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found"
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable @Parameter(description = "ID of the user") int id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Register a user",
            description = "Endpoint to register a new user",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Successful registration of a user"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request - User with the same username already exists"
                    )
            }
    )
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(
            @RequestBody @Parameter(description = "User data for registration") User user) throws NoSuchAlgorithmException {
        User existingUser = userService.findByUsername(user.getUsername());
        if (existingUser != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        String hashedPassword = PasswordHasher.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);

        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @Operation(
            summary = "Login a user",
            description = "Endpoint to log in a user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful login of a user"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - Incorrect username or password"
                    )
            }
    )
    @PostMapping("/login")
    public ResponseEntity<User> loginUser(
            @RequestBody @Parameter(description = "User data for login") User user) throws NoSuchAlgorithmException {
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
