package cool.stuff.employee.mgmt.system.service;

import java.util.Arrays;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import cool.stuff.employee.mgmt.system.model.Role;
import cool.stuff.employee.mgmt.system.model.User;
import cool.stuff.employee.mgmt.system.repository.RoleRepository;
import cool.stuff.employee.mgmt.system.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	BCryptPasswordEncoder encoder;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	UserRepository userRepository;

	@Override
	public void saveUser(User user) {
		user.setPassword(encoder.encode(user.getPassword()));
		user.setStatus("VERIFIED");
		
		Role userRole = roleRepository.findByRole("SITE_USER");
		user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
		userRepository.save(user);
	}

	@Override
	public boolean isAlreadyPresent(User user) {
		
		return false;
	}

}
