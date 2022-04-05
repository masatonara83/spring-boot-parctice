package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
//
//	// nameを指定
//	@Query(name = "Employee.searchByNameVariable")
//	Collection<Employee> findSample(String name);
//	
//	//「エンティティ名.メソッド名」が一致していれば省略も可能
//	Collection<Employee> searchByNameVariable(String name);
//	
//	//SQLでの実行
//	@Query(nativeQuery = true)
//	Collection<Employee> searchWithNativeQuery(String name);
}
