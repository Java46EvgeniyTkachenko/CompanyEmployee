package telran.spring.company.model;

import java.io.Serializable;

import jakarta.validation.constraints.*;

public class Employee implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	@Min(1)
	@Max(999999999)
	public Integer id;

	@NotEmpty
	@Pattern(regexp = "^[A-Z]{1}[a-z]{1,30}$")
	public String firstName;
	
	@NotEmpty
	@Pattern(regexp = "^[A-Z]{1}[a-z]{1,30}$")
	public String lastName;
	
	@NotEmpty
	@Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$")
	public String birthDate;
	
	@NotNull
	@Min(5000)
	@Max(45000)
	public Integer salary;

	

}
