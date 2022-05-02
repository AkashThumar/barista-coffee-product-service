package com.barista.coffee.productservice.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class Product implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6766754586401408225L;
	private Long productId;
	private String productName;
	private Integer quantity;
	private BigDecimal amount;

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}
