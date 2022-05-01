package com.barista.coffee.productservice.service;

import com.barista.coffee.productservice.bean.Product;
import com.barista.coffee.productservice.bean.ProductRequest;

public interface ProductService {

	/**
	 * This method is used to add product in barista product table
	 * 
	 * @param productName
	 * @param request
	 */
	public void addProduct(String productName, ProductRequest request);

	/**
	 * This method is used to get product from barista product table
	 * 
	 * @param productName
	 * @return
	 */
	public Product getProduct(String productName);
	
	/**
	 * This method is used to update the quantity of the product as per the order requested
	 * In case of not enough quatity would respond with error response  
	 * 
	 * @param productName
	 * @param request
	 * @return
	 */
	public Product updateProduct(String productName, ProductRequest request);
}
