package com.barista.coffee.productservice.service.impl;

import java.util.concurrent.locks.Lock;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.integration.support.locks.LockRegistry;

import com.barista.coffee.productservice.ProductServiceException;
import com.barista.coffee.productservice.bean.ProductRequest;
import com.barista.coffee.productservice.model.Product;
import com.barista.coffee.productservice.repository.ProductRepository;

@SpringBootTest
public class ProductServiceImplTest {

	@InjectMocks
	ProductServiceImpl productService;

	@Mock
	private ProductRepository productRepository;

	@Mock
	private LockRegistry lockRegistory;
	
	@BeforeEach
	public void setUp() throws InterruptedException {
		MockitoAnnotations.openMocks(this);
		
		Lock mockedLock = Mockito.mock(Lock.class);
		Mockito.when(lockRegistory.obtain(Mockito.any())).thenReturn(mockedLock);
		Mockito.when(mockedLock.tryLock(Mockito.anyLong(), Mockito.any())).thenReturn(true);
	}

	@Test
	public void testAddProduct() {
		ProductRequest productRequest = new ProductRequest();
		productRequest.setQuantity(10);
		Mockito.when(productRepository.findByName(Mockito.anyString())).thenReturn(null);
		productService.addProduct("ProductName", productRequest);
		Mockito.verify(productRepository, Mockito.times(1)).save(Mockito.any());
	}

	@Test
	public void testAddProduct_ProductNExist() {
		Product product = new Product();
		product.setQuantity(5);
		ProductRequest productRequest = new ProductRequest();
		productRequest.setQuantity(10);
		Mockito.when(productRepository.findByName(Mockito.anyString())).thenReturn(product);
		productService.addProduct("ProductName", productRequest);
		Mockito.verify(productRepository, Mockito.times(1)).save(Mockito.any());
	}

	@Test
	public void testGetProduct() {
		Product product = new Product();
		product.setQuantity(5);
		product.setName("ProductName");
		Mockito.when(productRepository.findByName(Mockito.anyString())).thenReturn(product);
		Assertions.assertNotNull(productService.getProduct("ProductName"));
	}

	@Test
	public void testGetProduct_NotExist() {
		Mockito.when(productRepository.findByName(Mockito.anyString())).thenReturn(null);
		try {
			productService.getProduct("ProductName");
		} catch (ProductServiceException serviceException) {
			Assertions.assertEquals(HttpStatus.NOT_FOUND, serviceException.getStatus());
		}
	}

	@Test
	public void testUpdateProduct() {
		Product product = new Product();
		product.setProductid(1l);
		product.setQuantity(5);
		product.setName("ProductName");
		ProductRequest productRequest = new ProductRequest();
		productRequest.setQuantity(4);
		Mockito.when(productRepository.findByName(Mockito.anyString())).thenReturn(product);
		Assertions.assertNotNull(productService.updateProduct("productName", productRequest));
	}

	@Test
	public void testUpdateProduct_NotExist() {
		ProductRequest productRequest = new ProductRequest();
		productRequest.setQuantity(4);
		Mockito.when(productRepository.findByName(Mockito.anyString())).thenReturn(null);
		try {
			productService.updateProduct("productName", productRequest);
		} catch (ProductServiceException serviceException) {
			Assertions.assertEquals(HttpStatus.NOT_FOUND, serviceException.getStatus());
		}
	}

	@Test
	public void testUpdateProduct_NotEnoughQuantity() {
		Product product = new Product();
		product.setProductid(1l);
		product.setQuantity(5);
		product.setName("ProductName");
		ProductRequest productRequest = new ProductRequest();
		productRequest.setQuantity(6);
		Mockito.when(productRepository.findByName(Mockito.anyString())).thenReturn(product);
		try {
			productService.updateProduct("productName", productRequest);
		} catch (ProductServiceException serviceException) {
			Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, serviceException.getStatus());
		}
	}

}
