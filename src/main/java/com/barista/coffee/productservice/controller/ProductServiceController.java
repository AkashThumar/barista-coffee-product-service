package com.barista.coffee.productservice.controller;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.NotBlank;
import javax.validation.groups.Default;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.barista.coffee.productservice.ProductServiceException;
import com.barista.coffee.productservice.bean.ErrorBean;
import com.barista.coffee.productservice.bean.ProductAddRequest;
import com.barista.coffee.productservice.service.ProductService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class ProductServiceController {
	
	@Autowired
	private Validator validator;
	
	@Autowired
	private ProductService productService;
	
	@ApiOperation(value = "Product Addition API", notes = "Product Addition API", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "Product Added Successfully", response = String.class),
			@ApiResponse(code = 400, message = "Bad request parameters"),
			@ApiResponse(code = 422, message = "Unprocessable entity", response = ErrorBean.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = ErrorBean.class)
	})
	@PostMapping(value = "/v1/product/{name}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> addProduct(@RequestBody ProductAddRequest productAddRequest,
			@PathVariable(value = "name") @NotBlank(message = "product name can not be null") String productName) {
		
		Set<ConstraintViolation<ProductAddRequest>> validationErrors = validator.validate(productAddRequest, Default.class);
		if (null != validationErrors && !validationErrors.isEmpty()) {
			String message = validationErrors.stream().findFirst().get().getMessage(); 
			throw new ProductServiceException(HttpStatus.UNPROCESSABLE_ENTITY, new ErrorBean("BCPS-100", message), null);
		} else {
			productService.addProduct(productName, productAddRequest);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}
}
