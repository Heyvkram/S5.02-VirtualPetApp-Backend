package com.virtualpetapp;
import com.virtualpetapp.controller.PetController;
import com.virtualpetapp.dto.PetDTO;
import com.virtualpetapp.entity.EPetType;
import com.virtualpetapp.service.PetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Collections;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(PetController.class)
public class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PetService petService;

    @MockBean
    private com.virtualpetapp.utils.JwtUtils jwtUtils;
    @MockBean
    private com.virtualpetapp.service.UserDetailsServiceImpl userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void whenCreatePet_asUser_thenReturnsCreated() throws Exception {

        PetDTO petToCreate = new PetDTO(null, "Peluix", EPetType.GATO, 0, 0, 0, null);

        PetDTO createdPet = new PetDTO(1L, "Peluix", EPetType.GATO, 0, 100, 100, "testuser");

        when(petService.createPet(any(PetDTO.class))).thenReturn(createdPet);

        mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(petToCreate))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Peluix"))
                // ===== CANVI: Comprovem els nous atributs =====
                .andExpect(jsonPath("$.type").value("GATO"))
                .andExpect(jsonPath("$.happiness").value(100))
                .andExpect(jsonPath("$.ownerUsername").value("testuser"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void whenGetAllPets_asUser_thenReturnOnlyOwnPets() throws Exception {

        PetDTO myPet = new PetDTO(1L, "Peluix", EPetType.GATO, 10, 80, 90, "testuser");
        when(petService.getAllPets()).thenReturn(Collections.singletonList(myPet));

        mockMvc.perform(get("/api/pets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Peluix"))
                .andExpect(jsonPath("$[0].ownerUsername").value("testuser"));
    }

    @Test
    void whenAccessPets_withoutAuth_thenReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/pets"))
                .andExpect(status().isUnauthorized());
    }
}
