package bsuir.coursework.HairSalon.models;

import lombok.*;
import jakarta.persistence.*;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "reset_codes")
@Schema(description = "Entity representing a reset code for password reset")
public class ResetCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier for the reset code", example = "1", hidden = true)
    @Column(name = "reset_code_id")
    private Long resetCodeID;

    @Column(nullable = false)
    @Schema(description = "Reset code", example = "A1B2C3D4")
    private String code;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @Schema(description = "User associated with this reset code")
    private User user;

    @Column(nullable = false)
    @Schema(description = "Creation time of the reset code")
    private LocalDateTime creationTime;
}