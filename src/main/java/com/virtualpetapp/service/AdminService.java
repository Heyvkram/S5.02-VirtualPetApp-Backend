package com.virtualpetapp.service;
import com.virtualpetapp.dto.PetAdminViewDTO;
import com.virtualpetapp.dto.UserAdminViewDTO;
import com.virtualpetapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;


    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserAdminViewDTO> getAllUsersWithPets() {
        return userRepository.findAll().stream()
                .map(user -> new UserAdminViewDTO(
                        user.getId(),
                        user.getUsername(),
                        user.getPets().stream()
                                .map(pet -> new PetAdminViewDTO(pet.getId(), pet.getName(), pet.getType()))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }


    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
