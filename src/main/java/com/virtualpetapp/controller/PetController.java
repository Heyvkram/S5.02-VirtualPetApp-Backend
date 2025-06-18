package com.virtualpetapp.controller;

import com.virtualpetapp.dto.PetDTO;
import com.virtualpetapp.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
@Tag(name = "Pet Management", description = "Endpoints for creating, viewing, and interacting with pets")
@SecurityRequirement(name = "bearerAuth")
public class PetController {

    @Autowired
    private PetService petService;

    @Operation(summary = "Create a new pet", description = "Creates a new pet for the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pet created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data provided"),
            @ApiResponse(responseCode = "403", description = "Access forbidden")
    })
    @PostMapping
    public ResponseEntity<PetDTO> createPet(@Valid @RequestBody PetDTO petDTO) {
        PetDTO newPet = petService.createPet(petDTO);
        return new ResponseEntity<>(newPet, HttpStatus.CREATED);
    }

    @Operation(summary = "Get user's pets", description = "Returns a list of all pets belonging to the authenticated user.")
    @GetMapping
    public ResponseEntity<List<PetDTO>> getAllPets() {
        List<PetDTO> pets = petService.getAllPets();
        return ResponseEntity.ok(pets);
    }

    @Operation(summary = "Feed a pet", description = "Decreases the pet's hunger and increases its energy.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pet fed successfully"),
            @ApiResponse(responseCode = "404", description = "Pet not found")
    })
    @PostMapping("/{id}/feed")
    public ResponseEntity<PetDTO> feedPet(@PathVariable Long id) {
        PetDTO updatedPet = petService.feedPet(id);
        return ResponseEntity.ok(updatedPet);
    }

    @Operation(summary = "Play with a pet", description = "Increases the pet's happiness in exchange for energy and hunger.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Played with pet successfully"),
            @ApiResponse(responseCode = "404", description = "Pet not found")
    })
    @PostMapping("/{id}/play")
    public ResponseEntity<PetDTO> playWithPet(@PathVariable Long id) {
        PetDTO updatedPet = petService.playWithPet(id);
        return ResponseEntity.ok(updatedPet);
    }

    @Operation(summary = "Delete a pet", description = "Deletes a pet by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pet deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Pet not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletePet(@PathVariable Long id) {
        petService.deletePet(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}