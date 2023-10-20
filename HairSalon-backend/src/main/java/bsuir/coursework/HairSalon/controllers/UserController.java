package bsuir.coursework.HairSalon.controllers;

import bsuir.coursework.HairSalon.models.User;
import bsuir.coursework.HairSalon.services.UserService;
import bsuir.coursework.HairSalon.utils.PasswordHasher;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Operation(
            summary = "Generate and send reset code for Confirmed Email",
            description = "Generate a reset code and send it to the user's email if the email is confirmed.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Reset code sent successfully"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Error generating reset code"
                    )
            }
    )
    @PostMapping("/reset-code")
    public ResponseEntity<Map<String, String>> generateResetCode(
            @Parameter(description = "User email. The email must be confirmed to generate the reset code.")
            @RequestParam String email) {
        String resetCode = userService.generateAndSendResetCode(email);
        Map<String, String> response = new HashMap<>();
        if (resetCode != null) {
            response.put("message", "Reset code sent successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "Error sending reset code");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @Operation(
            summary = "Check reset code",
            description = "Check if the provided reset code exists for the user.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Reset code exists"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Reset code does not exist"
                    )
            }
    )
    @GetMapping("/reset-code/check")
    public ResponseEntity<Boolean> checkResetCode(
            @Parameter(description = "User Email") @RequestParam String email,
            @Parameter(description = "Reset code") @RequestParam String resetCode) {
        boolean codeExists = userService.checkResetCode(email, resetCode);
        if (codeExists) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }
    }

    @Operation(
            summary = "Reset user password",
            description = "Reset user password using the provided reset code.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Password reset successful"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid reset code"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Reset code not found"
                    )
            }
    )
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @Parameter(description = "User Email") @RequestParam String email,
            @Parameter(description = "Reset code") @RequestParam String resetCode,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class),
                            examples = @ExampleObject(value = "{\"password\": \"new_password\"}")
                    )
            ) @RequestBody @Parameter(description = "New password") User user) throws NoSuchAlgorithmException {
        boolean resetSuccessful = userService.resetPassword(email, resetCode, user.getPassword());
        if (resetSuccessful) {
            String jsonResponse = "{\"resetCode\":\"" + resetCode + "\"}";
            return ResponseEntity.ok(jsonResponse);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid reset code");
        }
    }
}
