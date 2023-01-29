package telran.spring.company.service;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jakarta.annotation.*;
import telran.spring.company.model.Employee;

@Service
public class CompanyServiceImpl implements CompanyService {
	
	private static Logger LOG = LoggerFactory.getLogger(CompanyService.class);
	
	@Value("${app.file.name:empl.data}")
	private String fileName;
	private HashMap<Integer, Employee> employees;

	

	@Override
	public Employee addEmployee(Employee empl) {
		
		Employee employeeTmp = new Employee(); 
		employeeTmp.id = (int) (Math.random() * 999999999);		
		employeeTmp.firstName = empl.firstName;
		employeeTmp.lastName = empl.lastName;
		employeeTmp.birthDate = empl.birthDate;
		employeeTmp.salary = empl.salary;
		Employee res = employees.put(employeeTmp.id, employeeTmp);
		if (res != null) {
			throw new IllegalArgumentException("Emloyee "+employeeTmp.id.toString()+" alredy exsists!");			
		}
		LOG.debug("New employee {} successfuly added.", employeeTmp.firstName);
		return employeeTmp;
	}

	@Override
	public Employee updateEmployee(Employee empl) {
	
		Employee employeeTmp = new Employee();
		if (employees.containsKey(empl.id)) {
			employeeTmp = employees.get(empl.id);
			employees.get(empl.id).firstName = empl.firstName;
			employees.get(empl.id).lastName = empl.lastName;
			employees.get(empl.id).birthDate = empl.birthDate;
			employees.get(empl.id).salary = empl.salary;
			LOG.debug("Successfuly updated an employee with id {}.", empl.id);
		} else if (Integer.toString(empl.id).isEmpty() || !employees.containsKey(empl.id)) {
			LOG.warn("Employee {} is't empty or not exists!", empl.id);
			throw new NoSuchElementException("Employee " +  empl.id.toString()+" is't empty or not exists!");
		}
		return employeeTmp;
	}

	@Override
	public Employee deleteEmployee(int id) {
		if (employees.containsKey(id)) {
			LOG.debug("Successfuly deleted an employee with id {}.", id);
			return employees.remove(id);
		} else {
			LOG.warn("Employee id- " +  id +" not exists!");
			throw new NoSuchElementException("Employee id- " +  id +" not exists!");
			
		}
	}

	@Override
	public List<Employee> employeesBySalary(int salaryFrom, int salaryTo) {
		if (salaryFrom > salaryTo) {
			throw new IllegalArgumentException("Selary not relevant!");
		}
		List<Employee> res = employees.keySet().stream().map(e -> employees.get(e)).filter(e -> (e.salary >= salaryFrom && e.salary <= salaryTo)).toList();
		if (!res.isEmpty()) {
			LOG.debug("Found {} employees in the given salary range.", res.size());
		} else {
			LOG.warn("Not found employees in the given salary range.");
		}
		return res;
	}

	@Override
	public List<Employee> employeesByAge(int ageFrom, int ageTo) {
		if (ageFrom > ageTo) {
			throw new IllegalArgumentException();
		}
		List<Employee> res = employees.keySet().stream().map(e -> employees.get(e)).filter(e -> (getAge(e) >= ageFrom && getAge(e) <= ageTo)).toList();
		if (!res.isEmpty()) {
			LOG.debug("Found {} employees in the given salary range.", res.size());
		} else {
			LOG.warn("Not found employees in the given salary range.");
		}
		return res;
	}
	
	@Override
	public List<Employee> employeesByBirthMonth(int monthNumber) {
		if (monthNumber > 12 || monthNumber < 1) {
			throw new IllegalArgumentException("Number month not valid!");
		}
		List <Employee> res = employees.keySet().stream().map(e -> employees.get(e)).filter(e -> monthNumber == getBirthMonth(e)).toList();
		if (res.isEmpty()) {
			LOG.debug("Found {} employees born in the given month.", res.size());
		} else {
			LOG.warn("Not employees born in the given month.");
		}
		return res;
	}
	
	private int getAge(Employee empl) {
		LocalDate emplBirthDate = LocalDate.parse(empl.birthDate);
		return Period.between(emplBirthDate, LocalDate.now()).getYears();
	}

		
	private int getBirthMonth(Employee empl) {
		return Integer.parseInt(empl.birthDate.split("-")[1]);
	}
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	void restoreEmployees() {
		try(ObjectInputStream input = new ObjectInputStream(new FileInputStream(fileName))) {
			employees = (HashMap<Integer, Employee>) input.readObject();
			LOG.debug("Employees {} has been restored", employees.keySet());
		} catch(FileNotFoundException e) {
			LOG.warn("File {} doesn't exists", fileName);
			employees = new HashMap<>();
		} catch (Exception e) {
			LOG.error("Error restoring employees {}", e.getMessage());
		}
	}
	
	@PreDestroy
	void saveEmployees() {
		try(ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fileName))) {
			output.writeObject(employees);
			LOG.debug("Employees saved to file {}", fileName);
		} catch(Exception e) {
			LOG.error("Error saving employees to file {}", e.getMessage());
		}
	}

}
