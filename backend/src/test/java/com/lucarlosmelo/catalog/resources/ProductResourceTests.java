package com.lucarlosmelo.catalog.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucarlosmelo.catalog.dtos.products.ProductResponse;
import com.lucarlosmelo.catalog.services.ProductService;
import com.lucarlosmelo.catalog.services.exceptions.DataBaseException;
import com.lucarlosmelo.catalog.services.exceptions.ResourceNotFoundException;
import com.lucarlosmelo.catalog.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductResource.class)
@WithMockUser(roles = "ADMIN")
public class ProductResourceTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService service;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @MockBean
    private UserDetailsService userDetailsService;

    private UUID existsId;
    private UUID nonExistsId;
    private UUID dependentId;
    private ProductResponse productResponse;

    @BeforeEach
    void setUp() {
        existsId = UUID.fromString("04b0111e-48ac-4188-8fcf-35691987cc99");
        nonExistsId = UUID.randomUUID();
        dependentId = UUID.randomUUID();

        productResponse = Factory.createProductResponse();
        var page = new PageImpl<>(List.of(productResponse));

        when(service.findAllPaged(any())).thenReturn(page);

        when(service.findById(existsId)).thenReturn(productResponse);
        when(service.findById(nonExistsId)).thenThrow(ResourceNotFoundException.class);

        when(service.insert(any())).thenReturn(productResponse);

        when(service.update(eq(existsId), any())).thenReturn(productResponse);
        when(service.update(eq(nonExistsId), any())).thenThrow(ResourceNotFoundException.class);

        doNothing().when(service).delete(existsId);
        doThrow(ResourceNotFoundException.class).when(service).delete(nonExistsId);
        doThrow(DataBaseException.class).when(service).delete(dependentId);
    }

    @Test
    public void findAllShouldReturnPage() throws Exception {
        var result = mockMvc.perform(get("/products").accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturnProductWhenIdExists() throws Exception {
        var result = mockMvc.perform(get("/products/{id}", existsId).accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void findByIdShouldThrowReturnNotFoundExceptionWhenIdDoesNotExists() throws Exception {
        var result = mockMvc.perform(get("/products/{id}", nonExistsId).accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser("customUsername")
    public void updateShouldReturnProductWhenIdExists() throws Exception {
        var jsonBody = objectMapper.writeValueAsString(productResponse);

        var result = mockMvc.perform(put("/products/{id}", existsId)
                .with(SecurityMockMvcRequestPostProcessors.csrf()).content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        var jsonBody = objectMapper.writeValueAsString(productResponse);

        var result = mockMvc.perform(put("/products/{id}", nonExistsId)
                .with(SecurityMockMvcRequestPostProcessors.csrf()).content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNotFound());
    }

    @Test
    public void insertShouldReturnProductResponseCreated() throws Exception {
        var jsonBody = objectMapper.writeValueAsString(productResponse);

        var result = mockMvc.perform(post("/products", productResponse)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .content(jsonBody).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
        mockMvc.perform(delete("/products/{id}", existsId).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        mockMvc.perform(delete("/products/{id}", nonExistsId)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteShouldReturnDataBaseExceptionWhenDependentId() throws Exception {
        mockMvc.perform(delete("/products/{id}", dependentId)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isConflict());
    }
}
