package com.barista.coffee.productservice.controller;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.validation.Validation;
import javax.validation.Validator;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.barista.coffee.productservice.ProductServiceControllerAdvice;
import com.barista.coffee.productservice.ProductServiceException;
import com.barista.coffee.productservice.bean.ErrorBean;
import com.barista.coffee.productservice.bean.Product;
import com.barista.coffee.productservice.bean.ProductRequest;
import com.barista.coffee.productservice.service.ProductService;

@SpringBootTest
public class ProductServiceControllerTest {

	@InjectMocks
	ProductServiceController productServiceController;

	@Mock
	private ProductService productService;

	private Validator validator;

	private MockMvc mockMvc;

	@BeforeEach()
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		validator = Validation.buildDefaultValidatorFactory().getValidator();
		this.mockMvc = MockMvcBuilders.standaloneSetup(productServiceController)
				.setControllerAdvice(ProductServiceControllerAdvice.class).build();

		ReflectionTestUtils.setField(productServiceController, "validator", validator);
	}

	@Test
	public void testAddProduct_ValidationErrors() throws Exception {
		ProductRequest productAddRequest = new ProductRequest();
		productAddRequest.setQuantity(0);
		mockMvc.perform(post("/v1/product/{name}", "Barista").content(this.prepareRequestJson(productAddRequest))
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().is(HttpStatus.UNPROCESSABLE_ENTITY.value()));
	}

	@Test
	public void testAddProduct_ServiceError() throws Exception {
		ProductRequest productAddRequest = new ProductRequest();
		productAddRequest.setQuantity(10);
		doThrow(new ProductServiceException(HttpStatus.INTERNAL_SERVER_ERROR,
				new ErrorBean("errorCode", "errorMessage"), null)).when(productService)
				.addProduct(Mockito.anyString(), Mockito.any());
		mockMvc.perform(post("/v1/product/{name}", "Barista").content(this.prepareRequestJson(productAddRequest))
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()));
	}

	@Test
	public void testAddProduct_Success() throws Exception {
		ProductRequest productAddRequest = new ProductRequest();
		productAddRequest.setQuantity(10);
		mockMvc.perform(post("/v1/product/{name}", "Barista").content(this.prepareRequestJson(productAddRequest))
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().is(HttpStatus.NO_CONTENT.value()));
	}

	@Test
	public void testGetProduct() throws Exception {
		Mockito.when(productService.getProduct(Mockito.anyString())).thenReturn(new Product());
		mockMvc.perform(get("/v1/product/{name}", "Barista")).andExpect(status().is(HttpStatus.OK.value()));
	}

	@Test
	public void testGetProduct_NotFound() throws Exception {
		Mockito.when(productService.getProduct(Mockito.anyString()))
				.thenThrow(new ProductServiceException(HttpStatus.NOT_FOUND, new ErrorBean(null, null), null));
		mockMvc.perform(get("/v1/product/{name}", "Barista")).andExpect(status().is(HttpStatus.NOT_FOUND.value()));
	}

	@Test
	public void testUpdateProduct() throws Exception {
		ProductRequest productAddRequest = new ProductRequest();
		productAddRequest.setQuantity(10);
		mockMvc.perform(put("/v1/product/{name}", "Barista").content(this.prepareRequestJson(productAddRequest))
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().is(HttpStatus.OK.value()));
	}

	@Test
	public void testUpdateProduct_ValidationError() throws Exception {
		ProductRequest productAddRequest = new ProductRequest();
		productAddRequest.setQuantity(0);
		mockMvc.perform(put("/v1/product/{name}", "Barista").content(this.prepareRequestJson(productAddRequest))
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().is(HttpStatus.UNPROCESSABLE_ENTITY.value()));
	}

	@Test
	public void testUpdateProduct_ServiceError() throws Exception {
		ProductRequest productAddRequest = new ProductRequest();
		productAddRequest.setQuantity(5);
		doThrow(new ProductServiceException(HttpStatus.UNPROCESSABLE_ENTITY, new ErrorBean("errorCode", "errorMessage"),
				null)).when(productService).updateProduct(Mockito.anyString(), Mockito.any());
		mockMvc.perform(put("/v1/product/{name}", "Barista").content(this.prepareRequestJson(productAddRequest))
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().is(HttpStatus.UNPROCESSABLE_ENTITY.value()));
	}

	private String prepareRequestJson(Object request) {
		return new JSONObject(request).toString();
	}
}
