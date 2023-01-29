package telran.spring.company.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import telran.spring.company.model.Employee;
import telran.spring.company.service.CompanyService;

@RestController
@RequestMapping        //("employees")
@Validated
public class CompanyController {

	@Autowired
	CompanyService companyService;
	
	ObjectMapper mapper = new ObjectMapper();

	@PostMapping("/employees")
	String addEmployee(@RequestBody @Valid Employee empl) throws Exception {
		Employee addedEmployee = companyService.addEmployee(empl);
		return mapper.writeValueAsString(addedEmployee);
	}
	
	@PutMapping("/employees")
	String updateEmployee(@RequestBody @Valid Employee empl) throws Exception {
		Employee updatedEmployee = companyService.updateEmployee(empl);
		return mapper.writeValueAsString(updatedEmployee);
	}
	
	@DeleteMapping("/employees/{id}")
	String deleteEmployee(@Valid Integer id) throws Exception {
		return mapper.writeValueAsString(companyService.deleteEmployee(id));
	}
	
	@GetMapping("/employees/salary/{salaryFrom}/{salaryTo}")
	String employeesBySalary(@Valid int salaryFrom, @Valid int salaryTo) throws Exception {
		return mapper.writeValueAsString(companyService.employeesBySalary(salaryFrom, salaryTo));
	}
	
	@GetMapping("/employees/age/{ageFrom}/{ageTo}")
	String employeesByAge(int ageFrom, int ageTo) throws Exception {
		return mapper.writeValueAsString(companyService.employeesByAge(ageFrom, ageTo));
	}
	
	@GetMapping("/employees/month/{monthNumber}")
	String employeesByBirthMonth(int monthNumber) throws Exception {
		return mapper.writeValueAsString(companyService.employeesByBirthMonth(monthNumber));
	}

}
