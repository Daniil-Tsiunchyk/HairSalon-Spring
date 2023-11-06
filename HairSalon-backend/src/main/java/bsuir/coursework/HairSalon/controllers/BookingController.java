package bsuir.coursework.HairSalon.controllers;

import bsuir.coursework.HairSalon.models.Booking;
import bsuir.coursework.HairSalon.models.dto.BookingDTO;
import bsuir.coursework.HairSalon.services.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@Tag(
  name = "Booking Service",
  description = "Operations for interacting with bookings"
)
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
  public List<BookingDTO> getAllBookings() {
    List<Booking> bookings = bookingService.getAllBookings();
    return bookings.stream().map(BookingDTO::new).collect(Collectors.toList());
  }

  @Operation(
    summary = "Retrieve past bookings",
    description = "Endpoint to get a list of all past bookings"
  )
  @GetMapping("/past")
  public ResponseEntity<List<BookingDTO>> getPastBookings() {
    List<Booking> pastBookings = bookingService.getPastBookings();
    List<BookingDTO> pastBookingDTOs = pastBookings
      .stream()
      .map(BookingDTO::new)
      .collect(Collectors.toList());
    return ResponseEntity.ok(pastBookingDTOs);
  }

  @Operation(
    summary = "Retrieve future bookings",
    description = "Endpoint to get a list of all future bookings"
  )
  @GetMapping("/future")
  public ResponseEntity<List<BookingDTO>> getFutureBookings() {
    List<Booking> futureBookings = bookingService.getFutureBookings();
    List<BookingDTO> futureBookingDTOs = futureBookings
      .stream()
      .map(BookingDTO::new)
      .collect(Collectors.toList());
    return ResponseEntity.ok(futureBookingDTOs);
  }

  @Operation(
    summary = "Retrieve a booking by ID",
    description = "Endpoint to get a booking by its ID",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successful retrieval of a booking"
      ),
      @ApiResponse(responseCode = "404", description = "Booking not found"),
    }
  )
  @GetMapping("/{id}")
  public ResponseEntity<BookingDTO> getBookingById(
    @PathVariable @Parameter(description = "ID of the booking") int id
  ) {
    Optional<Booking> booking = bookingService.getBookingById(id);
    return booking
      .map(b -> ResponseEntity.ok(new BookingDTO(b)))
      .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @Operation(
    summary = "Create a new booking",
    description = "Endpoint to create a new booking",
    responses = {
      @ApiResponse(
        responseCode = "201",
        description = "Successful creation of a booking"
      ),
    }
  )
  @PostMapping
  public ResponseEntity<BookingDTO> createBooking(
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Booking.class),
        examples = @ExampleObject(
          value = "{\"user\": {\"id\": 1}, \"hairService\": {\"id\": 1}, \"barber\": {\"id\": 2}, \"dateTime\": \"2023-10-19T14:30:00Z\", \"status\": \"RESERVED\"}"
        )
      )
    ) @RequestBody @Parameter(
      description = "Booking data to create"
    ) Booking booking
  ) {
    Booking createdBooking = bookingService.createBooking(booking);
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(new BookingDTO(createdBooking));
  }

  @Operation(
    summary = "Update booking information",
    description = "Endpoint to update existing booking information. Allows partial updates.",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successful update of a booking"
      ),
      @ApiResponse(responseCode = "404", description = "Booking not found"),
    }
  )
  @PutMapping("/{id}")
  public ResponseEntity<BookingDTO> updateBooking(
    @PathVariable @Parameter(description = "ID of the booking") int id,
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Booking.class),
        examples = @ExampleObject(
          value = "{\"user\": {\"id\": 1}, \"hairService\": {\"id\": 1}, \"barber\": {\"id\": 2}, \"dateTime\": \"2023-10-19T14:30:00Z\", \"status\": \"RESERVED\"}"
        )
      )
    ) @RequestBody @Parameter(
      description = "Updated booking data"
    ) Booking updatedBooking
  ) {
    Booking updated = bookingService.updateBooking(id, updatedBooking);
    if (updated != null) {
      return ResponseEntity.ok(new BookingDTO(updated));
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
      @ApiResponse(responseCode = "404", description = "Booking not found"),
    }
  )
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteBooking(
    @PathVariable @Parameter(description = "ID of the booking") int id
  ) {
    bookingService.deleteBooking(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(
    summary = "Retrieve all bookings by user",
    description = "Endpoint to get a list of all bookings associated with a user"
  )
  @GetMapping("/user/{userId}")
  public ResponseEntity<List<BookingDTO>> getBookingsByUser(
    @PathVariable @Parameter(description = "ID of the user") int userId
  ) {
    List<Booking> bookings = bookingService.getBookingsByUser(userId);
    List<BookingDTO> bookingDTOs = bookings
      .stream()
      .map(BookingDTO::new)
      .collect(Collectors.toList());
    return ResponseEntity.ok(bookingDTOs);
  }
}
