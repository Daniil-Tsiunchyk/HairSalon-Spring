package bsuir.coursework.HairSalon.controllers;

import bsuir.coursework.HairSalon.models.DiscountCard;
import bsuir.coursework.HairSalon.models.dto.DiscountCardDTO;
import bsuir.coursework.HairSalon.services.DiscountCardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/discount-cards")
@Tag(
  name = "Discount Cards Service",
  description = "Operations for interacting with discount services"
)
public class DiscountCardController {

  private final DiscountCardService discountCardService;

  @Autowired
  public DiscountCardController(DiscountCardService discountCardService) {
    this.discountCardService = discountCardService;
  }

  @Operation(
    summary = "Retrieve all discount cards",
    description = "Endpoint to get a list of all discount cards"
  )
  @GetMapping
  public List<DiscountCardDTO> getAllDiscountCards() {
    return discountCardService.getAllDiscountCards();
  }

  @Operation(
    summary = "Retrieve a discount card by ID",
    description = "Endpoint to get a discount card by its ID",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successful retrieval of a discount card"
      ),
      @ApiResponse(
        responseCode = "404",
        description = "Discount card not found"
      ),
    }
  )
  @GetMapping("/{id}")
  public ResponseEntity<DiscountCardDTO> getDiscountCardById(
    @PathVariable @Parameter(description = "ID of the discount card") int id
  ) {
    Optional<DiscountCard> discountCard = discountCardService.getDiscountCardById(
      id
    );
    return discountCard
      .map(dc -> ResponseEntity.ok(new DiscountCardDTO(dc)))
      .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @Operation(
    summary = "Create a new discount card",
    description = "Endpoint to create a new discount card",
    responses = {
      @ApiResponse(
        responseCode = "201",
        description = "Successful creation of a discount card"
      ),
    }
  )
  @PostMapping
  public ResponseEntity<DiscountCard> createDiscountCard(
    @RequestBody @Parameter(
      description = "Discount card data to create"
    ) DiscountCard discountCard
  ) {
    DiscountCard createdDiscountCard = discountCardService.createDiscountCard(
      discountCard
    );
    return ResponseEntity.status(HttpStatus.CREATED).body(createdDiscountCard);
  }

  @Operation(
    summary = "Update discount card information",
    description = "Endpoint to update existing discount card information",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successful update of a discount card"
      ),
      @ApiResponse(
        responseCode = "404",
        description = "Discount card not found"
      ),
    }
  )
  @PutMapping("/{id}")
  public ResponseEntity<DiscountCardDTO> updateDiscountCard(
    @PathVariable @Parameter(description = "ID of the discount card") int id,
    @RequestBody @Parameter(
      description = "Updated discount card data"
    ) DiscountCard updatedDiscountCard
  ) {
    DiscountCard updated = discountCardService.updateDiscountCard(
      id,
      updatedDiscountCard
    );
    if (updated != null) {
      return ResponseEntity.ok(new DiscountCardDTO(updated));
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @Operation(
    summary = "Delete a discount card",
    description = "Endpoint to delete a discount card by its ID",
    responses = {
      @ApiResponse(
        responseCode = "204",
        description = "Successful deletion of a discount card"
      ),
      @ApiResponse(
        responseCode = "404",
        description = "Discount card not found"
      ),
    }
  )
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteDiscountCard(
    @PathVariable @Parameter(description = "ID of the discount card") int id
  ) {
    discountCardService.deleteDiscountCard(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(
    summary = "Retrieve a discount card by user ID",
    description = "Endpoint to get a discount card by the user's ID",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successful retrieval of a discount card"
      ),
      @ApiResponse(
        responseCode = "404",
        description = "Discount card not found"
      ),
    }
  )
  @GetMapping("/user/{userId}")
  public ResponseEntity<DiscountCardDTO> getDiscountCardByUserId(
    @PathVariable @Parameter(description = "ID of the user") int userId
  ) {
    DiscountCardDTO discountCard = discountCardService.getDiscountCardByUserId(
      userId
    );
    if (discountCard != null) {
      return ResponseEntity.ok(discountCard);
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}
