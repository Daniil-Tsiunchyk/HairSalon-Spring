package bsuir.coursework.HairSalon.controllers;

import bsuir.coursework.HairSalon.models.User;
import bsuir.coursework.HairSalon.services.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/emails")
@Tag(name = "Emails", description = "Operations for interacting with emails")
public class EmailController {

  private final EmailService emailService;

  public EmailController(EmailService emailService) {
    this.emailService = emailService;
  }

  @Operation(
    summary = "Send emails to user with some role",
    description = "Endpoint to send email to users"
  )
  @PostMapping
  public ResponseEntity<Void> sendEmail(
    @RequestParam String subject,
    @RequestParam String text,
    @RequestParam String role
  ) {
    emailService.sendSpam(subject, text, User.UserRole.valueOf(role));
    return ResponseEntity.noContent().build();
  }
}
