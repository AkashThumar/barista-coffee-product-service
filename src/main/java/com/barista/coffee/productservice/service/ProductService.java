package com.barista.coffee.productservice.service;

import com.barista.coffee.productservice.bean.ProductAddRequest;

public interface ProductService {

	public void addProduct(String productName, ProductAddRequest request);
}
