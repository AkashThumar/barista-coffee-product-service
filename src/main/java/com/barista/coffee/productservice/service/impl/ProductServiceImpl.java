package com.barista.coffee.productservice.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.barista.coffee.productservice.ProductServiceException;
import com.barista.coffee.productservice.bean.ErrorBean;
import com.barista.coffee.productservice.bean.ProductRequest;
import com.barista.coffee.productservice.model.Product;
import com.barista.coffee.productservice.repository.ProductRepository;
import com.barista.coffee.productservice.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	private static final String ERROR_MESSAGE_BCPS_103 = "Not enough quantity for update!";
	private static final String ERROR_CODE_BCPS_103 = "BCPS-103";
	private static final String ERROR_MESSAGE_BCPS_101 = "Requested product does not exist";
	private static final String ERROR_CODE_BCPS_101 = "BCPS-101";
	
	@Autowired
	private ProductRepository productRepository;

	@Override
	public void addProduct(String productName, ProductRequest request) {
		Product product = productRepository.findByName(productName);
		if (null != product) {
			product.setQuantity(request.getQuantity() + product.getQuantity());
		} else {
			product = new Product();
			product.setName(productName);
			product.setQuantity(request.getQuantity());
			product.setAmount(new BigDecimal(50));
			product.setIsactive(1);
			product.setAddeddate(new Timestamp(System.currentTimeMillis()));
			product.setAddedBy("System");
		}
		productRepository.save(product);
	}

	@Override
	public com.barista.coffee.productservice.bean.Product getProduct(String productName) {
		Product product = productRepository.findByName(productName);
		if (null != product) {
			com.barista.coffee.productservice.bean.Product productResponse = new com.barista.coffee.productservice.bean.Product();
			productResponse.setProductId(product.getProductid());
			productResponse.setProductName(product.getName());
			productResponse.setQuantity(product.getQuantity());
			return productResponse;
		} else {
			throw new ProductServiceException(HttpStatus.NOT_FOUND,
					new ErrorBean(ERROR_CODE_BCPS_101, ERROR_MESSAGE_BCPS_101), null);
		}
	}
	

	@Override
	public com.barista.coffee.productservice.bean.Product updateProduct(String productName, ProductRequest request) {
		Product product = productRepository.findByName(productName);
		if (null != product) {
			if (this.checkForEnoughQuantity(product, request.getQuantity())) {
				com.barista.coffee.productservice.bean.Product productResponse = new com.barista.coffee.productservice.bean.Product();
				productResponse.setProductId(product.getProductid());
				productResponse.setProductName(product.getName());
				productResponse.setQuantity(product.getQuantity());
				return productResponse;
			} else {
				throw new ProductServiceException(HttpStatus.UNPROCESSABLE_ENTITY, new ErrorBean(ERROR_CODE_BCPS_103, ERROR_MESSAGE_BCPS_103), null);
			}
		} else {
			throw new ProductServiceException(HttpStatus.NOT_FOUND,
					new ErrorBean(ERROR_CODE_BCPS_101, ERROR_MESSAGE_BCPS_101), null);
		}
	}
	
	private boolean checkForEnoughQuantity(Product product, Integer quantity) {
		if (0 > (product.getQuantity() - quantity)) {
			return false;
		} else {
			product.setQuantity(product.getQuantity() - quantity);
			productRepository.save(product);
			return true;
		}
	}
	
}