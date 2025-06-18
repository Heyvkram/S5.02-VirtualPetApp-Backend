package com.virtualpetapp.controller;

import com.virtualpetapp.dto.UserAdminViewDTO;
import com.virtualpetapp.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Administration", description = "Endpoints for user and pet management by an administrator.")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Operation(summary = "Get all users", description = "Returns a list of all users in the system with their respective pets.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users successfully retrieved"),
            @ApiResponse(responseCode = "403", description = "Access denied (not an administrator)")
    })
    @GetMapping("/users")
    public ResponseEntity<List<UserAdminViewDTO>> getAllUsers() {
        List<UserAdminViewDTO> users = adminService.getAllUsersWithPets();
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Delete a user", description = "Deletes a user and all their pets by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied (not an administrator)"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/users/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}