package cool.stuff.employee.mgmt.system.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cool.stuff.employee.mgmt.system.model.Employee;
import cool.stuff.employee.mgmt.system.repository.EmployeeRepository;

@Service
public class EmployeeDAO {
	
	@Autowired
	EmployeeRepository empRepo;
	
	// Save an Employee
	public Employee save(Employee emp) {
		return empRepo.save(emp);
	}
	
	// Search all employees
	public List<Employee> findAll() {
		return empRepo.findAll();
	}
	
	// Get an employee by Id
	public Employee findById(Integer id) {
		return empRepo.findById(id).get();
	}
	
	// Delete a Employee record
	public void delete(Employee emp) {
		empRepo.delete(emp);
	}
}
