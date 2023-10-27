package bsuir.coursework.HairSalon.services;

import bsuir.coursework.HairSalon.models.HairService;
import bsuir.coursework.HairSalon.repositories.HairServiceRepository;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
public class HairServiceService {

  private final HairServiceRepository hairServiceRepository;

  @Autowired
  public HairServiceService(HairServiceRepository hairServiceRepository) {
    this.hairServiceRepository = hairServiceRepository;
  }

  public List<HairService> getAllHairServices() {
    return hairServiceRepository.findAll();
  }

  public Optional<HairService> getHairServiceById(int id) {
    return hairServiceRepository.findById(id);
  }

  public HairService createHairService(HairService hairService) {
    return hairServiceRepository.save(hairService);
  }

  public HairService updateHairService(int id, HairService updatedHairService) {
    Optional<HairService> existingHairService = hairServiceRepository.findById(
      id
    );
    if (existingHairService.isPresent()) {
      HairService hairService = existingHairService.get();
      hairService.setName(updatedHairService.getName());
      hairService.setCost(updatedHairService.getCost());
      return hairServiceRepository.save(hairService);
    } else {
      return null;
    }
  }

  public void deleteHairService(int id) {
    hairServiceRepository.deleteById(id);
  }

  public HairService addImageToHairService(Long id, MultipartFile imageFile) {
    HairService existingHairService = hairServiceRepository
      .findById(Math.toIntExact(id))
      .orElseThrow(this::hairServiceNotFoundException);

    if (imageFile != null && !imageFile.isEmpty()) {
      String fileName = StringUtils.cleanPath(
        Objects.requireNonNull(imageFile.getOriginalFilename())
      );

      String uploadDir = "images/hair_services/";
      String fileDownloadUri = uploadDir + fileName;

      try {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
          Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        imageFile.transferTo(filePath);

        if (existingHairService.getImages() == null) {
          existingHairService.setImages(new ArrayList<>());
        }

        existingHairService.getImages().add(fileDownloadUri);

        return hairServiceRepository.save(existingHairService);
      } catch (IOException e) {
        throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR,
          "Failed to upload image"
        );
      }
    } else {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "Image file is empty"
      );
    }
  }

  public HairService removeImageFromHairService(Long id, String imageUrl) {
    HairService hairServiceEstablishment = hairServiceRepository
      .findById(Math.toIntExact(id))
      .orElseThrow(this::hairServiceNotFoundException);

    List<String> images = hairServiceEstablishment.getImages();

    if (images != null && images.contains(imageUrl)) {
      images.remove(imageUrl);
      hairServiceEstablishment.setImages(images);

      hairServiceRepository.save(hairServiceEstablishment);

      String fileName = StringUtils.getFilename(imageUrl);
      String uploadDir = "images/hair_services/";
      Path filePath = Paths.get(uploadDir).resolve(fileName);

      try {
        Files.deleteIfExists(filePath);
      } catch (IOException e) {
        System.err.println(
          "Ошибка при удалении файла. Попытка удаления пути: " + filePath
        );
        System.err.println("Ошибка: " + e.getMessage());
        throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR,
          "Failed to delete image file"
        );
      }

      return hairServiceEstablishment;
    } else {
      System.err.println("Image not found. Попытка удаления пути: " + imageUrl);
      throw new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "Image not found"
      );
    }
  }

  public byte[] getImageOfHairService(String imageUrl) {
    try {
      Path imagePath = Paths.get(imageUrl);
      return Files.readAllBytes(imagePath);
    } catch (IOException e) {
      throw new ResponseStatusException(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "Failed to retrieve image"
      );
    }
  }

  public byte[] getAllImagesOfHairService(Long id) {
    HairService hairService = getHairServiceById(id);
    List<String> imageUrls = hairService.getImages();
    try {
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      for (String imageUrl : imageUrls) {
        Path imagePath = Paths.get(imageUrl);
        byte[] imageBytes = Files.readAllBytes(imagePath);
        outputStream.write(imageBytes);
      }
      return outputStream.toByteArray();
    } catch (IOException e) {
      throw new ResponseStatusException(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "Failed to retrieve images"
      );
    }
  }

  public HairService getHairServiceById(Long id) {
    return hairServiceRepository
      .findById(Math.toIntExact(id))
      .orElseThrow(() ->
        new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          "Establishment not found"
        )
      );
  }

  private ResponseStatusException hairServiceNotFoundException() {
    return new ResponseStatusException(
      HttpStatus.NOT_FOUND,
      "Hair Service not found"
    );
  }
}
