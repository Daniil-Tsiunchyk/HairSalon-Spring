package bsuir.coursework.HairSalon.controllers;

import bsuir.coursework.HairSalon.models.Booking;
import bsuir.coursework.HairSalon.services.BookingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
@Tag(name = "Booking Service", description = "Operations for interacting with bookings")
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Operation(
            summary = "Retrieve all bookings",
            description = "Endpoint to get a list of all bookings"
    )
    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    @Operation(
            summary = "Retrieve a booking by ID",
            description = "Endpoint to get a booking by its ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful retrieval of a booking"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Booking not found"
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(
            @PathVariable @Parameter(description = "ID of the booking") int id) {
        Optional<Booking> booking = bookingService.getBookingById(id);
        return booking.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create a new booking",
            description = "Endpoint to create a new booking",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Successful creation of a booking"
                    )
            }
    )
    @PostMapping
    public ResponseEntity<Booking> createBooking(
            @RequestBody @Parameter(description = "Booking data to create") Booking booking) {
        Booking createdBooking = bookingService.createBooking(booking);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBooking);
    }

    @Operation(
            summary = "Update booking information",
            description = "Endpoint to update existing booking information. Allows partial updates.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful update of a booking"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Booking not found"
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(
            @PathVariable @Parameter(description = "ID of the booking") int id,
            @RequestBody @Parameter(description = "Updated booking data") Booking updatedBooking) {
        Booking updated = bookingService.updateBooking(id, updatedBooking);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Delete a booking",
            description = "Endpoint to delete a booking by its ID",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Successful deletion of a booking"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Booking not found"
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(
            @PathVariable @Parameter(description = "ID of the booking") int id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Retrieve all bookings by user",
            description = "Endpoint to get a list of all bookings associated with a user"
    )
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Booking>> getBookingsByUser(
            @PathVariable @Parameter(description = "ID of the user") int userId) {
        List<Booking> bookings = bookingService.getBookingsByUser(userId);
        return ResponseEntity.ok(bookings);
    }
}
