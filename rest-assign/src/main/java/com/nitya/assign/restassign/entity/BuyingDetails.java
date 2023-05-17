package com.nitya.assign.restassign.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class BuyingDetails {
	
	@Id
	private Integer id;
	
	private Integer userId;
	private String productName;
	
	@CreationTimestamp
	private LocalDateTime purchaseTime;

	public BuyingDetails() {
		
	}
	
	public BuyingDetails(Integer userId, String productName) {
		super();
		this.userId = userId;
		this.productName = productName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public LocalDateTime getPurchaseTime() {
		return purchaseTime;
	}

	public void setPurchaseTime(LocalDateTime purchaseTime) {
		this.purchaseTime = purchaseTime;
	}

	@Override
	public String toString() {
		return "BuyingDetails [id=" + id + ", userId=" + userId + ", productName=" + productName + ", purchaseTime="
				+ purchaseTime + "]";
	}
	
	
}
