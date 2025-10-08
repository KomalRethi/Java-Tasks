package com.order.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.order.TestOrder;

@Service
public class OrderService {
	
	private final List<String> message = new ArrayList<String>();
	
	@KafkaListener(topics="${topic.Inventory}", groupId = "Inventory_group")
	public void consume(String m) {
		System.out.println("Received order: "+ m);
		message.add(m);
	}
	
	public List<String> getMessage(){
		return message;
	}
}
