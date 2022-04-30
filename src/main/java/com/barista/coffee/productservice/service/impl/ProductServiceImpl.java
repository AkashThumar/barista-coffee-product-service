package com.barista.coffee.productservice.service.impl;

import org.springframework.stereotype.Service;

import com.barista.coffee.productservice.bean.ProductAddRequest;
import com.barista.coffee.productservice.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Override
	public void addProduct(String productName, ProductAddRequest request) {
		// TODO insert requested product to product table
		
	}

}
