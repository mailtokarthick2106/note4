package com.stackroute.keepnote.controller;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.stackroute.keepnote.exception.UserAlreadyExistException;
import com.stackroute.keepnote.exception.UserNotFoundException;
import com.stackroute.keepnote.model.User;
import com.stackroute.keepnote.service.UserService;

/*
 * As in this assignment, we are working on creating RESTful web service, hence annotate
 * the class with @RestController annotation. A class annotated with the @Controller annotation
 * has handler methods which return a view. However, if we use @ResponseBody annotation along
 * with @Controller annotation, it will return the data directly in a serialized 
 * format. Starting from Spring 4 and above, we can use @RestController annotation which 
 * is equivalent to using @Controller and @ResposeBody annotation
 */
@RestController
public class UserController {

	/*
	 * Autowiring should be implemented for the UserService. (Use
	 * Constructor-based autowiring) Please note that we should not create an
	 * object using the new keyword
	 */
	@Autowired
	UserService userService;
	// HttpSession session;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	/*
	 * Define a handler method which will create a specific user by reading the
	 * Serialized object from request body and save the user details in a User
	 * table in the database. This handler method should return any one of the
	 * status messages basis on different situations: 1. 201(CREATED) - If the
	 * user created successfully. 2. 409(CONFLICT) - If the userId conflicts
	 * with any existing user
	 * 
	 * Note: ------ This method can be called without being logged in as well as
	 * when a new user will use the app, he will register himself first before
	 * login.
	 * 
	 * This handler method should map to the URL "/user/register" using HTTP
	 * POST method
	 */
	@PostMapping(value = "/user/register")
	public ResponseEntity<User> registerUser(@RequestBody User user) {
		try {
			if (userService.validateUser(user.getUserId(), user.getUserPassword())) {
				System.out.println("A User with name " + user.getUserId() + " already exist");
				return new ResponseEntity<User>(HttpStatus.CONFLICT);
			}
			try {
				userService.registerUser(user);
			} catch (UserAlreadyExistException e) {
				// TODO Auto-generated catch block
				return new ResponseEntity<User>(HttpStatus.CONFLICT);
				//e.printStackTrace();
			}
		} catch (UserNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ResponseEntity<User>(user, HttpStatus.CREATED);
	}

	/*
	 * Define a handler method which will update a specific user by reading the
	 * Serialized object from request body and save the updated user details in
	 * a user table in database handle exception as well. This handler method
	 * should return any one of the status messages basis on different
	 * situations: 1. 200(OK) - If the user updated successfully. 2. 404(NOT
	 * FOUND) - If the user with specified userId is not found. 3.
	 * 401(UNAUTHORIZED) - If the user trying to perform the action has not
	 * logged in.
	 * 
	 * This handler method should map to the URL "/user/{id}" using HTTP PUT
	 * method.
	 */
	@PutMapping("/user/{id}")
	public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User user, HttpSession session) {

		User currentUser = null;
		try {
			if (session != null) {
				String userId = (String) session.getAttribute("loggedInUserId");

				if (id.equalsIgnoreCase(userId)) {
					currentUser = userService.getUserById(id);

					if (currentUser == null) {
						System.out.println("User with id " + id + " not found");
						return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
					}

					currentUser.setUserName(user.getUserName());
					currentUser.setUserPassword(user.getUserPassword());
					currentUser.setUserMobile(user.getUserMobile());
					currentUser.setUserAddedDate(new Date());

					currentUser = userService.updateUser(currentUser, id);

				} else {
					System.out.println(userId + "dsjdsjdsdkjslkdkjs" + id);
					return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);

				}
			}
			/*
			 * if (currentUser == null) return new
			 * ResponseEntity<User>(HttpStatus.UNAUTHORIZED)
			 */;

		} catch (UserNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<User>(currentUser, HttpStatus.OK);

	}

	/*
	 * Define a handler method which will delete a user from a database.
	 * 
	 * This handler method should return any one of the status messages basis on
	 * different situations: 1. 200(OK) - If the user deleted successfully from
	 * database. 2. 404(NOT FOUND) - If the user with specified userId is not
	 * found. 3. 401(UNAUTHORIZED) - If the user trying to perform the action
	 * has not logged in.
	 * 
	 * This handler method should map to the URL "/user/{id}" using HTTP Delete
	 * method" where "id" should be replaced by a valid userId without {}
	 */
	@DeleteMapping("/user/{id}")
	public ResponseEntity<User> deleteUser(@PathVariable String id, HttpSession session) {
		System.out.println("Fetching & Deleting User with id " + id);

		User user;
		try {
			if (session != null) {
				String userId = (String) session.getAttribute("loggedInUserId");

				if (id.equalsIgnoreCase(userId)) {
					user = userService.getUserById(userId);

					if (user == null) {
						System.out.println("Unable to delete. User with id " + id + " not found");
						return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
					}

					boolean status = userService.deleteUser(id);
				} else {
					// if (!status) {
					return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);
					// }
				}
			}
		} catch (UserNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<User>(HttpStatus.OK);

	}

	/*
	 * Define a handler method which will show details of a specific user handle
	 * UserNotFoundException as well. This handler method should return any one
	 * of the status messages basis on different situations: 1. 200(OK) - If the
	 * user found successfully. 2. 401(UNAUTHORIZED) - If the user trying to
	 * perform the action has not logged in. 3. 404(NOT FOUND) - If the user
	 * with specified userId is not found. This handler method should map to the
	 * URL "/user/{id}" using HTTP GET method where "id" should be replaced by a
	 * valid userId without {}
	 */
	@GetMapping("/user/{id}")
	public ResponseEntity<User> getUser(@PathVariable("id") String id, HttpSession session) {
		User user = null;
		try {
			if (session != null) {
				String userId = (String) session.getAttribute("loggedInUserId");
				if (id.equalsIgnoreCase(userId)) {
					user = userService.getUserById(id);
					if (user == null) {
						return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
					}

				} else {
					System.out.println(userId + "dsjdsjdsdkjslkdkjs" + id);
					return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);

				}
			}
		} catch (UserNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

}