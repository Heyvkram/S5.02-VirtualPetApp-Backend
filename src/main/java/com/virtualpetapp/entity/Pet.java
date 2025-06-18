package com.virtualpetapp.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pets")
@Getter
@Setter
@NoArgsConstructor
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EPetType type;

    @Min(0)
    @Max(100)
    private int hunger;

    @Min(0)
    @Max(100)
    private int energy;

    @Min(0)
    @Max(100)
    private int happiness;

    // ===== ANOTACIÓ AFEGIDA AQUÍ =====
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User owner;

    public Pet(String name, EPetType type, User owner) {
        this.name = name;
        this.type = type;
        this.owner = owner;
        this.hunger = 100;
        this.energy = 0;
        this.happiness = 0;
    }
}