package com.barista.coffee.productservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.barista.coffee.productservice.model.Product;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
	
	Product findByName(String name);
}
