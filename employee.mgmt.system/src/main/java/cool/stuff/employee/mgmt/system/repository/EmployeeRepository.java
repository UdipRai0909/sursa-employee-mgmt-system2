package cool.stuff.employee.mgmt.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cool.stuff.employee.mgmt.system.model.Employee;


public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

}
