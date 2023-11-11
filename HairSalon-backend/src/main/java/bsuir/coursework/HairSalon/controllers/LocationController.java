package bsuir.coursework.HairSalon.controllers;

import bsuir.coursework.HairSalon.models.Location;
import bsuir.coursework.HairSalon.services.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/locations")
@Tag(name = "Location", description = "Operations for interacting with locations")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @Operation(
            summary = "Retrieve all locations",
            description = "Endpoint to get a list of all locations"
    )
    @GetMapping
    public ResponseEntity<List<Location>> getAllLocations() {
        List<Location> locations = locationService.getAllLocations();
        return ResponseEntity.ok(locations);
    }

    @Operation(
            summary = "Retrieve a location by ID",
            description = "Endpoint to get a location by its ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful retrieval of a location"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Location not found"
                    ),
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Location> getLocationById(
            @PathVariable @Parameter(description = "ID of the location") int id
    ) {
        Optional<Location> location = locationService.getLocationById(id);
        return location
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create a new location",
            description = "Endpoint to create a new location",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Successful creation of a location"
                    ),
            }
    )
    @PostMapping
    public ResponseEntity<Location> createLocation(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Location.class),
                            examples = @ExampleObject(
                                    value = "{\"latitude\": 12.34, \"longitude\": 56.78}"
                            )
                    )
            ) @RequestBody @Parameter(
                    description = "Location data to create"
            ) Location location
    ) {
        Location createdLocation = locationService.createLocation(location);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLocation);
    }

    @Operation(
            summary = "Delete a location",
            description = "Endpoint to delete location by its ID",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Successful deletion of a location"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Location not found"
                    ),
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(
            @PathVariable @Parameter(description = "ID of the location") int id
    ) {
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }
}
