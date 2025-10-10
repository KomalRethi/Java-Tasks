package com.test.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
	int id;
	String name;
	double price;
	@Override
	public String toString() {
		return "Order [id=" + id + ", name=" + name + ", price=" + price + "]";
	}
	
	
}
