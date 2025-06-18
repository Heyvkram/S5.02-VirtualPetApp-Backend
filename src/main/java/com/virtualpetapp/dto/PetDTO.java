package com.virtualpetapp.dto;

import com.virtualpetapp.entity.EPetType;

public record PetDTO(
        Long id,
        String name,
        EPetType type,
        int hunger,
        int energy,
        int happiness,
        String ownerUsername
) {}