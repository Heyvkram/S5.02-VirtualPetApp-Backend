package com.virtualpetapp.dto;

import java.util.List;

public record UserAdminViewDTO(Long id, String username, List<PetAdminViewDTO> pets) {}

