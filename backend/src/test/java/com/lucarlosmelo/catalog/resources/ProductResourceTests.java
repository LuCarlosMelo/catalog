package com.lucarlosmelo.catalog.resources;

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

import com.lucarlosmelo.catalog.dtos.products.ProductResponse;
import com.lucarlosmelo.catalog.dtos.products.ProductUpdateRequest;
import com.lucarlosmelo.catalog.services.ImplProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.lucarlosmelo.catalog.dtos.products.ProductInsertRequest;
import com.lucarlosmelo.catalog.services.exceptions.DataBaseException;
import com.lucarlosmelo.catalog.services.exceptions.ResourceNotFoundException;
import com.lucarlosmelo.catalog.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private ImplProductService service;

	private long existsId;
	private long nonExistsId;
	private long dependentId;
	private ProductResponse productResponse;
	private ProductInsertRequest productInsertRequest;
	private ProductUpdateRequest productUpdateRequest;
	private PageImpl<ProductResponse> page;

	@BeforeEach
	void setUp() throws Exception {
		existsId = 1L;
		nonExistsId = 100L;
		dependentId = 3L;

		productResponse = Factory.createProductResponse();
		page = new PageImpl<>(List.of(productResponse));

		when(service.findAllPaged(any())).thenReturn(page);

		when(service.findById(existsId)).thenReturn(productResponse);
		when(service.findById(nonExistsId)).thenThrow(ResourceNotFoundException.class);

		when(service.insert(any())).thenReturn(productInsertRequest);

		when(service.update(eq(existsId), any())).thenReturn(productUpdateRequest);
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
		String jsonBody = objectMapper.writeValueAsString(productResponse);

		ResultActions result = mockMvc.perform(put("/products/{id}", existsId).content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
	}

	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(productResponse);

		ResultActions result = mockMvc.perform(put("/products/{id}", nonExistsId).content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNotFound());
	}

	@Test
	public void insertShouldReturnproductResponseCreated() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(productResponse);

		ResultActions result = mockMvc.perform(post("/products", productResponse).content(jsonBody)
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
