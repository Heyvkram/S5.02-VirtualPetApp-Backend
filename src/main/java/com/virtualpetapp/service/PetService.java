package com.virtualpetapp.service;
import com.virtualpetapp.dto.PetDTO;
import com.virtualpetapp.entity.Pet;
import com.virtualpetapp.entity.User;
import com.virtualpetapp.exceptions.PetNotFoundException;
import com.virtualpetapp.exceptions.UnauthorizedException;
import com.virtualpetapp.repository.PetRepository;
import com.virtualpetapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UserRepository userRepository;

    private PetDTO convertToDto(Pet pet) {
        return new PetDTO(pet.getId(), pet.getName(), pet.getType(), pet.getHunger(), pet.getEnergy(), pet.getHappiness(), pet.getOwner().getUsername());
    }


    @Transactional
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public PetDTO createPet(PetDTO petDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User owner = userRepository.findByUsername(currentUserName)
                .orElseThrow(() -> new UsernameNotFoundException("Usuari no trobat"));

        Pet newPet = new Pet(petDTO.name(), petDTO.type(), owner);
        Pet savedPet = petRepository.save(newPet);
        return convertToDto(savedPet);
    }


    @Transactional
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public PetDTO feedPet(Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new PetNotFoundException("Mascota no trobada amb id: " + petId));
        checkAuthorization(pet.getOwner().getUsername());


        pet.setHunger(Math.max(0, pet.getHunger() - 50));

        pet.setEnergy(Math.min(100, pet.getEnergy() + 50));

        Pet updatedPet = petRepository.save(pet);
        return convertToDto(updatedPet);
    }


    @Transactional
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public PetDTO playWithPet(Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new PetNotFoundException("Mascota no trobada amb id: " + petId));
        checkAuthorization(pet.getOwner().getUsername());


        pet.setHappiness(Math.min(100, pet.getHappiness() + 50));

        pet.setEnergy(Math.max(0, pet.getEnergy() - 50));

        pet.setHunger(Math.min(100, pet.getHunger() + 50));

        Pet updatedPet = petRepository.save(pet);
        return convertToDto(updatedPet);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<PetDTO> getAllPets() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            return petRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
        } else {
            User user = userRepository.findByUsername(currentUserName).orElseThrow(() -> new UsernameNotFoundException("Usuari no trobat"));
            return petRepository.findByOwnerId(user.getId()).stream().map(this::convertToDto).collect(Collectors.toList());
        }
    }

    @Transactional
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public void deletePet(Long petId) {
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new PetNotFoundException("Mascota no trobada amb id: " + petId));
        checkAuthorization(pet.getOwner().getUsername());
        petRepository.delete(pet);
    }

    private void checkAuthorization(String ownerUsername) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !currentUserName.equals(ownerUsername)) {
            throw new UnauthorizedException("No tens permís per realitzar aquesta acció en aquesta mascota.");
        }
    }

}