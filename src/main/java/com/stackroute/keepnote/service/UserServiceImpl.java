package com.stackroute.keepnote.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stackroute.keepnote.dao.UserDAO;
import com.stackroute.keepnote.exception.UserAlreadyExistException;
import com.stackroute.keepnote.exception.UserNotFoundException;
import com.stackroute.keepnote.model.User;

/*
* Service classes are used here to implement additional business logic/validation 
* This class has to be annotated with @Service annotation.
* @Service - It is a specialization of the component annotation. It doesn�t currently 
* provide any additional behavior over the @Component annotation, but it�s a good idea 
* to use @Service over @Component in service-layer classes because it specifies intent 
* better. Additionally, tool support and additional behavior might rely on it in the 
* future.
* */
@Service("userService")
public class UserServiceImpl implements UserService {

	/*
	 * Autowiring should be implemented for the userDAO. (Use Constructor-based
	 * autowiring) Please note that we should not create any object using the new
	 * keyword.
	 */
	@Autowired
    UserDAO userDAO;
	/*
	 * This method should be used to save a new user.
	 */

	public boolean registerUser(User user) throws UserAlreadyExistException {
		if(userDAO.registerUser(user)){
			return true;
		}else{
			throw new UserAlreadyExistException("User was already found");
		}
		//return userDAO.registerUser(user);

	}

	/*
	 * This method should be used to update a existing user.
	 */

	public User updateUser(User user, String userId) throws Exception {
		User currentUser = userDAO.getUserById(userId);
		if (currentUser == null) {
			throw new Exception();
		}
		boolean updateStatus=userDAO.updateUser(user);
		//System.out.println(updateStatus+"check all the logs");
		if(!updateStatus){
			throw new Exception();
		}
		return user;
		
	}

	/*
	 * This method should be used to get a user by userId.
	 */

	public User getUserById(String UserId) throws UserNotFoundException {
		User currentUser=userDAO.getUserById(UserId);
		if(currentUser!=null){
			return currentUser;
		}
		else{
			throw new UserNotFoundException("User is not found");
		}

	}

	/*
	 * This method should be used to validate a user using userId and password.
	 */

	public boolean validateUser(String userId, String password) throws UserNotFoundException {
		boolean updateStatus=userDAO.validateUser(userId, password);
		if(!updateStatus){
			throw new UserNotFoundException("User is not found");
		}
		return updateStatus;

	}

	/* This method should be used to delete an existing user. */
	public boolean deleteUser(String UserId) {
		return userDAO.deleteUser(UserId);

	}

}
