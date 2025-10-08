package com.inv.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	@Value(value="${topic.Inventory}")
	private String inventoryTopic;
	
	@PostMapping("/create")
	String createInventory(@RequestBody String OrderDetails) {
		kafkaTemplate.send(inventoryTopic, OrderDetails);
		return "Order placed successfully!!";
	}
}
