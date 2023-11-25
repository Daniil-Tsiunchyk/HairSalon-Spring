package bsuir.coursework.HairSalon.controllers;

import bsuir.coursework.HairSalon.models.HairService;
import bsuir.coursework.HairSalon.services.HairServiceService;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/hair-services")
@Tag(
  name = "Hair Service",
  description = "Operations for interacting with hair services"
)
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
        description = "Successful retrieval of a hair service"
      ),
      @ApiResponse(
        responseCode = "404",
        description = "Hair service not found"
      ),
    }
  )
  @GetMapping("/{id}")
  public ResponseEntity<HairService> getHairServiceById(
    @PathVariable @Parameter(description = "ID of the hair service") int id
  ) {
    Optional<HairService> hairService = hairServiceService.getHairServiceById(
      id
    );
    return hairService
      .map(ResponseEntity::ok)
      .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @Operation(
    summary = "Create a new hair service",
    description = "Endpoint to create a new hair service",
    responses = {
      @ApiResponse(
        responseCode = "201",
        description = "Successful creation of a hair service"
      ),
    }
  )
  @PostMapping
  public ResponseEntity<HairService> createHairService(
    @RequestBody @Parameter(
      description = "Hair service data to create"
    ) HairService hairService
  ) {
    HairService createdHairService = hairServiceService.createHairService(
      hairService
    );
    return ResponseEntity.status(HttpStatus.CREATED).body(createdHairService);
  }

  @Operation(
    summary = "Update hair service information",
    description = "Endpoint to update existing hair service information",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successful update of a hair service"
      ),
      @ApiResponse(
        responseCode = "404",
        description = "Hair service not found"
      ),
    }
  )
  @PutMapping("/{id}")
  public ResponseEntity<HairService> updateHairService(
    @PathVariable @Parameter(description = "ID of the hair service") int id,
    @RequestBody @Parameter(
      description = "Updated hair service data"
    ) HairService updatedHairService
  ) {
    HairService updated = hairServiceService.updateHairService(
      id,
      updatedHairService
    );
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
      ),
    }
  )
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteHairService(
    @PathVariable @Parameter(description = "ID of the hair service") int id
  ) {
    hairServiceService.deleteHairService(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(
    summary = "Add an image to the hair service",
    description = "Endpoint to add an image to the hair service"
  )
  @PostMapping(
    value = "/{id}/addImage",
    consumes = MediaType.MULTIPART_FORM_DATA_VALUE
  )
  public ResponseEntity<HairService> addImageToHairService(
    @PathVariable @Parameter(description = "ID of the hair service") Long id,
    @RequestPart(value = "imageFile") @Parameter(
      description = "Image file to upload",
      content = @Content(
        mediaType = "multipart/form-data",
        schema = @Schema(type = "string", format = "binary")
      )
    ) MultipartFile imageFile
  ) {
    HairService updatedHairService = hairServiceService.addImageToHairService(
      id,
      imageFile
    );
    return ResponseEntity.ok(updatedHairService);
  }

  @Operation(
    summary = "Remove an image from the hair service",
    description = "Endpoint to remove an image from the hair service"
  )
  @DeleteMapping("/{id}/removeImage")
  public ResponseEntity<HairService> removeImageFromHairService(
    @PathVariable @Parameter(description = "ID of the hair service") Long id,
    @RequestParam(name = "imageUrl") String imageUrl
  ) {
    HairService updatedHairService = hairServiceService.removeImageFromHairService(
      id,
      imageUrl
    );
    return ResponseEntity.ok(updatedHairService);
  }

  @Operation(
    summary = "Get a specific image of an hair service",
    description = "Endpoint to retrieve a specific image of an hair service by its URL"
  )
  @GetMapping("/getImage")
  public ResponseEntity<byte[]> getImageOfHairService(
    @RequestParam(name = "imageUrl") String imageUrl
  ) {
    byte[] imageBytes = hairServiceService.getImageOfHairService(imageUrl);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.IMAGE_JPEG);
    return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
  }

  @Operation(
    summary = "Get all images of an hair service",
    description = "Endpoint to retrieve all images of an hair service"
  )
  @GetMapping("/{id}/getAllImages")
  public ResponseEntity<byte[]> getAllImagesOfHairService(
    @PathVariable @Parameter(description = "ID of the hair service") Long id
  ) {
    byte[] allImagesBytes = hairServiceService.getAllImagesOfHairService(id);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.IMAGE_JPEG);
    return new ResponseEntity<>(allImagesBytes, headers, HttpStatus.OK);
  }
}
