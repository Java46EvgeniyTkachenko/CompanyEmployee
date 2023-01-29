package telran.spring.company.service;

import telran.spring.company.model.Employee;

public interface CompanyService {

	Employee addEmployee(Employee empl);
	
	Employee updateEmployee(Employee empl);
	
	Employee deleteEmployee(int id);
	
	Iterable<Employee> employeesBySalary(int salaryFrom, int salaryTo);
	
	Iterable<Employee> employeesByAge(int ageFrom, int ageTo);
	
	Iterable<Employee> employeesByBirthMonth(int monthNumber);
	
	
}
