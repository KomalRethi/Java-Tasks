package com.test.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.test.model.Employee;
import com.test.repository.EmployeeRespository;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
	
	private final EmployeeRespository repository;
	
	public EmployeeController(EmployeeRespository repository) {
		this.repository = repository;
	}
	
	@GetMapping("/all")
	public List<Employee> getAll(){
		return repository.findAll();
	}
	
	@PostMapping
	public Employee save(@RequestBody Employee employee) {
		return repository.save(employee);
	}
	
	
}
