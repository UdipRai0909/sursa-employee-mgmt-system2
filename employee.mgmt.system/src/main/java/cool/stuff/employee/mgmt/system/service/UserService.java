package cool.stuff.employee.mgmt.system.service;

import cool.stuff.employee.mgmt.system.model.User;

public interface UserService {
	
	public void saveUser(User user);
	public boolean isAlreadyPresent(User user);
}
