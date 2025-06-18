package com.virtualpetapp.dto;

import com.virtualpetapp.entity.EPetType;

public record PetAdminViewDTO(Long id, String name, EPetType type) {}
