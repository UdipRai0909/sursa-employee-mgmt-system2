package cool.stuff.employee.mgmt.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cool.stuff.employee.mgmt.system.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	
}
