package com.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.test.model.Employee;

public interface EmployeeRespository extends JpaRepository<Employee, Integer>{
	
}
