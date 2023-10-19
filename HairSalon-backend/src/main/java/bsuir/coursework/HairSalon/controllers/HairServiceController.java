package bsuir.coursework.HairSalon.controllers;

import bsuir.coursework.HairSalon.models.HairService;
import bsuir.coursework.HairSalon.services.HairServiceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/hair-services")
@Tag(name = "Hair Service", description = "Operations for interacting with hair services")
public class HairServiceController {
    private final HairServiceService hairServiceService;

    @Autowired
    public HairServiceController(HairServiceService hairServiceService) {
        this.hairServiceService = hairServiceService;
    }

    @Operation(
            summary = "Retrieve all hair services",
            description = "Endpoint to get a list of all hair services"
    )
    @GetMapping
    public ResponseEntity<List<HairService>> getAllHairServices() {
        List<HairService> hairServices = hairServiceService.getAllHairServices();
        return ResponseEntity.ok(hairServices);
    }

    @Operation(
            summary = "Retrieve a hair service by ID",
            description = "Endpoint to get a hair service by its ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful retrieval of a hair service",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = HairService.class),
                                    examples = @ExampleObject(value = "{\"id\": 1, \"name\": \"Haircut\", \"cost\": 25.0}")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Hair service not found"
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<HairService> getHairServiceById(
            @PathVariable @Parameter(description = "ID of the hair service") int id) {
        Optional<HairService> hairService = hairServiceService.getHairServiceById(id);
        return hairService.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create a new hair service",
            description = "Endpoint to create a new hair service",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Successful creation of a hair service",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = HairService.class),
                                    examples = @ExampleObject(value = "{\"id\": 1, \"name\": \"New Service\", \"cost\": 30.0}")
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<HairService> createHairService(
            @RequestBody @Parameter(description = "Hair service data to create") HairService hairService) {
        HairService createdHairService = hairServiceService.createHairService(hairService);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdHairService);
    }

    @Operation(
            summary = "Update hair service information",
            description = "Endpoint to update existing hair service information",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful update of a hair service",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = HairService.class),
                                    examples = @ExampleObject(value = "{\"id\": 1, \"name\": \"Updated Service\", \"cost\": 35.0}")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Hair service not found"
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<HairService> updateHairService(
            @PathVariable @Parameter(description = "ID of the hair service") int id,
            @RequestBody @Parameter(description = "Updated hair service data") HairService updatedHairService) {
        HairService updated = hairServiceService.updateHairService(id, updatedHairService);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Delete a hair service",
            description = "Endpoint to delete a hair service by its ID",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Successful deletion of a hair service"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Hair service not found"
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHairService(
            @PathVariable @Parameter(description = "ID of the hair service") int id) {
        hairServiceService.deleteHairService(id);
        return ResponseEntity.noContent().build();
    }
}
