package com.barista.coffee.productservice.bean;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ProductAddRequest {

	@NotNull(message = "quantity can not be null")
	@Min(value = 1, message = "quantity must be atleast 1")
	private Integer quantity;

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
