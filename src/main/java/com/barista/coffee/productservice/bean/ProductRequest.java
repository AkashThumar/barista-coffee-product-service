package com.barista.coffee.productservice.bean;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ProductRequest {

	@NotNull(message = "quantity can not be null")
	@Min(value = 1, message = "quantity must be atleast 1")
	private Integer quantity;
	
	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

}
