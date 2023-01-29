package telran.spring.company;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import telran.spring.company.model.Employee;
import telran.spring.company.service.CompanyService;


interface TestConstants {
	String FILE_NAME = "test-accounts.data";
}
@SpringBootTest(properties = {"ADMIN_PASSWORD=12345.com", "app.file.name=" + TestConstants.FILE_NAME})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CompanyEmployeeApplicationTests {
	public static int idTmp;	
	static Logger LOG = LoggerFactory.getLogger(CompanyEmployeeApplicationTests.class);
	@Autowired
CompanyService companyService;
	@BeforeAll
static	void setUpBeforeAll() throws IOException {	
		if (Files.deleteIfExists(Path.of(TestConstants.FILE_NAME))) {
			LOG.info("file {} has been deleted", TestConstants.FILE_NAME);
		} else {
			LOG.info("file {} not found", TestConstants.FILE_NAME);
		}
	}
	
	@Test
	@Order(1)
	void addEmployeeTest() {
		Employee employee = new Employee();		
		employee.firstName = "Moshe";
		employee.lastName = "Kozlov";
		employee.birthDate = "1998-01-01";
		employee.salary = 32000;		
		companyService.addEmployee(employee);
		
		employee.firstName = "Sara";
		employee.lastName = "Levi";
		employee.birthDate = "2003-01-05";
		employee.salary = 37000;
		companyService.addEmployee(employee);
		
		employee.firstName = "Ivan";
		employee.lastName = "Ivanov";
		employee.birthDate = "1999-01-01";
		employee.salary = 31000;
		Employee employeeTmp = companyService.addEmployee(employee);		
		idTmp = employeeTmp.id;
		assertEquals(employee.lastName,employeeTmp.lastName);
	}
	@Test
	@Order(2)
	void updateEmployeeTest() {
		Employee employee = new Employee();
		employee.id = idTmp;
		employee.firstName = "Ivan";
		employee.lastName = "Ivanov";
		employee.birthDate = "1999-01-01";
		employee.salary = 35000;
		Employee employeeTmp = companyService.updateEmployee(employee);		
		assertEquals(employee.salary,employeeTmp.salary);
	}
	
	@Test
	@Order(3)
	void employeesBySalaryEmployeeTest() {		
		List <Employee> list = (List<Employee>) companyService.employeesBySalary(30000,40000);		
		assertEquals(list.size(),3);
	}
	
	@Test
	@Order(4)
	void employeesByAgeTest() {		
		List <Employee> list = (List<Employee>) companyService.employeesByAge(21,60);		
		assertEquals(list.size(),2);
	}
	@Test
	@Order(5)
	void employeesByBirthMonthTest() {		
		List <Employee> list = (List<Employee>) companyService.employeesByBirthMonth(1);		
		assertEquals(list.size(),3);
	}
	
	@Test
	@Order(6)
	void deleteEmployeeTest() {
		Employee employee = new Employee();		
		employee.id = idTmp;
		employee.firstName = "Ivan";
		employee.lastName = "Ivanov";
		employee.birthDate = "1999-01-01";
		employee.salary = 39000;
		employee = companyService.updateEmployee(employee);	
		Employee employeeTmp = companyService.deleteEmployee(idTmp);		
		assertEquals(employee.salary,employeeTmp.salary);
	}
	
	
	
	

}
