package cool.stuff.employee.mgmt.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cool.stuff.employee.mgmt.system.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
	
	public Role findByRole(String role);
}
