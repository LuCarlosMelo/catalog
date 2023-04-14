package com.devsuperior.dscatalog.resources;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.exceptions.DataBaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private ProductService service;

	private long existsId;
	private long nonExistsId;
	private long dependentId;
	private ProductDTO productDTO;
	private PageImpl<ProductDTO> page;

	@BeforeEach
	void setUp() throws Exception {
		existsId = 1L;
		nonExistsId = 100L;
		dependentId = 3L;

		productDTO = Factory.createProductDTO();
		page = new PageImpl<>(List.of(productDTO));

		when(service.findAllPaged(any())).thenReturn(page);

		when(service.findById(existsId)).thenReturn(productDTO);
		when(service.findById(nonExistsId)).thenThrow(ResourceNotFoundException.class);

		when(service.insert(any())).thenReturn(productDTO);

		when(service.update(eq(existsId), any())).thenReturn(productDTO);
		when(service.update(eq(nonExistsId), any())).thenThrow(ResourceNotFoundException.class);

		doNothing().when(service).delete(existsId);
		doThrow(ResourceNotFoundException.class).when(service).delete(nonExistsId);
		doThrow(DataBaseException.class).when(service).delete(dependentId);
	}

	@Test
	public void findAllShouldReturnPage() throws Exception {
		ResultActions result = mockMvc.perform(get("/products").accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
	}

	@Test
	public void findByIdShouldReturnProductWhenIdExists() throws Exception {
		ResultActions result = mockMvc.perform(get("/products/{id}", existsId).accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
	}

	@Test
	public void findByIdShouldThrowReturnNotFoundExceptionWhenIdDoesNotxists() throws Exception {
		ResultActions result = mockMvc.perform(get("/products/{id}", nonExistsId).accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNotFound());
	}

	@Test
	public void updateShouldReturnProductWhenIdExists() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(productDTO);

		ResultActions result = mockMvc.perform(put("/products/{id}", existsId).content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
	}

	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(productDTO);

		ResultActions result = mockMvc.perform(put("/products/{id}", nonExistsId).content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNotFound());
	}

	@Test
	public void insertShouldReturnProductDTOCreated() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(productDTO);

		ResultActions result = mockMvc.perform(post("/products", productDTO).content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
	}

	@Test
	public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
		mockMvc.perform(delete("/products/{id}", existsId)).andExpect(status().isNoContent());
	}
	
	@Test
	public void deleteShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
		mockMvc.perform(delete("/products/{id}", nonExistsId)).andExpect(status().isNotFound());
	}
	
	@Test
	public void deleteShouldReturnDataBaseExceptionWhenDependentId() throws Exception {
		mockMvc.perform(delete("/products/{id}", dependentId)).andExpect(status().isBadRequest());
	}
}
