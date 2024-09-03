package com.microservices.product.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.product.service.DTO.ProductRequest;
import com.microservices.product.service.model.Product;
import com.microservices.product.service.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@DirtiesContext
class ProductServiceApplicationTests {
	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.4");

	@Autowired
	private MockMvc mockMvc;
    @Autowired
    private DefaultMockMvcBuilder mockMvcBuilder;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private ProductRepository productRepository;

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@BeforeEach
	void cleanUp() {
		productRepository.deleteAll();
	}


	@Test
	void shouldCreateProduct() throws Exception {
		ProductRequest productRequest = getProductRequest();
		String productRequestString = objectMapper.writeValueAsString(productRequest);

		mockMvc.perform(MockMvcRequestBuilders.post("/product/create")
				.contentType(MediaType.APPLICATION_JSON)
				.content(productRequestString))
				.andExpect(status().isCreated());
        Assertions.assertEquals(1, productRepository.findAll().size());
	}

	private ProductRequest getProductRequest() {
		return ProductRequest.builder()
				.name("soap")
				.description("washing powder")
				.price(200.0)
				.build();

	}

	@Test
	void shouldFindProduct() throws Exception {
		Product product = Product.builder()
				.name("soap")
				.description("washing powder")
				.price(200.0)
				.build();
		productRepository.save(product);

		mockMvc.perform(MockMvcRequestBuilders.get("/product/findAll"))
				.andExpect(status().isOk());

		Assertions.assertEquals(1, productRepository.findAll().size());

	}

}
